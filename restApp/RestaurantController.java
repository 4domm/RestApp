package org.hse.software.construction.restapp.controller;

import org.hse.software.construction.restapp.entity.Dish;
import org.hse.software.construction.restapp.entity.Order;
import org.hse.software.construction.restapp.entity.User;
import org.hse.software.construction.restapp.repository.JsonUserRepository;
import org.hse.software.construction.restapp.service.RestaurantServiceImpl;
import org.hse.software.construction.restapp.util.ConsoleOutput;
import org.hse.software.construction.restapp.util.CookingStatus;
import org.hse.software.construction.restapp.util.Pair;
import org.hse.software.construction.restapp.util.UserStatus;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class RestaurantController {
    private final RestaurantServiceImpl restaurantService;
    private final Scanner scanner;
    private User currentUser;
    private final AuthenticationController authController;
    private JsonUserRepository userRepository;

    public RestaurantController(RestaurantServiceImpl restaurantService, AuthenticationController authController, JsonUserRepository userRepository) {
        this.restaurantService = restaurantService;
        this.scanner = new Scanner(System.in);
        this.authController = authController;
        this.userRepository = userRepository;
    }


    public void start() {
        System.out.println("Добро пожаловать в ресторан!");
        try {
            loginOrRegister();
        } catch (Exception e) {
            System.out.println("Произошла ошибка при аутентификации");
        }
        while (true) {
            if (currentUser != null && currentUser.getStatus().equals(UserStatus.ADMIN)) {
                ConsoleOutput.showAdminMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        try {
                            addDishMenu();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при добавлении блюда в меню");
                            return;
                        }
                    case "2":
                        try {
                            deleteDishMenu();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при удалении блюда из меню");
                            return;
                        }
                    case "3":
                        try {
                            updateDishCount();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при обновлении количества блюда");
                            return;
                        }

                    case "4":
                        try {
                            updateDishCookingTime();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при обновлении времени приготовления блюда");
                            return;
                        }
                    case "5":
                        try {
                            updateDishPrice();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при обновлении цены блюда");
                            return;
                        }

                    case "6":
                        try {
                            checkProfit();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при просмотре выручки");
                            return;
                        }
                    case "7":
                        System.out.println("До свидания!");
                        return;
                    case "8":
                        try {
                            currentUser = null;
                            loginOrRegister();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при аутентификации");
                            return;
                        }
                    case "9":
                        try {
                            getFeedback();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при получении отзывов");
                            return;
                        }
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }

            } else if (currentUser != null && currentUser.getStatus().equals(UserStatus.VISITOR)) {
                ConsoleOutput.showVisitorMenu();
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        try {
                            displayMenu();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при просмотре меню");
                            return;
                        }
                    case "2":
                        try {
                            addDishToCookingOrder();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при добавлении блюда в заказ");
                            return;
                        }
                    case "3":
                        try {
                            cancelOrder();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при отмене заказа");
                            return;
                        }

                    case "4":
                        try {
                            checkOrderStatus();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при просмотре статуса заказа");
                            return;
                        }

                    case "5":
                        try {
                            currentUser = null;
                            loginOrRegister();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при аутентификации");
                            return;
                        }
                    case "6":
                        System.out.println("До свидания!");
                        return;
                    case "7":
                        try {
                            makeOrder();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при создании заказа");
                            return;
                        }
                    case "8":
                        try {
                            payCompletedOrder();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при оплате заказа");
                            return;
                        }
                    case "9":
                        try {
                            makeFeedback();
                            break;
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка при создании отзыва");
                            return;
                        }
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            }
        }

    }


    private void loginOrRegister() {
        boolean loggedIn = false;
        while (!loggedIn) {
            ConsoleOutput.showLoginMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    loggedIn = login();
                    break;
                case "2":
                    register();
                    break;
                case "3":
                    System.out.println("Завершение работы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private void payCompletedOrder() {
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()) == null) {
            System.out.println("Нет заказов");
            return;
        }
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()).getPaymentStatus()) {
            System.out.println("Заказ уже оплачен...");
            return;
        }
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()).getStatus().equals(CookingStatus.COMPLETED)) {
            System.out.println("Введите pay для оплаты заказа или что-то другое для отмены оплаты");
            if (scanner.nextLine().equals("pay")) {
                restaurantService.payOrder(currentUser.getCurrentOrderId());
                return;
            } else {
                System.out.println("Заказ не может быть оплачен");
                return;
            }
        }
        System.out.println("Заказ не может быть оплачен");
    }

    private void getFeedback() {
        System.out.println("Отзывы на заказы:");
        List<Pair<String, Integer>> feedbackList = restaurantService.checkFeedback();
        for (Pair<String, Integer> feedback : feedbackList) {
            System.out.println("Отзыв: " + feedback.getKey());
            System.out.println("Оценка: " + feedback.getValue());
        }
    }

    private void checkProfit() {
        System.out.println("Profit: " + restaurantService.checkProfit());
    }

    private boolean login() {
        System.out.println("Введите статус- 1(Если администратор) 2(Если гость ресторана):");
        String temp = scanner.nextLine();
        UserStatus userStatus;
        if (temp.equals("1")) {
            userStatus = UserStatus.ADMIN;
        } else if (temp.equals("2")) {
            userStatus = UserStatus.VISITOR;
        } else {
            System.out.println("неверна указана роль");
            return false;
        }
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        currentUser = authController.login(username, password, userStatus);
        if (currentUser != null) {
            System.out.println("Вход выполнен успешно, " + currentUser.getName() + "!");
            return true;
        } else {
            System.out.println("Неверное имя пользователя или пароль.");
            return false;
        }
    }

    private void addDishMenu() {
        System.out.println("Введите имя блюда:");
        String dishName = scanner.nextLine().trim();
        System.out.println("Введите цену:");
        BigDecimal price = BigDecimal.ZERO;
        try {
            price = scanner.nextBigDecimal();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }
        System.out.println("Введите время приготовления:");
        int cookingTime = 0;
        try {
            cookingTime = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }

        System.out.println("Введите количество порций:");
        int count = 0;
        try {
            count = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }
        if (price.compareTo(BigDecimal.ZERO) > 0 && cookingTime > 0 && count >= 0) {
            restaurantService.addDishMenu(dishName, price, cookingTime, count);
        } else {
            System.out.println("неверные параметры для блюда");
        }
    }

    private void updateDishPrice() {
        displayAdminMenu();
        String dishName;
        BigDecimal price;
        try {
            System.out.println("Введите имя блюда:");
            dishName = scanner.nextLine().trim();
            System.out.println("Введите новую цену:");
            price = scanner.nextBigDecimal();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            restaurantService.updateDishPrice(dishName, price);
        } else {
            System.out.println("неверные параметры для блюда");
        }
    }

    private void updateDishCookingTime() {
        displayAdminMenu();
        String dishName;
        int cookingTime;
        try {
            System.out.println("Введите имя блюда:");
            dishName = scanner.nextLine().trim();
            System.out.println("Введите новое время приготовления в секундах:");
            cookingTime = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }
        if (cookingTime > 0) {
            restaurantService.updateDishCookingTime(dishName, cookingTime);
        } else {
            System.out.println("неверные параметры для блюда");
        }
    }

    private void updateDishCount() {
        displayAdminMenu();
        System.out.println("Введите имя блюда:");
        String dishName = scanner.nextLine().trim();
        System.out.println("Введите новое количество порций:");
        int count;
        try {
            count = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Неверный формат ввода");
            return;
        }
        if (count >= 0) {
            restaurantService.updateDishCount(dishName, count);
        } else {
            System.out.println("неверные параметры для блюда");
        }
    }

    private void deleteDishMenu() {
        displayAdminMenu();
        System.out.println("Введите имя блюда для удаления:");
        String dishName = scanner.nextLine().trim();
        restaurantService.DeleteDishMenu(dishName);
    }

    private void makeOrder() {
        Order order = new Order();
        restaurantService.saveOrder(order);
        currentUser.setCurrentOrder(order.getId());
        userRepository.updateUser(currentUser);
        inputDishes();
        if (!restaurantService.orderFindById(currentUser.getCurrentOrderId()).getDishes().isEmpty()) {
            System.out.println("Заказ передан на кухню, во время приготовления вы можете добавить блюда");
            restaurantService.updateOrder(restaurantService.orderFindById(currentUser.getCurrentOrderId()));
            restaurantService.processOrder(restaurantService.orderFindById(currentUser.getCurrentOrderId()));
        } else {
            System.out.println("Заказ пуст, попробуйте создать еще раз");
            restaurantService.deleteOrderById(currentUser.getCurrentOrderId());
            currentUser.setCurrentOrder(null);
        }
    }

    public void makeFeedback() {
        Order order = restaurantService.orderFindById(currentUser.getCurrentOrderId());
        if (order == null || !order.getPaymentStatus()) {
            System.out.println("Нельзя оставить отзыв");
            return;
        }
        System.out.println("Введите оценку заказа от 1 до 5:");
        int evaluation;
        try {
            evaluation = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Ошибка: некорректный ввод. Пожалуйста, введите целое число.");
            return;
        }
        if (!(evaluation >= 1 && evaluation <= 5)) {
            System.out.println("Нельзя поставить такую оценку");
            return;
        }
        System.out.println("Введите отзыв:");
        String message = scanner.nextLine();
        restaurantService.makeFeedback(currentUser.getCurrentOrderId(), message, evaluation);
        System.out.println("Вы успешно оставили свой отзыв");
    }

    private void register() {
        System.out.println("Введите имя пользователя:");
        String username = scanner.nextLine();

        System.out.println("Введите пароль:");
        String password = scanner.nextLine();

        authController.register(username, password);
    }

    private void displayMenu() {
        List<Dish> menu = restaurantService.getMenu();
        if (menu.isEmpty()) {
            System.out.println("Меню пусто");
            return;
        }

        System.out.println("Меню ресторана:");
        for (Dish dish : menu) {
            System.out.println(dish);
        }
    }

    private void displayAdminMenu() {
        List<Dish> menu = restaurantService.getMenu();
        if (menu.isEmpty()) {
            System.out.println("Меню пусто");
            return;
        }
        System.out.println("Меню ресторана:");
        for (Dish dish : menu) {
            System.out.println("Имя: " + dish.getName() +
                    ", Количество: " + dish.getCount() +
                    ", Время приготовления: " + dish.getCookingTime() +
                    ", Цена: " + dish.getPrice());
        }
    }

    private void addDishToCookingOrder() {
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()) == null) {
            System.out.println("У вас нет заказов. Создайте, чтобы добавить блюда");
            return;
        }
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()).getStatus().equals(CookingStatus.IN_PROGRESS) || restaurantService.orderFindById(currentUser.getCurrentOrderId()).getStatus().equals(CookingStatus.ACCEPTED)) {
            Pair<String, Integer> dishData = parseDish();
            restaurantService.addDishToCookingOrder(dishData.getKey(), currentUser.getCurrentOrderId(), dishData.getValue());
            restaurantService.updateOrder(restaurantService.orderFindById(currentUser.getCurrentOrderId()));
        } else {
            System.out.println("В заказ уже нельзя добавлять блюда");
        }

    }

    private Pair<String, Integer> parseDish() {
        boolean flag = true;
        Pair<String, Integer> pair = new Pair<>();

        System.out.println("Выберите одно блюдо для добавления.Введите в формате: name count");
        while (flag) {
            displayMenu();
            String input = scanner.nextLine();
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Некорректный формат ввода. Попробуйте снова.");
            }
            String dishName = parts[0];
            int res = 0;
            try {
                res = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Количество должно быть числом.не удалось найти блюдо");
            }
            flag = false;
            pair.setKey(dishName);
            pair.setValue(res);
        }
        return pair;
    }

    private void inputDishes() {
        displayMenu();
        System.out.println("Введите имя блюда и количество в формате: name count .Или введите stop для прекращения ввода блюд:");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("stop")) {
                break;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Некорректный формат ввода. Попробуйте снова.");
                continue;
            }

            String dishName = parts[0];
            int count;
            try {
                count = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Количество должно быть числом. Попробуйте снова.");
                continue;
            }
            if (restaurantService.addDishToOrder(dishName, currentUser.getCurrentOrderId(), count)) {
                System.out.println("Добавлено в заказ: " + dishName + " в количестве " + count);
            }
        }
    }

    private void cancelOrder() {
        System.out.println("Сейчас мы попробуем отменить текущий ваш заказ");
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()) == null) {
            System.out.println("Нет текущих заказов");
        }
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()).getStatus().equals(CookingStatus.IN_PROGRESS)) {
            restaurantService.cancelOrder(currentUser.getCurrentOrderId());
        } else {
            System.out.println("Заказ уже нельзя отменить!");
        }
    }

    private void checkOrderStatus() {
        if (restaurantService.orderFindById(currentUser.getCurrentOrderId()) == null) {
            System.out.println("Нет действующего заказ!");
            return;
        }
        System.out.println("Статус текущего заказа " + restaurantService.orderFindById(currentUser.getCurrentOrderId()).getStatus());
    }
}
