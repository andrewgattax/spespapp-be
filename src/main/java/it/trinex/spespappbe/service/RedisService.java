package it.trinex.spespappbe.service;

import it.trinex.spespappbe.model.AuthChallenge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private final RedisTemplate<String, AuthChallenge> challengeRedisTemplate;

    private static final String CHALLENGE_KEY_PREFIX = "auth:challenge:";

    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void put(String key, String value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // AuthChallenge-specific methods

    public void saveChallenge(AuthChallenge challenge) {
        String key = CHALLENGE_KEY_PREFIX + challenge.getId();

        // Calculate TTL based on expiresAt
        long ttlSeconds = java.time.Duration.between(
                java.time.Instant.now(),
                challenge.getExpiresAt()
        ).getSeconds();

        if (ttlSeconds > 0) {
            challengeRedisTemplate.opsForValue().set(key, challenge, ttlSeconds, TimeUnit.SECONDS);
        } else {
            challengeRedisTemplate.opsForValue().set(key, challenge);
        }
    }

    public AuthChallenge getChallenge(java.util.UUID challengeId) {
        String key = CHALLENGE_KEY_PREFIX + challengeId;
        return challengeRedisTemplate.opsForValue().get(key);
    }

    public boolean deleteChallenge(java.util.UUID challengeId) {
        String key = CHALLENGE_KEY_PREFIX + challengeId;
        return Boolean.TRUE.equals(challengeRedisTemplate.delete(key));
    }

    public boolean hasChallenge(java.util.UUID challengeId) {
        String key = CHALLENGE_KEY_PREFIX + challengeId;
        return Boolean.TRUE.equals(challengeRedisTemplate.hasKey(key));
    }
}
