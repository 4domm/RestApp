package org.hse.software.construction.restapp.util;

import org.hse.software.construction.restapp.controller.AuthenticationController;
import org.hse.software.construction.restapp.controller.RestaurantController;
import org.hse.software.construction.restapp.repository.JsonDishRepository;
import org.hse.software.construction.restapp.repository.JsonOrderRepository;
import org.hse.software.construction.restapp.repository.JsonUserRepository;
import org.hse.software.construction.restapp.service.DishServiceImpl;
import org.hse.software.construction.restapp.service.OrderServiceImpl;
import org.hse.software.construction.restapp.service.RestaurantServiceImpl;

import java.io.File;

public class ApplicationInitializer {
    public static void startApplication(File file1, File file2, File file3) {
        JsonDishRepository dishRepository = JsonDishRepository.getInstance(file1);
        JsonOrderRepository orderRepository = JsonOrderRepository.getInstance(file2);
        JsonUserRepository userRepository = JsonUserRepository.getInstance(file3);
        DishServiceImpl dishService = new DishServiceImpl(dishRepository);
        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository);
        RestaurantServiceImpl restService = new RestaurantServiceImpl(dishService, orderService);
        AuthenticationController authController = new AuthenticationController(userRepository);
        RestaurantController controller = new RestaurantController(restService, authController, userRepository);
        controller.start();
    }
}
