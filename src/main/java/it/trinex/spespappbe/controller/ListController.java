package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.dto.IngredientDTO;
import it.trinex.spespappbe.dto.request.AddSpespItemRequest;
import it.trinex.spespappbe.dto.response.list.ListResponseDTO;
import it.trinex.spespappbe.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @GetMapping
    public ResponseEntity<ListResponseDTO> getList() {
        return ResponseEntity.ok(listService.getList());
    }

    @PostMapping("/item")
    public ResponseEntity<ListResponseDTO> addItem(@Valid @RequestBody AddSpespItemRequest ingredient) {
        return ResponseEntity.ok(listService.addItem(ingredient));
    }

}
