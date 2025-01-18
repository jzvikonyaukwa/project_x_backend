package com.axe.invoices.invoiceDTOs.services;

import com.axe.clients.Client;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.invoices.CreateInvoiceService;
import com.axe.invoices.DTOs.InventoryPaginatedDTO;
import com.axe.invoices.DTOs.InvoiceResponseDTO;
import com.axe.invoices.Invoice;
import com.axe.invoices.InvoiceRepository;
import com.axe.invoices.DTOs.InvoiceDTO;
import com.axe.invoices.invoiceDTOs.IssueInvoiceDTO;
import com.axe.invoices.invoiceDTOs.services.providers.InvoiceDTORowProvider;
import com.axe.product.Product;
import com.axe.utilities.InvoiceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;
    private final CreateInvoiceService createInvoiceService;
    private final InvoiceDTORowProvider invoiceDTORowProvider;

    public InvoiceService(InvoiceRepository invoiceRepository, CreateInvoiceService createInvoiceService,
            InvoiceDTORowProvider invoiceDTORowProvider) {
        this.invoiceRepository = invoiceRepository;
        this.createInvoiceService = createInvoiceService;
        this.invoiceDTORowProvider = invoiceDTORowProvider;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public InventoryPaginatedDTO getAllInvoicesSTO(Pageable pageable){
         Page<InvoiceResponseDTO> invoiceResponseDTO = invoiceRepository.findAllInvoices(pageable);
         return new  InventoryPaginatedDTO(invoiceResponseDTO.getContent(), invoiceResponseDTO.getTotalElements());
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElseThrow();
    }

    public Invoice updateInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Transactional
    public Invoice issueInvoice(IssueInvoiceDTO invoicesToIssue) {
        return createInvoiceService.createInvoice(invoicesToIssue);
    }


    public Client getClientByInvoiceId(Long invoiceId) {
        return invoiceRepository.findClientByInvoiceId(invoiceId).orElseThrow(
                () -> new InvoiceNotFoundException(1000, "No client found for the provided invoice ID: " + invoiceId));
    }

    public void deleteInvoice(Long invoiceId) {

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        // Invoice will not be deleted if it is associated with any consumableOnQuote,
        // outsourcedProduct or cuttingList
        logger.info("Deleting invoice with ID: {}", invoiceId);

        for (ConsumableOnQuote consumableOnQuote : invoice.getConsumablesOnQuote()) {
            consumableOnQuote.setInvoice(null);
        }
        for(Product product : invoice.getProducts()){
            product.setInvoice(null);
        }

        invoiceRepository.deleteById(invoiceId);
    }

    public ServerSideGetRowsResponse<List<InvoiceDTO>> fetchInvoiceDTOGridRows(ServerSideGetRowsRequest request) {
        logger.debug("Getting all invoices for request: {}", request);
        try {
            return invoiceDTORowProvider.getRows(request);
        } catch (Exception e) {
            logger.error("Error in getting invoices: ", e);
            throw new RuntimeException("Error in getting invoices: ", e);
        }
    }
}
