package com.axe.consumable_product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumable-products")
public class ConsumableProductController {
    private final ConsumableProductService consumableProductService;

    public ConsumableProductController(ConsumableProductService consumableProductService) {
        this.consumableProductService = consumableProductService;
    }


    @GetMapping
    public List<ConsumableProduct> getAllConsumableProducts() {
        return consumableProductService.getAllConsumableProducts();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ConsumableProduct> getConsumableProductById(@PathVariable Long id) {
        return consumableProductService.getConsumableProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ConsumableProduct createConsumableProduct(@RequestBody ConsumableProduct consumableProduct) {
        return consumableProductService.createConsumableProduct(consumableProduct);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ConsumableProduct> updateConsumableProduct(
            @PathVariable Long id,
            @RequestBody ConsumableProduct updatedConsumableProduct) {
        try {
            ConsumableProduct consumableProduct = consumableProductService.updateConsumableProduct(id, updatedConsumableProduct);
            return ResponseEntity.ok(consumableProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumableProduct(@PathVariable Long id) {
        consumableProductService.deleteConsumableProduct(id);
        return ResponseEntity.noContent().build();
    }
}
