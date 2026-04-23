package it.trinex.spespappbe.security.rsa;

import it.trinex.spespappbe.security.JwtUserPrincipal;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class WhiteAuthenticationToken extends AbstractAuthenticationToken {

    private final JwtUserPrincipal principal;

    public WhiteAuthenticationToken(JwtUserPrincipal principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return null;
    }
}
