package org.hse.software.construction.restapp.repository;

import org.hse.software.construction.restapp.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User saveUser(User User);


    User findByName(String name);

    void deleteByName(String name);

    void saveAll(List<User> users);

    User updateUser(User user);
}
