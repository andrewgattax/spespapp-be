package it.trinex.spespappbe.dto.request.list;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteItemBulkRequest {
    @NotEmpty(message = "La lista degli itemId non può essere vuota")
    List<Long> itemIds;
}
