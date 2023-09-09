package com.fitplanner.authentication.service;

import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import com.fitplanner.authentication.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository, MongoTemplate mongoTemplate) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void saveToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken)
            .orElse(null);
    }

    public void setConfirmedAt(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken)
                .orElseThrow(() -> new IllegalStateException("Token not found: " + confirmationToken));

        token.setConfirmedAt(LocalDateTime.now());
        mongoTemplate.save(token);
    }
}
