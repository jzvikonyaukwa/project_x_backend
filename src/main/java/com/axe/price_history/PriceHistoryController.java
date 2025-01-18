package com.axe.price_history;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-history")
public class PriceHistoryController {
    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @PostMapping
    public ResponseEntity<PriceHistory> create(@RequestBody PriceHistory priceHistory) {
        PriceHistory saved = priceHistoryService.createPriceHistory(priceHistory);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<PriceHistory>> getAll() {
        return ResponseEntity.ok(priceHistoryService.getAllPriceHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriceHistory> getById(@PathVariable Long id) {
        PriceHistory record = priceHistoryService.getPriceHistoryById(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceHistory> update(@PathVariable Long id, @RequestBody PriceHistory updated) {
        PriceHistory result = priceHistoryService.updatePriceHistory(id, updated);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = priceHistoryService.deletePriceHistory(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/record-change")
    public ResponseEntity<Void> recordPriceChange(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam java.math.BigDecimal oldPrice,
            @RequestParam java.math.BigDecimal newPrice
    ) {
        priceHistoryService.recordPriceChange(userId, productId, oldPrice, newPrice);
        return ResponseEntity.ok().build();
    }
}
