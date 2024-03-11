package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Dish;
import org.hse.software.construction.restapp.entity.Order;
import org.hse.software.construction.restapp.util.CookingStatus;
import org.hse.software.construction.restapp.util.Pair;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

public class RestaurantServiceImpl implements RestaurantService {
    private DishServiceImpl dishService;
    private OrderServiceImpl orderService;
    private final Map<UUID, List<Future<?>>> cookingTasks = new ConcurrentHashMap<>();
    private final ExecutorService kitchenExecutor = Executors.newFixedThreadPool(3);
    ;

    public RestaurantServiceImpl(DishServiceImpl dishService, OrderServiceImpl orderService) {
        this.dishService = dishService;
        this.orderService = orderService;
    }

    public List<Dish> getMenu() {
        return dishService.getAll();
    }

    public Order updateOrder(Order order) {
        return orderService.updateOrder(order);
    }

    public Order orderFindById(UUID id) {
        return orderService.findById(id);
    }

    public List<Pair<String, Integer>> checkFeedback() {
        List<Order> orders = orderService.getAll();
        List<Pair<String, Integer>> feedbackList = new ArrayList<>();
        for (Order order : orders) {
            if (order.getFeedback() != null) {
                feedbackList.add(new Pair<>(order.getFeedback(), order.getEvaluation()));
            }
        }
        return feedbackList;
    }

    public boolean addDishToOrder(String dishName, UUID orderId, Integer count) {
        Order order = orderService.findById(orderId);
        Dish dish = dishService.findByName(dishName);
        if (dishService.reserveDish(dish, count) && count > 0) {
            order.addDish(dish.getId(), count, dish.getPrice());
            orderService.updateOrder(order);
            dishService.updateDish(dish);
            return true;
        } else {
            System.out.println("Нельзя заказать такое количество порций!");
            return false;
        }
    }

    public void makeFeedback(UUID orderId, String message, int evaluation) {
        Order order = orderService.findById(orderId);
        order.setFeedback(message);
        order.setEvaluation(evaluation);
        orderService.updateOrder(order);
    }

    public BigDecimal checkProfit() {
        List<Order> orders = orderService.getAll();
        BigDecimal totalProfit = BigDecimal.ZERO;
        for (Order order : orders) {
            if (order.getPaymentStatus()) {
                totalProfit = totalProfit.add(order.getCost());
            }
        }

        return totalProfit;
    }

    public void addDishToCookingOrder(String dishName, UUID orderId, Integer count) {
        Order order = orderService.findById(orderId);
        Dish dish = dishService.findByName(dishName);
        if (!addDishToOrder(dishName, orderId, count)) {
            return;
        }
        List<Future<?>> tasks = cookingTasks.getOrDefault(orderId, new ArrayList<>());

        if (tasks == null) {
            tasks = new ArrayList<>();
            cookingTasks.put(orderId, tasks);
        }
        System.out.println("Начали готовить дополнительные блюда...");
        for (int i = 0; i < count; i++) {
            Future<?> future = kitchenExecutor.submit(() -> {
                cookDishProcess(dish);
            });
            tasks.add(future);
        }
        checkOrderComplete(order);
    }


    public void deleteOrderById(UUID id) {
        orderService.deleteOrderById(id);
    }

    public void processOrder(Order order) {
        order.setStatus(CookingStatus.IN_PROGRESS);
        orderService.updateOrder(order);
        List<Future<?>> tasks = new ArrayList<>();
        order.getDishes().forEach((dishId, amount) -> {
                    for (int i = 0; i < amount; ++i) {
                        Dish dish = dishService.findById(dishId);
                        Future<?> future = kitchenExecutor.submit(() -> {
                            cookDishProcess(dish);
                        });
                    }
                }
        );
        cookingTasks.put(order.getId(), tasks);
        checkOrderComplete(order);
    }

    private void checkOrderComplete(Order order) {
        kitchenExecutor.submit(() -> {
            List<Future<?>> tasks = cookingTasks.get(order.getId());

            boolean allDone = tasks.stream().allMatch(Future::isDone);
            while (!allDone) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    allDone = tasks.stream().allMatch(Future::isDone);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            completeOrder(order);
            cookingTasks.remove(order.getId());
        });
    }

    private void completeOrder(Order order) {
        order.setStatus(CookingStatus.COMPLETED);
        orderService.updateOrder(order);
        System.out.println("Еда готова!!!");
    }

    public Order saveOrder(Order order) {
        return orderService.saveOrder(order);
    }

    public void updateDishPrice(String dishName, BigDecimal price) {
        Dish dish = dishService.findByName(dishName);
        if (dish != null) {
            dish.setPrice(price);
            dishService.updateDish(dish);
        } else {
            System.out.println("cant update price. wrong name or dish dont exist");
        }
    }

    public void updateDishCookingTime(String dishName, Integer cookingTime) {
        Dish dish = dishService.findByName(dishName);
        if (dish != null) {
            dish.setCookingTime(cookingTime);
            dishService.updateDish(dish);
        } else {
            System.out.println("cant update cooking time. wrong name or dish dont exist");
        }
    }

    public void updateDishCount(String dishName, Integer count) {
        Dish dish = dishService.findByName(dishName);
        if (dish != null) {
            dish.setCount(count);
            dishService.updateDish(dish);
        } else {
            System.out.println("cant update cooking time. wrong name or dish dont exist");
        }
    }

    public void addDishMenu(String dishName, BigDecimal price, Integer cookingTime, Integer count) {
        Dish searchDish = dishService.findByName(dishName);
        Dish newDish = new Dish(dishName, price, cookingTime, count);
        if (searchDish != null) {
            System.out.println("Блюдо уже существует, можете изменить параметры");
        } else {
            dishService.saveDish(newDish);
        }
    }

    public void DeleteDishMenu(String dishName) {
        Dish searchDish = dishService.findByName(dishName);
        if (searchDish != null) {
            dishService.deleteDish(dishName);
            System.out.println("Блюдо удалено");
        } else {
            System.out.println("нет блюда с таким именем");
        }
    }

    public synchronized void cancelOrder(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.setStatus(CookingStatus.DENIED);
        orderService.updateOrder(order);
        List<Future<?>> tasks = cookingTasks.get(orderId);
        if (tasks != null) {
            tasks.forEach(future -> future.cancel(true));
        }
        cookingTasks.remove(orderId);
        System.out.println("order denied");
    }

    public void payOrder(UUID orderId) {
        Order order = orderService.findById(orderId);
        order.setPaymentStatus(true);
        orderService.updateOrder(order);
        System.out.println("Заказ оплачен");
    }

    private void cookDishProcess(Dish dish) {
        try {
            TimeUnit.SECONDS.sleep((long) dish.getCookingTime());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
