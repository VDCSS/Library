package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User create(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        User u = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        u.setName(user.getName());
        u.setEmail(user.getEmail());
        // do not update password here unless API for that
        return userRepo.save(u);
    }

    @Override
    @Transactional
    public void blockUser(Long id) {
        User u = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        u.setBlocked(true);
        userRepo.save(u);
    }

    @Override
    @Transactional
    public void unblockUser(Long id) {
        User u = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        u.setBlocked(false);
        userRepo.save(u);
    }
}
