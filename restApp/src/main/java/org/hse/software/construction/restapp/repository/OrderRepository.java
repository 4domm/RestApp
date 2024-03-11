package org.hse.software.construction.restapp.repository;

import org.hse.software.construction.restapp.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    List<Order> getAll();

    Order saveOrder(Order order);
    void saveAll(List<Order> orders);

    Order findById(UUID id);

    void deleteById(UUID id);
}
