package com.axe.purchaseOrders.DTOs;

import com.axe.purchaseOrders.PurchaseOrder;

import java.util.List;

public record PurchaseOrdersPaginationResponseDTO(List<PurchaseOrder> purchaseOrders, long totalElements) {
}
