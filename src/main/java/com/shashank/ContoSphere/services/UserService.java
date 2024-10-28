package com.shashank.ContoSphere.services;



import com.shashank.ContoSphere.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User getUserByEmail(String email);
    Optional<User> getUserById(int id);
    Optional<User> updateUser(User user);
    void deleteUser(int id);
    boolean isUserExist(int id);
    boolean isUserExistByEmail(String email);
    List<User> getAllUsers();

    // Additional methods for handling OTP authentication
    String getOtp(String email);
    void enableUser(String email);
}
