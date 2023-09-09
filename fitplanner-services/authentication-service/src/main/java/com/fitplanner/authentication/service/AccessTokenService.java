package com.fitplanner.authentication.service;

import com.fitplanner.authentication.model.accesstoken.AccessToken;
import com.fitplanner.authentication.model.user.User;
import com.fitplanner.authentication.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;

    @Autowired
    public AccessTokenService(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    public boolean isTokenValid(String token) {
        return accessTokenRepository.findByToken(token).isPresent();
    }

    public void saveToken(AccessToken accessToken) {
        accessTokenRepository.save(accessToken);
    }

    public void deleteToken(User user) {
        accessTokenRepository.findByUserEmail(user.getUsername()).ifPresent(accessTokenRepository::delete);
    }
}
