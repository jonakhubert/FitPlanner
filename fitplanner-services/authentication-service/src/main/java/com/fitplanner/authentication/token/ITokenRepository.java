package com.fitplanner.authentication.token;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository {
    List<Token> findAllValidTokensByUser(String email);
    Optional<Token> findByToken(String token);
    void saveAll(List<Token> tokens);
    void save(Token token);
}