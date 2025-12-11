package com.example.demo.service;

import com.example.demo.model.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User findById(Long id);
    List<User> findAll();
    User update(Long id, User user);
    void blockUser(Long id);
    void unblockUser(Long id);
}
