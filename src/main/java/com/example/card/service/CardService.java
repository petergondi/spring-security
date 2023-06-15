package com.example.card.service;

import com.example.card.domain.Card;

import java.util.List;

public interface CardService {
    List<Card> getCardsByUser(String email);

    Card getCardByIdAndUser(Long cardId, String email);

    Card createCard(String email, Card card);

    Card updateCard(Long cardId, String email, Card updatedCard);

    void deleteCard(Long cardId, String email);
}
