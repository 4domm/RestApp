package org.hse.software.construction.restapp.entity;

import org.hse.software.construction.restapp.util.UserStatus;

import java.util.UUID;

public class User {
    private String name;
    private String password;
    private UserStatus status;
    private UUID currentOrderId;

    public User(String name, String password, UserStatus status) {
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public UUID getCurrentOrderId() {
        return currentOrderId;
    }

    public void setCurrentOrder(UUID currentOrderId) {
        this.currentOrderId = currentOrderId;
    }


    public UserStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
