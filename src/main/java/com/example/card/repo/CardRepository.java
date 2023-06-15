package com.example.card.repo;

import com.example.card.domain.Card;
import com.example.card.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByUser(User user);
    Optional<Card> findByIdAndUser(Long cardId, User user);
}
