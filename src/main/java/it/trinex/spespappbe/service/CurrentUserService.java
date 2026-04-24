package it.trinex.spespappbe.service;

import it.trinex.spespappbe.model.SpespappUser;
import it.trinex.spespappbe.repo.UserRepo;
import it.trinex.spespappbe.security.JwtUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepo userRepo;

    public Optional<JwtUserPrincipal> getCurrentPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof JwtUserPrincipal)
                .map(auth -> (JwtUserPrincipal) auth.getPrincipal());
    }

    public Optional<SpespappUser> getCurrentUser() {
        Optional<JwtUserPrincipal>  userPrincipal = getCurrentPrincipal();
        if(userPrincipal.isPresent()) {
            return userRepo.findByUsername(userPrincipal.get().getUsername());
        } else {
            return Optional.empty();
        }
    }

}
