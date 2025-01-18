package com.axe.credit_note.credit_notesDTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditNoteDto {
    private Long creditNoteId;
    private LocalDate dateCreated;
    private Long returnedProductId;
    private String returnedProductReason;
    private LocalDate returnedProductDate;
    private Long inventoryId;
    private LocalDate inventoryDateIn;
    private LocalDate inventoryDateOut;
    private Long productId;
//    private String manufacturedProductCode;
    private String productFrameName;
    private BigDecimal productProductLength;
//    private String manufacturedProductStickType;
    private String productProductFrameType;
    private String productProductStatus;
//    private BigDecimal productProductPricePerMeter;

    // Fields from ConsumableOnQuote
    private Long consumableOnQuoteId;
    private Integer consumableOnQuoteQty;
    private BigDecimal consumableOnQuoteUnitPrice;
    private String frameName; // New field for consumable name
    private String frameType; // New field for consumable type


}