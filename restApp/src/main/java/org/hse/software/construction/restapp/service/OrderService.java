package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAll();

    Order saveOrder(Order order);


    Order updateOrder(Order order);

    void deleteOrderById(UUID id);

    Order findById(UUID dishId);
}
