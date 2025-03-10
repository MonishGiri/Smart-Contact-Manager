package com.scm.services;

import com.scm.entities.User;

import java.util.List;
import java.util.Optional;
public interface UserService {
    User savUser(User user);

    Optional<User> getUserById(String id);

    Optional<User> updateUser(User user);
    
    void deleteUser(String id);
    
    boolean isUserExist(String userId);
    
    boolean isUserExistByEmail(String email);
    
    List<User> getAllUsers();

    User getUserByEmail(String email);

    Optional<User> findByEmailToken(String token);
}
