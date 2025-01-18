package com.axe.delivery_notes_consumables_on_quote;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delivery-notes-consumables")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "https://axebuild.io"})
public class DeliveryNoteHasConsumablesOnQuoteController {
    private final DeliveryNoteHasConsumablesOnQuoteService deliveryNoteHasConsumablesOnQuoteService;

    public DeliveryNoteHasConsumablesOnQuoteController(DeliveryNoteHasConsumablesOnQuoteService deliveryNoteHasConsumablesOnQuoteService) {
        this.deliveryNoteHasConsumablesOnQuoteService = deliveryNoteHasConsumablesOnQuoteService;

    }

    @PostMapping
    public ResponseEntity<DeliveryNoteHasConsumablesOnQuote> create(@RequestBody DeliveryNoteHasConsumablesOnQuote deliveryNoteHasConsumablesOnQuote) {
        DeliveryNoteHasConsumablesOnQuote created = deliveryNoteHasConsumablesOnQuoteService.create(deliveryNoteHasConsumablesOnQuote);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryNoteHasConsumablesOnQuote>> getAll() {
        return ResponseEntity.ok(deliveryNoteHasConsumablesOnQuoteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryNoteHasConsumablesOnQuote> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryNoteHasConsumablesOnQuoteService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryNoteHasConsumablesOnQuote> update(@PathVariable Long id, @RequestBody DeliveryNoteHasConsumablesOnQuote deliveryNoteHasConsumablesOnQuote) {
        DeliveryNoteHasConsumablesOnQuote updated = deliveryNoteHasConsumablesOnQuoteService.update(id, deliveryNoteHasConsumablesOnQuote);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryNoteHasConsumablesOnQuoteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
