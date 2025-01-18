package com.axe.product_type_consumable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-type-consumables")
public class ProductTypeConsumableController {
    private final ProductTypeConsumableService productTypeConsumableService;

    public ProductTypeConsumableController(ProductTypeConsumableService productTypeConsumableService) {
        this.productTypeConsumableService = productTypeConsumableService;
    }

    @GetMapping
    public List<ProductTypeConsumable> getAllProductTypeConsumables() {
        return productTypeConsumableService.getAllProductTypeConsumables();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeConsumable> getProductTypeConsumableById(@PathVariable Long id) {
        return productTypeConsumableService.getProductTypeConsumableById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductTypeConsumable createProductTypeConsumable(@RequestBody ProductTypeConsumable productTypeConsumable) {
        return productTypeConsumableService.createProductTypeConsumable(productTypeConsumable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeConsumable> updateProductTypeConsumable(
            @PathVariable Long id,
            @RequestBody ProductTypeConsumable updatedConsumable) {
        try {
            ProductTypeConsumable consumable = productTypeConsumableService.updateProductTypeConsumable(id, updatedConsumable);
            return ResponseEntity.ok(consumable);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductTypeConsumable(@PathVariable Long id) {
        productTypeConsumableService.deleteProductTypeConsumable(id);
        return ResponseEntity.noContent().build();
    }
}
