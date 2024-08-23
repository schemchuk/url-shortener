package de.telran.urlshortener.service;

import de.telran.urlshortener.entity.User;

public interface UserService {
    User createUser(String userName, String email, String password);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
}







