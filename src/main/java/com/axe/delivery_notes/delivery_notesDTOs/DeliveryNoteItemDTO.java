package com.axe.delivery_notes.delivery_notesDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
public class DeliveryNoteItemDTO {
    private Long inventoryId;
    private LocalDate dateIn;
    private LocalDate dateOut;
    private Long deliveryNoteId;
    private Long quoteId;
    private LocalDate dateAccepted;
    private Long cuttingListId;
    private String cuttingListStatus;
    private String consumableName;
    private String frameName;
    private String frameType;
    private String manufacturedProductStatus;
    private String productType;
    private BigDecimal totalLength;
    private Integer qty;
    private String projectName;
    private String clientName;
}