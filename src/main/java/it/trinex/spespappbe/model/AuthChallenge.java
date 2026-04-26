package it.trinex.spespappbe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthChallenge {
    private UUID id;
    private String username;
    private String nonceBase64;
    private Instant expiresAt;

    public AuthChallenge(String username) {
        this.id = UUID.randomUUID();

        this.username = username;

        SecureRandom secureRandom = new SecureRandom();

        byte[] nonce = new byte[64];

        secureRandom.nextBytes(nonce);

        this.nonceBase64 = Base64.getEncoder().withoutPadding()
                .encodeToString(nonce);

        this.expiresAt = Instant.now().plusSeconds(30);
    }
}
