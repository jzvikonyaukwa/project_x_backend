package com.axe.invoices.invoiceDTOs;

import com.axe.invoices.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteInvoiceService {

    private final InvoiceRepository invoiceRepository;

    public DeleteInvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
}
