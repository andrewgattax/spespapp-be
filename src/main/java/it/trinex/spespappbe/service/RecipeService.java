package it.trinex.spespappbe.service;

import it.trinex.spespappbe.exception.RecordNotFoundException;
import it.trinex.spespappbe.model.Ingredient;
import it.trinex.spespappbe.model.Recipe;
import it.trinex.spespappbe.repo.RecipeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepo recipeRepo;

    public Recipe addRecipe(String name, List<String> ingredientNames) {
        List<Ingredient> ingredients = resolveIngredients(ingredientNames);
        Recipe recipe = Recipe.builder()
                .name(name)
                .ingredients(ingredients)
                .build();
        return recipeRepo.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepo.findAll();
    }

    @Transactional
    public Recipe updateRecipe(String name, List<String> ingredientNames) {
        Recipe recipe = recipeRepo.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException("Recipe not found: " + name));

        List<Ingredient> newIngredients = resolveIngredients(ingredientNames);
        
        recipe.getIngredients().clear();
        recipe.getIngredients().addAll(newIngredients);
        
        return recipeRepo.save(recipe);
    }

    private List<Ingredient> resolveIngredients(List<String> ingredientNames) {
        if (ingredientNames == null || ingredientNames.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> normalizedNames = ingredientNames.stream()
                .map(Ingredient::normalizeIngredientName)
                .distinct()
                .toList();

        Map<String, Ingredient> existingIngredients = recipeRepo.findIngredientsByNames(normalizedNames)
                .stream()
                .collect(Collectors.toMap(Ingredient::getName, i -> i));

        return normalizedNames.stream()
                .map(name -> existingIngredients.getOrDefault(name, new Ingredient(name)))
                .collect(Collectors.toList());
    }
}
