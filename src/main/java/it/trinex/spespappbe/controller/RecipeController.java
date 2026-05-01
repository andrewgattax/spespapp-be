package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.dto.RecipeDTO;
import it.trinex.spespappbe.dto.request.recipe.AddRecipeDTO;
import it.trinex.spespappbe.dto.request.recipe.UpdateRecipeDTO;
import it.trinex.spespappbe.mapper.RecipeMapper;
import it.trinex.spespappbe.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    @PostMapping
    public ResponseEntity<RecipeDTO> addRecipe(@Valid @RequestBody AddRecipeDTO request) {
        return ResponseEntity.ok(recipeMapper.toDto(
                recipeService.addRecipe(request.getName(), request.getIngredientNames())
        ));
    }

    @GetMapping
    public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
        return ResponseEntity.ok(recipeMapper.toDtoList(recipeService.getAllRecipes()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<RecipeDTO> getRecipeByName(@PathVariable String name) {
        return ResponseEntity.ok(recipeMapper.toDto(
                recipeService.getRecipeByName(name)
        ));
    }

    @PutMapping("/{name}")
    public ResponseEntity<RecipeDTO> updateRecipe(
            @PathVariable String name,
            @Valid @RequestBody UpdateRecipeDTO request
    ) {
        return ResponseEntity.ok(recipeMapper.toDto(
                recipeService.updateRecipe(name, request.getIngredientNames())
        ));
    }
}
