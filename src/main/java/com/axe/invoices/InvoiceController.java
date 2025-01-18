package com.axe.invoices;

import com.axe.clients.Client;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.invoices.DTOs.InventoryPaginatedDTO;
import com.axe.invoices.DTOs.InvoiceDTO;
import com.axe.invoices.invoiceDTOs.IssueInvoiceDTO;
import com.axe.invoices.invoiceDTOs.services.InvoiceService;
import com.axe.purchaseOrders.PurchaseOrderController;
import com.axe.quotePrice.QuotePrice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class InvoiceController {
    private final static Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
            this.invoiceService = invoiceService;
        }



    @GetMapping("")
    public InventoryPaginatedDTO getAllQuotes(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return invoiceService.getAllInvoicesSTO(pageable);
    }



    @PostMapping("/get-rows")
    @Operation(summary = "Get invoices based on ag-grid request")
    public ServerSideGetRowsResponse<List<InvoiceDTO>> fetchInvoiceDTOGridRows(
            @RequestBody ServerSideGetRowsRequest request) {
        logger.info("Getting invoices for request: {}", request);
        try {
            return invoiceService.fetchInvoiceDTOGridRows(request);
        } catch (Exception e) {
            logger.error("Error fetching invoices for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching invoices.");
        }
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id){
        return invoiceService.getInvoiceById(id);
    }

    @PatchMapping("update-invoice")
    public Invoice updateInvoice(@RequestBody Invoice invoice){
        return invoiceService.updateInvoice(invoice);
    }

    @PostMapping("issue-invoice")
    public Invoice issueInvoice(@RequestBody IssueInvoiceDTO issueInvoiceDTO){
        return invoiceService.issueInvoice(issueInvoiceDTO);
    }

    @GetMapping("{invoiceId}/client")
    public ResponseEntity<Client> getClientByInvoiceId(@PathVariable Long invoiceId) {
        return ResponseEntity.ok(invoiceService.getClientByInvoiceId(invoiceId));
    }

    @DeleteMapping("{invoiceId}")
    public void deleteInvoice(@PathVariable Long invoiceId){
        invoiceService.deleteInvoice(invoiceId);
    }
}
