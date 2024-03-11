package org.hse.software.construction.restapp.repository;

import org.hse.software.construction.restapp.entity.Dish;

import java.util.List;
import java.util.UUID;

public interface DishRepository {
    public List<Dish> getAll();

    public Dish saveDish(Dish dish);

    public Dish findByName(String name);

    public Dish findById(UUID id);

    void deleteByName(String name);

    void saveAll(List<Dish> dishes);

}

