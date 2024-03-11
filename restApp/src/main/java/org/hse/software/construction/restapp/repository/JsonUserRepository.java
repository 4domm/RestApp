package org.hse.software.construction.restapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.List;


import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import com.fasterxml.jackson.core.type.TypeReference;
import org.hse.software.construction.restapp.entity.User;

public class JsonUserRepository implements UserRepository {
    private static JsonUserRepository instance;
    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUserRepository(File file) {
        this.file = file;
        createFileIfNotExists();
    }

    public static JsonUserRepository getInstance(File file) {
        if (instance == null) {
            instance = new JsonUserRepository(file);
        }
        return instance;
    }

    @Override
    public List<User> getAll() {
        try {
            return objectMapper.readValue(file, new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public User saveUser(User user) {
        List<User> users = getAll();
        users.add(user);
        saveAll(users);
        return user;
    }

    @Override
    public User updateUser(User user) {
        deleteByName(user.getName());
        return saveUser(user);
    }

    @Override
    public User findByName(String name) {
        List<User> users = getAll();
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteByName(String name) {
        List<User> users = getAll();
        List<User> updatedUsers = new ArrayList<>();
        for (User user : users) {
            if (!user.getName().equals(name)) {
                updatedUsers.add(user);
            }
        }
        saveAll(updatedUsers);
    }

    private void createFileIfNotExists() {
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    objectMapper.writeValue(file, new ArrayList<User>());
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating file: " + e.getMessage());
        }
    }

    @Override
    public void saveAll(List<User> users) {
        try {
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            System.err.println("An error occurred while saving dishes: " + e.getMessage());
        }
    }
}
