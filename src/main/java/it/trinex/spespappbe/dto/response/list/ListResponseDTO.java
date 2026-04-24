package it.trinex.spespappbe.dto.response.list;

import it.trinex.spespappbe.dto.SpespItemDTO;
import lombok.Builder;

import java.util.List;

@Builder
public class ListResponseDTO {
    List<SpespItemDTO> itemsToBuy;
    List<SpespItemDTO> recentlyBought;
}
