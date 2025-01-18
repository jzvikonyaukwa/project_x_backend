package com.axe.invoices.invoiceDTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class IssueInvoiceDTO {
    LocalDate dateInvoiced;
    Long saleOrderId;
    List<ItemsToBeInvoiced> itemsToBeInvoiced;

}
