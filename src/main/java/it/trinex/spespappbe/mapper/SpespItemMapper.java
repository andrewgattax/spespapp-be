package it.trinex.spespappbe.mapper;

import it.trinex.spespappbe.dto.SpespItemDTO;
import it.trinex.spespappbe.model.SpespItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IngredientMapper.class})
public interface SpespItemMapper {

    SpespItemDTO toDto(SpespItem spespItem);

    SpespItem toEntity(SpespItemDTO spespItemDto);

    List<SpespItemDTO> toDtoList(List<SpespItem> spespItems);

    List<SpespItem> toEntityList(List<SpespItemDTO> spespItemDtos);

    void updateEntityFromDto(SpespItemDTO spespItemDto, @MappingTarget SpespItem spespItem);
}
