package it.trinex.spespappbe.dto.response.list;

import it.trinex.spespappbe.dto.SpespItemDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListResponseDTO {
    List<SpespItemDTO> itemsToBuy;
    List<SpespItemDTO> recentlyBought;
}
