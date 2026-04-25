package it.trinex.spespappbe.dto.request.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddRecipeDTO {
    @NotBlank
    private String name;

    @NotEmpty
    private List<String> ingredientNames;
}
