package com.example.card.domain;

import lombok.Builder;

@Builder
public class UserResponse {
    private String email;
    private String role;
    private String token;
}
