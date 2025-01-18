package com.axe.saleOrder.models;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface SaleOrderData {
    Long getSaleOrderId();
    String getSalesOrderStatus();
    LocalDate getSalesOrderIssued();
    LocalDate getTargetDate();
    LocalDate getDateInvoiced();
    @Value("#{target.paid == 1}")
    Boolean getPaid();
    Long getInvoiceId();
    LocalDate getDateIssued();
    LocalDate getDateLastModified();
    String getClientName();
    String getCuttingListStatus();
    String getPriority();
    String getPlanName();


}
