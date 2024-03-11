package org.hse.software.construction.restapp.repository;


import java.util.List;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hse.software.construction.restapp.entity.Dish;

public class JsonDishRepository implements DishRepository {
    private static JsonDishRepository instance;
    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonDishRepository(File file) {
        this.file = file;
        createFileIfNotExists();
    }

    public static JsonDishRepository getInstance(File file) {
        if (instance == null) {
            instance = new JsonDishRepository(file);
        }
        return instance;
    }

    @Override
    public List<Dish> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<Dish>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Dish saveDish(Dish dish) {
        List<Dish> dishes = getAll();
        dishes.add(dish);
        saveAll(dishes);
        return dish;
    }

    @Override
    public Dish findByName(String name) {
        List<Dish> dishes = getAll();
        for (Dish dish : dishes) {
            if (dish.getName().equals(name)) {
                return dish;
            }
        }
        return null;
    }

    @Override
    public Dish findById(UUID id) {
        List<Dish> dishes = getAll();
        for (Dish dish : dishes) {
            if (dish.getId().equals(id)) {
                return dish;
            }
        }
        return null;
    }

    @Override
    public void saveAll(List<Dish> dishes) {
        try {
            objectMapper.writeValue(file, dishes);
        } catch (IOException e) {
            System.err.println("An error occurred while saving dishes: " + e.getMessage());
        }
    }

    public void deleteByName(String name) {
        List<Dish> dishes = getAll();
        List<Dish> updatedDishes = new ArrayList<>();
        for (Dish dish : dishes) {
            if (!dish.getName().equals(name)) {
                updatedDishes.add(dish);
            }
        }
        saveAll(updatedDishes);
    }

    private void createFileIfNotExists() {
        try {
            if (file.createNewFile()) {
                objectMapper.writeValue(file, new ArrayList<Dish>());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating or writing to file: " + e.getMessage());
        }
    }
}


