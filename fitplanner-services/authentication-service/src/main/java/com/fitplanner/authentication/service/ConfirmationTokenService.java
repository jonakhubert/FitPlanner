package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.confirmationtoken.ConfirmationToken;
import com.fitplanner.authentication.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public ConfirmationToken getToken(String confirmationToken) {
        return confirmationTokenRepository.findByToken(confirmationToken)
            .orElseThrow(() -> new TokenNotFoundException("Token not found " + confirmationToken));
    }

    public void setConfirmedAt(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken)
            .orElseThrow(() -> new TokenNotFoundException("Token not found: " + confirmationToken));

        token.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(token);
    }

    public void deleteToken(String userEmail) {
        confirmationTokenRepository.findByUserEmail(userEmail).ifPresent(confirmationTokenRepository::delete);
    }
}
