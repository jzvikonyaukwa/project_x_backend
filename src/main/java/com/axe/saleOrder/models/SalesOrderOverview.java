package com.axe.saleOrder.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SalesOrderOverview {
    Long saleOrderId;
    String clientName;
    String salesOrderStatus;
    Boolean reserveStock;
    LocalDate targetDate;
    LocalDate salesOrderIssued;
    Integer completedCuttingList;
    Integer inProgressCuttingLists;
}
