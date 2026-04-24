package it.trinex.spespappbe.mapper;

import it.trinex.spespappbe.dto.RecipeDTO;
import it.trinex.spespappbe.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface RecipeMapper {

    @Mapping(target = "ingredients", ignore = true)
    RecipeDTO toDto(Recipe recipe);

    Recipe toEntity(RecipeDTO recipeDto);

    List<RecipeDTO> toDtoList(List<Recipe> recipes);

    List<Recipe> toEntityList(List<RecipeDTO> recipeDtos);

    void updateEntityFromDto(RecipeDTO recipeDto, @MappingTarget Recipe recipe);
}
