package it.trinex.spespappbe.service;

import it.trinex.spespappbe.dto.SpespItemDTO;
import it.trinex.spespappbe.dto.request.list.AddSpespItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.AddSpespItemRequest;
import it.trinex.spespappbe.dto.request.list.CheckItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.DeleteItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.EditSpespItemRequest;
import it.trinex.spespappbe.dto.response.list.ListResponseDTO;
import it.trinex.spespappbe.exception.RecordNotFoundException;
import it.trinex.spespappbe.mapper.SpespItemMapper;
import it.trinex.spespappbe.model.Ingredient;
import it.trinex.spespappbe.model.SpespItem;
import it.trinex.spespappbe.repo.IngredientRepo;
import it.trinex.spespappbe.repo.SpespItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {


    private final SpespItemRepo spespItemRepo;
    private final SpespItemMapper spespItemMapper;

    private final int DAYS_AGO = 3;
    private final IngredientRepo ingredientRepo;

    public ListResponseDTO getList() {

        List<SpespItemDTO> itemsToBuy = spespItemMapper.toDtoList(spespItemRepo.findAllByChecked(false));

        Instant daysAgoInstant = Instant.now().minus(DAYS_AGO, ChronoUnit.DAYS);
        List<SpespItemDTO> recentlyBought = spespItemMapper.toDtoList(spespItemRepo.findAllRecentsBought(daysAgoInstant));

        return ListResponseDTO.builder()
                .itemsToBuy(itemsToBuy)
                .recentlyBought(recentlyBought)
                .build();
    }

    public ListResponseDTO addItem(AddSpespItemRequest request) {

        String normalizedName = Ingredient.normalizeIngredientName(request.getIngredientName());

        Ingredient ingredient = ingredientRepo.findByName(normalizedName)
                .orElseGet(() -> ingredientRepo.save(new Ingredient(normalizedName)));

        SpespItem newItem = SpespItem.builder()
                .ingredient(ingredient)
                .checked(false)
                .priorityLevel(request.getPriorityLevel())
                .quantity(request.getQuantity())
                .unitType(request.getUnitType())
                .build();

        spespItemRepo.save(newItem);

        return getList();
    }

    public ListResponseDTO addItemBulk(AddSpespItemBulkRequest request) {

        List<SpespItem> toPush = new ArrayList<>();

        for (AddSpespItemRequest item : request.getItems()) {

            String normalizedName = Ingredient.normalizeIngredientName(item.getIngredientName());

            Ingredient ingredient = ingredientRepo.findByName(normalizedName)
                    .orElseGet(() -> ingredientRepo.save(new Ingredient(normalizedName)));

            SpespItem newItem = SpespItem.builder()
                    .ingredient(ingredient)
                    .checked(false)
                    .priorityLevel(item.getPriorityLevel())
                    .quantity(item.getQuantity())
                    .unitType(item.getUnitType())
                    .build();

            toPush.add(newItem);
        }

        spespItemRepo.saveAll(toPush);

        return getList();
    }

    public ListResponseDTO checkItem(Long id) {
        SpespItem item = spespItemRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Spespitem non trovato con id: " + id));

        item.setChecked(true);

        spespItemRepo.save(item);

        return getList();
    }

    public ListResponseDTO checkItemBulk(CheckItemBulkRequest request) {
        List<SpespItem> itemsToCheck = spespItemRepo.findAllById(request.getItemIds());

        if (itemsToCheck.size() != request.getItemIds().size()) {
            throw new RecordNotFoundException("Alcuni Spespitem non sono stati trovati");
        }

        for (SpespItem item : itemsToCheck) {
            item.setChecked(true);
        }

        spespItemRepo.saveAll(itemsToCheck);

        return getList();
    }

    public ListResponseDTO deleteItem(Long id) {
        if (!spespItemRepo.existsById(id)) {
            throw new RecordNotFoundException("Spespitem non trovato con id: " + id);
        }

        spespItemRepo.deleteById(id);

        return getList();
    }

    public ListResponseDTO deleteItemBulk(DeleteItemBulkRequest request) {
        List<SpespItem> itemsToDelete = spespItemRepo.findAllById(request.getItemIds());

        if (itemsToDelete.size() != request.getItemIds().size()) {
            throw new RecordNotFoundException("Alcuni Spespitem non sono stati trovati");
        }

        spespItemRepo.deleteAll(itemsToDelete);

        return getList();
    }

    public ListResponseDTO editItem(Long id, EditSpespItemRequest request) {
        SpespItem item = spespItemRepo.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Spespitem non trovato con id: " + id));

        // Update only the editable fields
        item.setQuantity(request.getQuantity());
        if (request.getUnitType() != null) {
            item.setUnitType(request.getUnitType());
        }
        if (request.getPriorityLevel() != null) {
            item.setPriorityLevel(request.getPriorityLevel());
        }

        spespItemRepo.save(item);

        return getList();
    }

}
