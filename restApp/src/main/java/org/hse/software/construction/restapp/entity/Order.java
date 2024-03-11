package org.hse.software.construction.restapp.entity;

import org.hse.software.construction.restapp.util.CookingStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Order {
    private UUID id = UUID.randomUUID();
    private BigDecimal cost = BigDecimal.ZERO;
    private CookingStatus status = CookingStatus.ACCEPTED;

    private boolean paymentStatus = false; //true-payed / false-no
    private Map<UUID, Integer> dishes = new HashMap<>();
    String feedback;
    int evaluation;

    public Order() {

    }


    public UUID getId() {
        return id;
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public CookingStatus getStatus() {
        return status;
    }

    public void setStatus(CookingStatus status) {
        this.status = status;
    }


    public Map<UUID, Integer> getDishes() {
        return dishes;
    }

    public void setDishes(Map<UUID, Integer> dishes) {
        this.dishes = dishes;
    }

    public void addDish(UUID dishId, int count, BigDecimal price) {

        if (dishes.containsKey(dishId)) {
            dishes.put(dishId, dishes.get(dishId) + count);
        } else {
            dishes.put(dishId, count);
        }
        cost = cost.add(BigDecimal.valueOf(count).multiply(price));

    }

    public void removeDish(UUID dishId, BigDecimal price) {
        if (dishes.containsKey(dishId)) {
            dishes.remove(dishId);
            cost = cost.subtract(price.multiply(BigDecimal.valueOf(dishes.get(dishId))));
        }
    }
}
