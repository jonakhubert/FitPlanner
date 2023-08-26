package com.fitplanner.authentication.token;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepository implements ITokenRepository {

    private final MongoTemplate mongoTemplate;

    public TokenRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Token> findAllValidTokensByUser(String email) {
        Query query = new Query(Criteria.where("userEmail").is(email)
            .andOperator(
                Criteria.where("expired").is(false),
                Criteria.where("revoked").is(false)
            )
        );

        return mongoTemplate.find(query, Token.class);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        Token foundToken = mongoTemplate.findOne(query, Token.class);
        return Optional.ofNullable(foundToken);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        tokens.forEach(mongoTemplate::save);
    }

    @Override
    public void save(Token token) {
        mongoTemplate.save(token);
    }
}
