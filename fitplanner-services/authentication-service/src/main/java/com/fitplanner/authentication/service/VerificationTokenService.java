package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.verificationtoken.VerificationToken;
import com.fitplanner.authentication.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void saveToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken)
            .orElseThrow(() -> new TokenNotFoundException("Token not found " + verificationToken));
    }

    public void setConfirmedAt(String verificationToken) {
        var token = verificationTokenRepository.findByToken(verificationToken)
            .orElseThrow(() -> new TokenNotFoundException("Token not found: " + verificationToken));

        token.setConfirmedAt(LocalDateTime.now());
        verificationTokenRepository.save(token);
    }

    public void deleteToken(String userEmail) {
        verificationTokenRepository.findByUserEmail(userEmail).ifPresent(verificationTokenRepository::delete);
    }
}
