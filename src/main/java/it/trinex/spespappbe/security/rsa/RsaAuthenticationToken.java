package it.trinex.spespappbe.security.rsa;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class RsaAuthenticationToken extends AbstractAuthenticationToken {

    private final String challengeId;
    private final String signatureBase64;

    public RsaAuthenticationToken(String principal, String credentials) {
        super((Collection<? extends GrantedAuthority>) null);
        this.challengeId = principal;
        this.signatureBase64 = credentials;
        setAuthenticated(false);
    }

    @Override
    public @Nullable String getCredentials() {
        return challengeId;
    }

    @Override
    public @Nullable String getPrincipal() {
        return signatureBase64;
    }
}
