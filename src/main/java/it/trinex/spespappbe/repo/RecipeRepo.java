package it.trinex.spespappbe.repo;

import it.trinex.spespappbe.model.Ingredient;
import it.trinex.spespappbe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepo extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByName(String name);

    @Query("SELECT i FROM Ingredient i WHERE i.name IN :names")
    List<Ingredient> findIngredientsByNames(@Param("names") List<String> names);
}
