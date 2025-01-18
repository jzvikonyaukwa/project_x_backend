package com.axe.invoices.DTOs;


import java.util.List;

public record InventoryPaginatedDTO(List<InvoiceResponseDTO> invoice, long totalElements) {
}
