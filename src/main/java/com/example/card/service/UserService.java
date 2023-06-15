package com.example.card.service;

import com.example.card.domain.User;

public interface UserService {
    User getUserByEmail(String email);

    void createUser(User user);
}
