package com.axe.consumables.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumablesStockLevelToBeChecked {
    Long consumableOnQuoteId;
    boolean stockAvailable;
}
