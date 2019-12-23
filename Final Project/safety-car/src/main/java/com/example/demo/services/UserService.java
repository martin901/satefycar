package com.example.demo.services;

import com.example.demo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User create(User user, boolean encryptPassword);
    User getById(Long id);
    User getByUsername(String username);
    User getByEmail(String email);
    Page<User> getAll(Pageable pageable);
    User updateById(Long id, User updatedUser);
    User getCurrentUser();
}
