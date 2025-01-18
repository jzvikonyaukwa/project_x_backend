package com.axe.consumables.DTOs;

import com.axe.consumableSource.SourceService;
import com.axe.consumable_category.ConsumableCategoryService;
import com.axe.warehouse.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class ConsumablesDetailsService {

    private final ConsumableCategoryService consumableCategoryService;
    private final SourceService sourceService;
    private final WarehouseService warehouseService;

    public ConsumablesDetailsService(ConsumableCategoryService consumableCategoryService,
                                     SourceService sourceService,
                                     WarehouseService warehouseService) {
        this.consumableCategoryService = consumableCategoryService;
        this.sourceService = sourceService;
        this.warehouseService = warehouseService;
    }

    public ConsumableDetails createConsumableDetailsFromSQL(ConsumableDetailsSQL consumableDetailsSQL) {
        ConsumableDetails consumableDetails = new ConsumableDetails();
        consumableDetails.setId(consumableDetailsSQL.getConsumableId());
        consumableDetails.setName(consumableDetailsSQL.getName());
        consumableDetails.setSerialNumber(consumableDetailsSQL.getSerialNumber());
        consumableDetails.setUom(consumableDetailsSQL.getUom());
        consumableDetails.setQty(consumableDetailsSQL.getQty());
//        consumableDetails.setMinAlertQty(consumableDetailsSQL.getMinAlertQty());
        consumableDetails.setCategory(consumableCategoryService.getConsumableCategoryByName(consumableDetailsSQL.getCategory()));
        consumableDetails.setWarehouseName(warehouseService.getWarehouseByName(consumableDetailsSQL.getWarehouseName()));
        consumableDetails.setSourceCountry(sourceService.getSourceCountryByName(consumableDetailsSQL.getSourceCountry()));
        return consumableDetails;
    }
}
