package com.axe.consumables.DTOs;

import com.axe.consumableSource.SourceCountry;
import com.axe.consumable_category.ConsumableCategory;
import com.axe.warehouse.Warehouse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ConsumableDetails {

    Long id;
    String serialNumber;
    String name;
    String uom;
    Integer qty;
    Integer minAlertQty;
    ConsumableCategory category;
    Warehouse warehouseName;
    SourceCountry sourceCountry;

}
