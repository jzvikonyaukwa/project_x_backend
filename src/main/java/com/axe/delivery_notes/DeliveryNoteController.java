package com.axe.delivery_notes;

import com.axe.clients.Client;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.delivery_notes.delivery_notesDTOs.*;

import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.DeliveryNoteResponse;
import com.axe.inventories.Inventory;
import com.axe.quotes.Quote;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-notes")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "https://axebuild.io"})
public class DeliveryNoteController {
    private final static Logger logger = LoggerFactory.getLogger(DeliveryNoteController.class);

    private final DeliveryNoteService deliveryNoteService;

    public DeliveryNoteController(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    @GetMapping
    public List<DeliveryNoteGetAllDto> getAllDeliveryNotes() {
        logger.info("Getting all delivery notes");
        
        return deliveryNoteService.getAllDeliveryNotes();
    }

    @PostMapping("/get-rows")
    @Operation(summary = "Get delivery notes based on ag-grid request")
    public ServerSideGetRowsResponse<List<DeliveryNoteGetAllDto>> fetchDeliveryNoteGetAllDtoGridRows(
            @RequestBody ServerSideGetRowsRequest request){
        logger.info("Getting delivery notes for request: {}", request);
        try {
            return deliveryNoteService.fetchDeliveryNoteGetAllDtoGridRows(request);
        } catch (Exception e) {
            logger.error("Error fetching delivery notes for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching delivery notes.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryNoteNewDto> getDeliveryNoteById(@PathVariable Long id) {
        return ResponseEntity.ok().body(deliveryNoteService.getDeliveryNoteById(id));
    }

    @GetMapping("/project/{projectId}")
    public List<DeliveryNoteResponse> getDeliveryNotesByProjectId(@PathVariable Long projectId) {
        return deliveryNoteService.getDeliveryNotesByProjectId(projectId);
    }

    @PostMapping
    public ResponseEntity<DeliveryNote> createDeliveryNote(@RequestBody DeliveryNoteDTO deliveryNote) {
        return ResponseEntity.ok(deliveryNoteService.createDeliveryNote(deliveryNote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeliveryNote> updateDeliveryNote(@PathVariable Long id, @RequestBody DeliveryNote deliveryNoteDetails) {
        if (!deliveryNoteService.isEditable(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok( deliveryNoteService.updateDeliveryNote(id, deliveryNoteDetails));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DeliveryNote> updateDeliveryNoteStatus(@PathVariable Long id, @RequestBody String status) {
        DeliveryNote updatedDeliveryNote = deliveryNoteService.updateDeliveryNoteStatus(id, status);
        return ResponseEntity.ok(updatedDeliveryNote);
    }

    @GetMapping("/{id}/inventories")
    public ResponseEntity<List<Inventory>> getInventoriesByDeliveryNoteId(@PathVariable Long id) {
        List<Inventory> inventories = deliveryNoteService.getInventoriesByDeliveryNoteId(id);
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    @GetMapping("/{id}/client-info")
    public ResponseEntity<Client> getClientInfoByDeliveryNoteId(@PathVariable Long id) {
        Client client = deliveryNoteService.getClientInfoByDeliveryNoteId(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/project-info")
    public ResponseEntity<ProjectInformation> getProjectInfoByDeliveryNoteId(@PathVariable Long id) {
        ProjectInformation project = deliveryNoteService.getProjectInfoByDeliveryNoteId(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{deliveryNoteId}/quote")
    public ResponseEntity<Quote> getQuoteFromDeliveryNote(@PathVariable Long deliveryNoteId) {
        Quote quote = deliveryNoteService.getQuoteFromDeliveryNote(deliveryNoteId);
        if (quote != null) {
            return ResponseEntity.ok(quote);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/is-editable-delivery-note")
    public ResponseEntity<Boolean> isEditable(@PathVariable("id") Long deliveryNoteId) {
        boolean editable = deliveryNoteService.isEditable(deliveryNoteId);
        return ResponseEntity.ok(editable);
    }

    @GetMapping("/{deliveryNoteId}/items")
    public List<DeliveryNoteItemDTO> getDeliveryNoteItemsForUpdate(@PathVariable Long deliveryNoteId) {
        return deliveryNoteService.getDeliveryNoteItemsForUpdate(deliveryNoteId);
    }

    @PostMapping("/remove-items")
    public ResponseEntity<Void> removeItems(@RequestBody DeliveryNoteRemoveItemsDTO removeItemsDTO) {
        deliveryNoteService.removeItemsFromDeliveryNote(removeItemsDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add-delivery-note-items")
    public ResponseEntity<Void> addItems(@RequestBody DeliveryNoteAddItemsDTO deliveryNoteAddItemsDTO) {
        deliveryNoteService.addItemsToDeliveryNote(deliveryNoteAddItemsDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeliveryNote(@PathVariable Long id) {
        deliveryNoteService.deleteDeliveryNote(id);
        return ResponseEntity.noContent().build();
    }


}
