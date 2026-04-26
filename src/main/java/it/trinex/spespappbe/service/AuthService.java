package it.trinex.spespappbe.service;

import it.trinex.spespappbe.dto.request.auth.CompleteLoginRequest;
import it.trinex.spespappbe.dto.request.auth.UpdatePublicKeyRequest;
import it.trinex.spespappbe.dto.response.auth.CompleteLoginResponse;
import it.trinex.spespappbe.exception.RecordNotFoundException;
import it.trinex.spespappbe.exception.UnauthorizedException;
import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.model.SpespappUser;
import it.trinex.spespappbe.model.UserPublicKey;
import it.trinex.spespappbe.repo.PublicKeyRepo;
import it.trinex.spespappbe.repo.UserRepo;
import it.trinex.spespappbe.security.JwtService;
import it.trinex.spespappbe.security.JwtUserPrincipal;
import it.trinex.spespappbe.security.rsa.RsaAuthenticationToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.money.CurrencyValidatorForMonetaryAmount;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepo userRepo;
    private final RedisService redisService;

    private final String REDIS_CHALLENGE_KEY_PREFIX = "auth_challenge:";
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;
    private final PublicKeyRepo publicKeyRepo;

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
                    new RsaAuthenticationToken(UUID.fromString(request.getChallengeId()), request.getSignatureBase64(), request.getDeviceId())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Autenticazione fallita");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        JwtUserPrincipal principal = (JwtUserPrincipal) authentication.getPrincipal();
        if(principal == null) {
            throw new UnauthorizedException("Autenticazione fallita, utente non trovato");
        }
        String authToken = jwtService.generaToken(Map.of("uid", principal.getId()), principal);

        log.info("Loggato utente: " + principal.getUsername());

        return CompleteLoginResponse.builder()
                .authToken(authToken)
                .build();
    }

    public void updateKeys(UpdatePublicKeyRequest request) {

        SpespappUser user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Errore nel caricamento dell'utente"));

        UserPublicKey currentKey = user.getPublicKeys().stream()
                .filter(k -> k.getDeviceId().equals(request.getDeviceId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Chiave pubblica non trovata per il device ID: " + request.getDeviceId()));

        currentKey.setPublicKeyBase64(request.getPublicKeyBase64());

        publicKeyRepo.save(currentKey);

    }

    public void updateDeviceId(String previousDeviceId, String deviceId) {

        SpespappUser user = currentUserService.getCurrentUser()
                .orElseThrow(() -> new UnauthorizedException("Errore nel caricamento dell'utente"));

        UserPublicKey key = user.getPublicKeys().stream()
                .filter(k -> k.getDeviceId().equals(previousDeviceId))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("Chiave pubblica non trovata per il device ID: " + previousDeviceId));

        key.setDeviceId(deviceId);

        publicKeyRepo.save(key);
    }
}
