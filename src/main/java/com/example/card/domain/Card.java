package com.example.card.domain;

import com.example.card.util.CardStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    private String description;
    private String color;
    @Enumerated(EnumType.STRING)
    private CardStatus status;
}
