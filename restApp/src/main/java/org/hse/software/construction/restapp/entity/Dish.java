package org.hse.software.construction.restapp.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Dish {
    private UUID id = UUID.randomUUID();
    ;
    private String name;
    private BigDecimal price;
    private Integer cookingTime;
    private Integer count;

    public UUID getId() {
        return id;
    }

    public Dish() {
    }

    public Dish(String name, BigDecimal price, Integer cookingTime, Integer count) {
        this.name = name;
        this.price = price;
        this.cookingTime = cookingTime;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Название блюда-'" + name + '\'' +
                ", Цена-" + price
                ;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }
}
