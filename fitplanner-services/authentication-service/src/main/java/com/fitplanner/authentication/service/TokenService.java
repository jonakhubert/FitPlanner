package com.fitplanner.authentication.service;

import com.fitplanner.authentication.exception.model.TokenNotFoundException;
import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.ResetPasswordToken;
import com.fitplanner.authentication.model.VerificationToken;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import com.fitplanner.authentication.repository.ResetPasswordTokenRepository;
import com.fitplanner.authentication.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    public TokenService(
        AccessTokenRepository accessTokenRepository,
        VerificationTokenRepository verificationTokenRepository,
        ResetPasswordTokenRepository resetPasswordTokenRepository
    ) {
        this.accessTokenRepository = accessTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    // Access token

    public void createAccessToken(String jwt, String email) {
        var token = new AccessToken(jwt, email);

        accessTokenRepository.save(token);
    }

    public boolean isAccessTokenValid(String token) {
        return accessTokenRepository.findByToken(token).isPresent();
    }

    public void deleteAccessToken(String email) {
        accessTokenRepository.findByUserEmail(email).ifPresent(accessTokenRepository::delete);
    }

    // Verification token

    public VerificationToken createVerificationToken(String email) {
        var token = UUID.randomUUID().toString();
        var verificationToken = new VerificationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            email
        );

        return verificationTokenRepository.save(verificationToken);
    }

    public void saveVerificationToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken findVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Token not found."));
    }

    public void deleteVerificationToken(String email) {
        verificationTokenRepository.findByUserEmail(email).ifPresent(verificationTokenRepository::delete);
    }

    // Reset password token

    public ResetPasswordToken createResetPasswordToken(String email) {
        var token = UUID.randomUUID().toString();
        var resetPasswordToken = new ResetPasswordToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            email
        );

        return resetPasswordTokenRepository.save(resetPasswordToken);
    }

    public ResetPasswordToken findResetPasswordToken(String token) {
        return resetPasswordTokenRepository.findByToken(token)
            .orElseThrow(() -> new TokenNotFoundException("Token not found."));
    }

    public boolean isResetPasswordTokenValid(String token) {
        var val = resetPasswordTokenRepository.findByToken(token);

        return val.isPresent() && !val.get().getExpiredAt().isBefore(LocalDateTime.now());
    }

    public void deleteResetPasswordToken(String email) {
        resetPasswordTokenRepository.findByUserEmail(email).ifPresent(resetPasswordTokenRepository::delete);
    }
}
