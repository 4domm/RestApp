package org.hse.software.construction.restapp.service;

import org.hse.software.construction.restapp.entity.Dish;
import org.hse.software.construction.restapp.repository.DishRepository;

import java.util.List;
import java.util.UUID;

public class DishServiceImpl implements DishService {
    private DishRepository dishRepository;

    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public List<Dish> getAll() {
        return dishRepository.getAll();
    }

    @Override
    public Dish saveDish(Dish dish) {
        return dishRepository.saveDish(dish);
    }

    @Override
    public Dish findByName(String name) {
        return dishRepository.findByName(name);
    }

    @Override
    public Dish updateDish(Dish dish) {
        dishRepository.deleteByName(dish.getName());
        return dishRepository.saveDish(dish);
    }

    @Override
    public boolean reserveDish(Dish dish, Integer count) {
        if (dish.getCount() >= count) {
            dish.setCount(dish.getCount() - count);
            return true;
        }
        return false;
    }

    @Override
    public void deleteDish(String name) {
        dishRepository.deleteByName(name);
    }

    @Override
    public Dish findById(UUID dishId) {
        return dishRepository.findById(dishId);
    }


}
