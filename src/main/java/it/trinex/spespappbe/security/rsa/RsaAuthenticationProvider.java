package it.trinex.spespappbe.security.rsa;

import it.trinex.spespappbe.model.AuthChallenge;
import it.trinex.spespappbe.model.SpespappUser;
import it.trinex.spespappbe.model.UserPublicKey;
import it.trinex.spespappbe.repo.UserRepo;
import it.trinex.spespappbe.security.JwtUserPrincipal;
import it.trinex.spespappbe.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Slf4j
public class RsaAuthenticationProvider implements AuthenticationProvider {

    private final RedisService redisService;
    private final UserRepo userRepo;

    public RsaAuthenticationProvider(RedisService redisService, UserRepo userRepo) {
        this.redisService = redisService;
        this.userRepo = userRepo;
    }

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        RsaAuthenticationToken authenticationToken = (RsaAuthenticationToken) authentication;

        AuthChallenge challenge = redisService.getChallenge(authenticationToken.getPrincipal());

        String deviceId = authenticationToken.getDeviceId();

        if(challenge == null) {
            throw new BadCredentialsException("Invalid challenge");
        }
        String credentialsBase64 = authenticationToken.getCredentials();
        if (credentialsBase64 == null) {
            throw new AuthenticationCredentialsNotFoundException("Credenziali mancanti");
        }

        SpespappUser user = userRepo.findByUsername(challenge.getUsername())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Utente non trovato con username: " + challenge.getUsername()));

       String publicKeyBase64 = user.getPublicKeys()
                .stream()
                .filter(key -> key.getDeviceId().equals(deviceId))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("Chiave pubblica non trovata per dispositivo: " + deviceId))
                .getPublicKeyBase64();

        byte[] nonce = Base64.getDecoder().decode(challenge.getNonceBase64());

        log.debug("Starting signature verification for user: {}", user.getUsername());
        log.debug("Challenge ID: {}", challenge.getId());
        log.debug("Nonce (base64url): {}", challenge.getNonceBase64());
        log.debug("Nonce length: {} bytes", nonce.length);
        log.debug("Signature (base64): {}", credentialsBase64.substring(0, Math.min(50, credentialsBase64.length())));
        log.debug("Attempting verification with public key...");

        if(isSignatureValid(nonce, publicKeyBase64, credentialsBase64)) {
            return new WhiteAuthenticationToken(new JwtUserPrincipal(user.getId(), user.getUsername(), List.of()), List.of());
        }

        throw new BadCredentialsException("Verifica della firma fallita per utente: " + user.getUsername());
    }

    private String cleanBase64Key(String key) {
        return key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "")
                .replace("\r", "")
                .replace("\t", "")
                .replace(" ", "")
                .trim();
    }

    public boolean isSignatureValid(byte[] nonce, String publicKeyBase64, String signatureBase64) {
        try {
            log.debug("  isSignatureValid called");
            log.debug("  Nonce length: {} bytes", nonce.length);
            log.debug("  Signature (base64) length: {} chars", signatureBase64.length());

            String cleanKey = cleanBase64Key(publicKeyBase64);
            log.debug("  Public key cleaned, length: {} chars", cleanKey.length());
            byte[] publicKeyBytes = Base64.getDecoder().decode(cleanKey);
            log.debug("  Public key decoded, length: {} bytes", publicKeyBytes.length);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            log.debug("  Public key generated, algorithm: {}", publicKey.getAlgorithm());

            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
            log.debug("  Signature decoded, length: {} bytes", signatureBytes.length);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);

            signature.update(nonce);
            log.debug("  Calling signature.verify()...");

            boolean result = signature.verify(signatureBytes);
            log.debug("  signature.verify() returned: {}", result);
            return result;
        } catch (InvalidKeySpecException e) {
            throw new AuthenticationServiceException("Chiave non valida per RSA", e);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationServiceException("Algoritmo di firma non supportato", e);
        } catch (InvalidKeyException e) {
            throw new AuthenticationServiceException("Chiave pubblica non valida", e);
        } catch (SignatureException e) {
            throw new AuthenticationServiceException("Errore durante la verifica della firma", e);
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return RsaAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
