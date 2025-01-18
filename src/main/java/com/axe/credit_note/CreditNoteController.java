package com.axe.credit_note;


import com.axe.clients.Client;
import com.axe.credit_note.credit_notesDTO.CreditNoteDto;
import com.axe.credit_note.credit_notesDTO.CreditNoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credit-notes")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "https://axebuild.io"})
public class CreditNoteController {


    private final CreditNoteService creditNoteService;

    public  CreditNoteController(CreditNoteService creditNoteService) {
        this.creditNoteService = creditNoteService;
    }

    @GetMapping
    public List<CreditNote> getAllCreditNotes() {
        return creditNoteService.getAllCreditNotes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditNote> getCreditNoteById(@PathVariable Long id) {
        return ResponseEntity.ok(creditNoteService.getCreditNoteById(id));
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<List<CreditNoteDto>> getCreditNoteDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(creditNoteService.getCreditNoteDetailsById(id));
    }

    @PostMapping
    public CreditNote createCreditNote(@RequestBody CreditNoteRequest creditNoteRequest) {
        return creditNoteService.createCreditNote(creditNoteRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditNote> updateCreditNote(@PathVariable Long id, @RequestBody CreditNote creditNoteDetails) {
        CreditNote updatedCreditNote = creditNoteService.updateCreditNote(id, creditNoteDetails);
        return ResponseEntity.ok(updatedCreditNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditNote(@PathVariable Long id) {
        creditNoteService.deleteCreditNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<CreditNote>> getCreditNotesByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(creditNoteService.getCreditNotesByProjectId(projectId));
    }


    @GetMapping("/{id}/client-info")
    public ResponseEntity<Client> getClientInfoByDeliveryNoteId(@PathVariable Long id) {
        Client client = creditNoteService.getClientInfoByDeliveryNoteId(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
