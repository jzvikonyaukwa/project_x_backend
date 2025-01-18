package com.axe.credit_note;


import com.axe.clients.Client;
import com.axe.credit_note.credit_notesDTO.CreditNoteDto;
import com.axe.credit_note.credit_notesDTO.CreditNoteRequest;
import com.axe.credit_note.exceptions.ResourceNotFoundException;
import com.axe.inventories.Inventory;
import com.axe.inventories.InventoryService;
import com.axe.projects.ProjectService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CreditNoteService {


    private static final Logger log = LoggerFactory.getLogger(CreditNoteService.class);
    private final CreditNoteRepository creditNoteRepository;
    private final InventoryService inventoryService;
    private final ProjectService projectService;;
    @PersistenceContext
    private EntityManager entityManager;

    public CreditNoteService(CreditNoteRepository creditNoteRepository,
                             InventoryService inventoryService
                             , ProjectService projectService

                             ){
        this.creditNoteRepository = creditNoteRepository;
        this.inventoryService = inventoryService;
        this.projectService = projectService;
    }

    public List<CreditNote> getAllCreditNotes() {
        return creditNoteRepository.findAll();
    }

    public List<CreditNoteDto> getCreditNoteDetailsById(Long id) {

        List<CreditNoteDto> details = creditNoteRepository.findCreditNoteDetailsById(id);

        if (details.isEmpty()) {
            throw new ResourceNotFoundException("CreditNote not found");
        }

        return details;
    }

    public CreditNote getCreditNoteById(Long id) {

        return creditNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CreditNote not found"));
    }


    @Transactional
    public CreditNote createCreditNote(CreditNoteRequest creditNoteRequest) {
        CreditNote creditNote = new CreditNote();

        log.info("Creating credit note with returned products size: {}",
                creditNoteRequest.returnedProducts() != null ? creditNoteRequest.returnedProducts().size() : 0);

        // Set project and date created for credit note
        creditNote.setProject(projectService.getProjectById(creditNoteRequest.projectId()));
        creditNote.setDateCreated(creditNoteRequest.dateCreated());

        // Link returned products to credit note
        if (creditNoteRequest.returnedProducts() != null) {
            creditNoteRequest.returnedProducts().forEach(returnedProduct -> {
                returnedProduct.setCreditNote(creditNote);
                // Fetch and merge inventories to ensure they are managed entities
                List<Inventory> inventories = returnedProduct.getInventories().stream()
                        .map(inventory -> {
                            Inventory managedInventory = inventoryService.getInventoryById(inventory.getId()).orElse(inventory);
                            managedInventory.setReturnedProducts(returnedProduct);
                            return entityManager.merge(managedInventory);
                        })
                        .toList();
                returnedProduct.setInventories(inventories);
            });
            creditNote.setReturnedProducts(creditNoteRequest.returnedProducts());
        }

        return creditNoteRepository.save(creditNote);
    }




    public CreditNote updateCreditNote(Long id, CreditNote creditNoteDetails) {
        return creditNoteRepository.findById(id)
                .map(existingCreditNote -> {
                    existingCreditNote.setDateCreated(creditNoteDetails.getDateCreated());
                    existingCreditNote.setQuote(creditNoteDetails.getQuote());

                    // Update returned products
                    if (creditNoteDetails.getReturnedProducts() != null) {
                        existingCreditNote.getReturnedProducts().clear();
                        existingCreditNote.getReturnedProducts().addAll(creditNoteDetails.getReturnedProducts());
                        existingCreditNote.getReturnedProducts().forEach(returnedProduct -> returnedProduct.setCreditNote(existingCreditNote));
                    }

                    return creditNoteRepository.save(existingCreditNote);
                }).
                orElseThrow(() -> new ResourceNotFoundException("CreditNote not found"));
    }

    public void deleteCreditNote(Long id) {
        CreditNote creditNote = creditNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CreditNote not found"));
        creditNoteRepository.delete(creditNote);
    }

    public List<CreditNote> getCreditNotesByProjectId(Long projectId) {
        return creditNoteRepository.findByProjectId(projectId);
    }

    public Client getClientInfoByDeliveryNoteId(Long id) {
        Optional<CreditNote> deliveryNote = creditNoteRepository.findById(id);
        return deliveryNote.map(dn -> dn.getProject().getClient()).orElse(null);
    }

}
