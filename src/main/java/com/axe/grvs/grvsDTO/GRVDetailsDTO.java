package com.axe.grvs.grvsDTO;

import com.axe.consumables.DTOs.ConsumablePostDTO;
import com.axe.warehouse.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GRVDetailsDTO {

    Long id;
    LocalDate dateReceived;
    String comments;
    Long supplierId;
    String supplierGrvCode;
    Long purchaseOrderId;
    Warehouse warehouse;
    List<SteelCoilPostDTO> steelCoils;
    List<ConsumablePostDTO> consumablesOnGrv;
}
