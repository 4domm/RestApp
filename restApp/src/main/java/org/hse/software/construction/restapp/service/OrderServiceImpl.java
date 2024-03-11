package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Order;
import org.hse.software.construction.restapp.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

public class   OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.getAll();
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.saveOrder(order);
    }

    @Override
    public Order updateOrder(Order order) {
        orderRepository.deleteById(order.getId());
        return orderRepository.saveOrder(order);
    }
    @Override
    public void deleteOrderById(UUID id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order findById(UUID id) {
        return orderRepository.findById(id);
    }
}
