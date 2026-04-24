package it.trinex.spespappbe.mapper;

import it.trinex.spespappbe.dto.IngredientDTO;
import it.trinex.spespappbe.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDTO toDto(Ingredient ingredient);

    Ingredient toEntity(IngredientDTO ingredientDto);

    List<IngredientDTO> toDtoList(List<Ingredient> ingredients);

    List<Ingredient> toEntityList(List<IngredientDTO> ingredientDtos);

    void updateEntityFromDto(IngredientDTO ingredientDto, @MappingTarget Ingredient ingredient);
}
