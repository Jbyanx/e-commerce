package com.unimagdalena.inventory_service.controller;

import com.unimagdalena.inventory_service.entity.InventoryItem;
import com.unimagdalena.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository repository;

    @GetMapping
    public List<InventoryItem> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{productId}")
    public InventoryItem getByProductId(@PathVariable String productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @PostMapping
    public InventoryItem createItem(@RequestBody InventoryItem item) {
        return repository.save(item);
    }

    @PutMapping("/{id}")
    public InventoryItem updateItem(@PathVariable UUID id, @RequestBody InventoryItem item) {
        item.setId(id);
        return repository.save(item);
    }
}
