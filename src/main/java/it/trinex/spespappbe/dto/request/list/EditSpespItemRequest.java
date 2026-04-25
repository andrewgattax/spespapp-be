package it.trinex.spespappbe.dto.request.list;

import it.trinex.spespappbe.model.PriorityLevel;
import it.trinex.spespappbe.model.UnitType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditSpespItemRequest {
    @NotNull(message = "La quantità è richiesta")
    private Float quantity;

    private UnitType unitType;

    private PriorityLevel priorityLevel;
}
