package com.axe.purchaseOrders.DTOs;

import com.axe.consumables.DTOs.ConsumableOnPurchaseOrderPostDTO;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class PurchaseOrderPostDTO {
    Long purchaseOrderId;
    Long supplierId;
    LocalDate dateIssued;
    LocalDateTime expectedDeliveryDate;
    String comments;
    String status;
    Boolean paid;
    List<SteelCoilPostDTO> productPurchases;
    List<ConsumableOnPurchaseOrderPostDTO> consumablesOnPurchaseOrder;
}
