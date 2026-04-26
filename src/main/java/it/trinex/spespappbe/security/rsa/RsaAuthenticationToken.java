package it.trinex.spespappbe.security.rsa;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class RsaAuthenticationToken extends AbstractAuthenticationToken {

    private final UUID challengeId;
    private final String signatureBase64;
    private final String deviceId;

    public RsaAuthenticationToken(UUID principal, String credentials, String deviceId) {
        super((Collection<? extends GrantedAuthority>) null);
        this.challengeId = principal;
        this.signatureBase64 = credentials;
        this.deviceId = deviceId;
        setAuthenticated(false);
    }

    @Override
    public @Nullable String getCredentials() {
        return signatureBase64;
    }

    @Override
    public @Nullable UUID getPrincipal() {
        return challengeId;
    }

    public @Nullable String getDeviceId() { return deviceId; }
}
