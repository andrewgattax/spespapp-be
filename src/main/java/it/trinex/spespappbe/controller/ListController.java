package it.trinex.spespappbe.controller;

import it.trinex.spespappbe.dto.request.list.AddSpespItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.AddSpespItemRequest;
import it.trinex.spespappbe.dto.request.list.CheckItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.DeleteItemBulkRequest;
import it.trinex.spespappbe.dto.request.list.EditSpespItemRequest;
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

    @PostMapping("/item-bulk")
    public ResponseEntity<ListResponseDTO> addItemBulk(@Valid @RequestBody AddSpespItemBulkRequest request) {
        return ResponseEntity.ok(listService.addItemBulk(request));
    }

    @PutMapping("/check/{id}")
    public ResponseEntity<ListResponseDTO> checkItem(@PathVariable Long id) {
        return ResponseEntity.ok(listService.checkItem(id));
    }

    @PutMapping("/check-bulk")
    public ResponseEntity<ListResponseDTO> checkItemBulk(@RequestBody CheckItemBulkRequest request) {
        return ResponseEntity.ok(listService.checkItemBulk(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ListResponseDTO> deleteItem(@PathVariable Long id) {
        return ResponseEntity.ok(listService.deleteItem(id));
    }

    @DeleteMapping("/delete-bulk")
    public ResponseEntity<ListResponseDTO> deleteItemBulk(@RequestBody DeleteItemBulkRequest request) {
        return ResponseEntity.ok(listService.deleteItemBulk(request));
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ListResponseDTO> editItem(
            @PathVariable Long id,
            @Valid @RequestBody EditSpespItemRequest request) {
        return ResponseEntity.ok(listService.editItem(id, request));
    }

}
