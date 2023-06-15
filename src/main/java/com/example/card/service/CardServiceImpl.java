package com.example.card.service;

import com.example.card.domain.Card;
import com.example.card.domain.User;
import com.example.card.repo.CardRepository;
import com.example.card.repo.UserRepository;
import com.example.card.util.CardStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<Card> getCardsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cardRepository.findByUser(user);
    }
    @Override
    public Card getCardByIdAndUser(Long cardId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Card> cardOptional = cardRepository.findByIdAndUser(cardId, user);
        return cardOptional.orElseThrow(() -> new RuntimeException("Card not found"));
    }
    @Override
    public Card createCard(String email, Card card) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        card.setUser(user);
        card.setStatus(CardStatus.TODO);
        return cardRepository.save(card);
    }
    @Override
    public Card updateCard(Long cardId, String email, Card updatedCard) {
        Card card = getCardByIdAndUser(cardId, email);
        card.setName(updatedCard.getName());
        card.setDescription(updatedCard.getDescription());
        card.setColor(updatedCard.getColor());
        card.setStatus(updatedCard.getStatus());
        return cardRepository.save(card);
    }
    @Override
    public void deleteCard(Long cardId, String email) {
        Card card = getCardByIdAndUser(cardId, email);
        cardRepository.delete(card);
    }
}
