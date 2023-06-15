package com.example.card.service;

import com.example.card.domain.User;
import com.example.card.repo.UserRepository;
import com.example.card.util.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public void createUser(User user) {
        // Validate if user with the given email already exists
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already exists");
//        }

        // Create a new User object
        User user1 = new User();
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());
        user1.setRole(UserRole.valueOf("ADMIN"));
        // Save the user in the database
        userRepository.save(user1);
    }
}
