package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Dish;
import org.hse.software.construction.restapp.entity.Order;
import org.hse.software.construction.restapp.util.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RestaurantService {

    List<Dish> getMenu();

    Order updateOrder(Order order);

    Order orderFindById(UUID id);

    List<Pair<String, Integer>> checkFeedback();

    boolean addDishToOrder(String dishName, UUID orderId, Integer count);

    void makeFeedback(UUID orderId, String message, int evaluation);

    BigDecimal checkProfit();

    void addDishToCookingOrder(String dishName, UUID orderId, Integer count);

    void deleteOrderById(UUID id);

    void processOrder(Order order);

    Order saveOrder(Order order);

    void updateDishPrice(String dishName, BigDecimal price);

    void updateDishCookingTime(String dishName, Integer cookingTime);

    void updateDishCount(String dishName, Integer count);

    void addDishMenu(String dishName, BigDecimal price, Integer cookingTime, Integer count);

    void DeleteDishMenu(String dishName);

    void cancelOrder(UUID orderId);

    void payOrder(UUID orderId);

}
