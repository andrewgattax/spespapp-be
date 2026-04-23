package it.trinex.spespappbe.repo;

import it.trinex.spespappbe.model.SpespappUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<SpespappUser, Long> {
    Optional<SpespappUser> findByUsername(String username);
}
