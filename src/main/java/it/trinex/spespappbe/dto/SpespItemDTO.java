package it.trinex.spespappbe.dto;

import it.trinex.spespappbe.model.UnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpespItemDTO {
    private Long id;
    private IngredientDTO ingredient;
    private String quantity;
    private boolean checked;
    private UnitType unitType;
    private Instant createdAt;
    private Instant updatedAt;
}
