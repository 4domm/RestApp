package org.hse.software.construction.restapp.controller;


import org.hse.software.construction.restapp.repository.JsonUserRepository;
import org.hse.software.construction.restapp.entity.User;
import org.hse.software.construction.restapp.util.UserStatus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationController {
    JsonUserRepository userRepository;

    public AuthenticationController(JsonUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password, UserStatus userStatus) {
        User user = userRepository.findByName(username);
        if (user != null && verifyPassword(password, user.getPassword()) && user.getStatus().equals(userStatus)) {
            return user;
        }
        return null;
    }

    public void register(String username, String password) {
        if (userRepository.findByName(username) == null) {
            String hashedPassword = hashPassword(password);
            User newUser = new User(username, hashedPassword, UserStatus.VISITOR);
            userRepository.saveUser(newUser);
            System.out.println("Пользователь успешно зарегистрирован.");
        } else {
            System.out.println("Пользователь с таким именем уже существует.");
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password;
        }
    }

    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        String hashedEnteredPassword = hashPassword(plainPassword);
        return hashedEnteredPassword.equals(hashedPassword);
    }
}

