package org.hse.software.construction.restapp.repository;

import java.util.List;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hse.software.construction.restapp.entity.Order;

public class JsonOrderRepository implements OrderRepository {
    private static JsonOrderRepository instance;
    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonOrderRepository(File file) {
        this.file = file;
        createFileIfNotExists();
    }

    public static JsonOrderRepository getInstance(File file) {
        if (instance == null) {
            instance = new JsonOrderRepository(file);
        }
        return instance;
    }

    @Override
    public List<Order> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<Order>>() {
            });
        } catch (IOException e) {
            System.err.println("An error occurred while reading orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Order saveOrder(Order order) {
        List<Order> orders = getAll();
        orders.add(order);
        saveAll(orders);
        return order;
    }

    @Override
    public void saveAll(List<Order> orders) {
        try {
            objectMapper.writeValue(file, orders);
        } catch (IOException e) {
            System.err.println("An error occurred while saving orders: " + e.getMessage());
        }
    }

    @Override
    public Order findById(UUID id) {
        List<Order> orders = getAll();
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        List<Order> orders = getAll();
        List<Order> updatedOrders = new ArrayList<>();
        for (Order order : orders) {
            if (!order.getId().equals(id)) {
                updatedOrders.add(order);
            }
        }
        saveAll(updatedOrders);
    }

    private void createFileIfNotExists() {
        try {
            if (file.createNewFile()) {
                objectMapper.writeValue(file, new ArrayList<Order>());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating or writing to file: " + e.getMessage());
        }
    }
}
