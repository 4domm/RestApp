package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Dish;

import java.util.List;
import java.util.UUID;

public interface DishService {
    List<Dish> getAll();

    Dish saveDish(Dish dish);

    Dish findByName(String name);

    Dish updateDish(Dish dish);

    void deleteDish(String name);

    boolean reserveDish(Dish dish, Integer count);

    Dish findById(UUID dishId);

}
