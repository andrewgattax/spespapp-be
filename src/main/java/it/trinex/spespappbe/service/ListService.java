package it.trinex.spespappbe.service;

import it.trinex.spespappbe.dto.SpespItemDTO;
import it.trinex.spespappbe.dto.request.AddSpespItemRequest;
import it.trinex.spespappbe.dto.response.list.ListResponseDTO;
import it.trinex.spespappbe.mapper.SpespItemMapper;
import it.trinex.spespappbe.model.Ingredient;
import it.trinex.spespappbe.model.SpespItem;
import it.trinex.spespappbe.repo.IngredientRepo;
import it.trinex.spespappbe.repo.SpespItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {


    private final SpespItemRepo spespItemRepo;
    private final SpespItemMapper spespItemMapper;

    private final int DAYS_AGO = 3;
    private final IngredientRepo ingredientRepo;

    public ListResponseDTO getList() {
        List<SpespItemDTO> itemsToBuy = spespItemRepo.findAllByChecked(false)
                .stream()
                .map(spespItemMapper::toDto)
                .toList();

        Instant daysAgoInstant = Instant.now().minus(DAYS_AGO, ChronoUnit.DAYS);
        List<SpespItemDTO> recentlyBought = spespItemRepo.findAllRecentsBought(daysAgoInstant)
                .stream()
                .map(spespItemMapper::toDto)
                .toList();

        return ListResponseDTO.builder()
                .itemsToBuy(itemsToBuy)
                .recentlyBought(recentlyBought)
                .build();
    }

    public ListResponseDTO addItem(AddSpespItemRequest request) {
        Ingredient ingredient = ingredientRepo.findByName(request.getIngredientName())
                .orElseGet(() -> ingredientRepo.save(new Ingredient(request.getIngredientName())));

        SpespItem newItem =
    }

}
