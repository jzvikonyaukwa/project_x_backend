package com.axe.grvs.grvsDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRVDetailDTO {
    // GRV
    private Long grvId;
    private LocalDate dateReceived;
    private String comments;
    private String supplierGrvCode;

    // Steel Coil Supplier related fields
    private Long steelCoilSupplierId;
    private String steelCoilSupplierName;

    // Steel Coil related fields
    private String status;
    private Long steelCoilId;
    private String coilNumber;
    private String cardNumber;
    private BigDecimal estimatedMeterRunOnArrival;
    private BigDecimal weightInKgsOnArrival;
    private BigDecimal estimatedMetersRemaining;
    private BigDecimal landedCostPerMtr;
    private BigDecimal landedCostPerKg;
    private String isqGrade;

    // Width, Color, Finish, Coating, and Gauge fields
    private String finish;
    private String color;
    private BigDecimal width;
    private String coating;
    private BigDecimal gauge;

    // Steel Coil Warehouse related fields
    private Long steelCoilWarehouseId;
    private String steelCoilWarehouse;

    // Consumables
    private Long consumableOnGrvId;
    private Long consumableInWarehouseId;
    private String consumableName;
    private Long consumableId;
    private String serialNumber;
    private String uom;
    private Long consumableCategoryId;
    private BigDecimal avgLandedPrice;

    // Consumable Supplier related fields
    private Long consumableSupplierId;
    private String consumableSupplierName;

    // Consumable Warehouse related fields
    private Long consumableWarehouseId;
    private String consumableWarehouse;

    private Integer qtyOrdered;
//    private BigDecimal landedPrice;

    // Purchase Order ID
    private Long purchaseOrderId;
}
