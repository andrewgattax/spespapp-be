package it.trinex.spespappbe.dto.request.list;


import it.trinex.spespappbe.model.PriorityLevel;
import it.trinex.spespappbe.model.UnitType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddSpespItemRequest {
    @NotBlank(message = "Il nome dell'ingrediente è richiesto")
    private String ingredientName;
    @NotBlank(message = "La quantità è richiesta")
    private float quantity;
    private UnitType unitType;
    private PriorityLevel priorityLevel;
}
