package it.trinex.spespappbe.service;

import it.trinex.spespappbe.exception.RecordNotFoundException;
import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.model.SpespappUser;
import it.trinex.spespappbe.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final RedisService redisService;

    private final String REDIS_CHALLENGE_KEY_PREFIX = "auth_challenge:";

    public AuthChallenge initLogin(String username) {
        SpespappUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("Utente non trovato con username: " + username));

        AuthChallenge challenge = new AuthChallenge(username);

        redisService.put(REDIS_CHALLENGE_KEY_PREFIX + challenge.getId(), challenge.getNonceBase64(), 30);

        return challenge;
    }
}
