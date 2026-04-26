package it.trinex.spespappbe.repo;

import it.trinex.spespappbe.model.UserPublicKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicKeyRepo extends JpaRepository<UserPublicKey, Long> {

}
