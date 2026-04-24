package it.trinex.spespappbe.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddSpespItemRequest {
    @NotBlank(message = "Il nome dell'ingrediente è richiesto")
    private String ingredientName;
    @NotBlank(message = "La quantità è richiesta")
    private String quantity;
}
