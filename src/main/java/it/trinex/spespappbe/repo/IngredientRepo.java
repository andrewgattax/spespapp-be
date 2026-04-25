package it.trinex.spespappbe.repo;

import it.trinex.spespappbe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepo extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);

}
