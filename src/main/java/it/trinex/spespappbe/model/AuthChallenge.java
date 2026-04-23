package it.trinex.spespappbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Getter
public class AuthChallenge {
    private final UUID id;
    private final String username;
    @JsonIgnore
    private final byte[] nonce;
    private final String nonceBase64;
    private final Instant expiresAt;

    public AuthChallenge(String username) {
        this.id = UUID.randomUUID();

        this.username = username;

        SecureRandom secureRandom = new SecureRandom();

        nonce = new byte[64];

        secureRandom.nextBytes(this.nonce);

        this.nonceBase64 = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(this.nonce);

        this.expiresAt = Instant.now().plusSeconds(30);
    }
}
