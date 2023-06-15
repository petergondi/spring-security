package com.example.card.controller;

import com.example.card.domain.Card;
import com.example.card.service.CardService;
import com.example.card.util.CardStatus;
import com.example.card.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    @Autowired
    CardService cardService;
    @GetMapping()
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public List<Card> getCardsByUser(@RequestHeader(name = "Authorization") String token) {
        String email = getEmailFromToken(token);
        validateToken(email, token);
        return cardService.getCardsByUser(email);
    }

    @GetMapping("/{cardId}")
    @PreAuthorize("@cardService.hasAccessToCard(#cardId, #email)")
    public Card getCardByIdAndUser(@PathVariable Long cardId, @RequestParam String email, @RequestHeader(name = "Authorization") String token) {
        validateToken(email, token);
        return cardService.getCardByIdAndUser(cardId, email);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public Card createCard(@RequestHeader(name = "Authorization") String token, @RequestBody Card card) {
        if (StringUtils.isEmpty(card.getName())) {
            throw new IllegalArgumentException("Card name is required");
        }

        if (card.getColor() != null && !isValidColorFormat(card.getColor())) {
            throw new IllegalArgumentException("Invalid color format");
        }

        // Set default values if not provided
        if (StringUtils.isEmpty(card.getStatus())) {
            card.setStatus(CardStatus.valueOf("TODO"));
        }
        String email = getEmailFromToken(token);
        validateToken(email, token);
        return cardService.createCard(email, card);
    }
    private boolean isValidColorFormat(String color) {
        // Validate the color format (e.g., "#FFFFFF")
        String colorRegex = "^#[a-fA-F0-9]{6}$";
        return color.matches(colorRegex);
    }
    private String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey("568f6c00-a8ed-4332-b3ee-3c46db661506").parseClaimsJws(token).getBody();
            return claims.get("email", String.class);
        } catch (Exception e) {
                return null;
        }
    }

    @PutMapping("/{cardId}")
    @PreAuthorize("@cardService.hasAccessToCard(#cardId, #email)")
    public Card updateCard(@PathVariable Long cardId, @RequestParam String email, @RequestHeader(name = "Authorization") String token, @RequestBody Card updatedCard) {
        validateToken(email, token);
        return cardService.updateCard(cardId, email, updatedCard);
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("@cardService.hasAccessToCard(#cardId, #email)")
    public void deleteCard(@PathVariable Long cardId, @RequestParam String email, @RequestHeader(name = "Authorization") String token) {
        validateToken(email, token);
        cardService.deleteCard(cardId, email);
    }

    private void validateToken(String email, String token) {
        if (StringUtils.isEmpty(token)) {
            throw new RuntimeException("Authorization token is missing");
        }

        try {
            Claims claims = JwtUtil.validateToken(token);
            if (!email.equals(claims.get("email", String.class))) {
                throw new RuntimeException("Invalid token for the specified email");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }
}
