package it.trinex.spespappbe.security.rsa;

import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RsaAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        RsaAuthenticationToken authenticationToken = (RsaAuthenticationToken) authentication;

        return null;
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return RsaAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
