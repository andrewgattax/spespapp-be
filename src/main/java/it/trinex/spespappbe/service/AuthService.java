package it.trinex.spespappbe.service;

import it.trinex.spespappbe.dto.request.auth.CompleteLoginRequest;
import it.trinex.spespappbe.dto.response.auth.CompleteLoginResponse;
import it.trinex.spespappbe.exception.RecordNotFoundException;
import it.trinex.spespappbe.exception.UnauthorizedException;
import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.model.SpespappUser;
import it.trinex.spespappbe.repo.UserRepo;
import it.trinex.spespappbe.security.JwtService;
import it.trinex.spespappbe.security.JwtUserPrincipal;
import it.trinex.spespappbe.security.rsa.RsaAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final RedisService redisService;

    private final String REDIS_CHALLENGE_KEY_PREFIX = "auth_challenge:";
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthChallenge initLogin(String username) {
        SpespappUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("Utente non trovato con username: " + username));

        AuthChallenge challenge = new AuthChallenge(username);

        redisService.saveChallenge(challenge);

        return challenge;
    }

    public CompleteLoginResponse completeLogin(CompleteLoginRequest request) {
        Authentication authentication;
        try {
             authentication = authenticationManager.authenticate(
                    new RsaAuthenticationToken(UUID.fromString(request.getChallengeId()), request.getSignatureBase64())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Autenticazione fallita");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        if(principal == null) {
            throw new UnauthorizedException("Autenticazione fallita, utente non trovato");
        }
        String authToken = jwtService.generaToken(Map.of(), principal);

        return CompleteLoginResponse.builder()
                .authToken(authToken)
                .build();
    }
}
