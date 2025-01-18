package com.axe.consumablesInWarehouse;

import com.axe.consignor.Consignor;
import com.axe.consumables.Consumable;
import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.interBranchTransfer.consumables.ConsumableInterBranchTransfer;
import com.axe.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consumables_in_warehouse")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ConsumablesInWarehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "avg_landed_price")
    private BigDecimal avgLandedPrice;

    @ManyToOne()
    @JoinColumn(name = "consumable_id")
    private Consumable consumable;

    @ManyToOne()
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne()
    @JoinColumn(name = "consignor_id")
    private Consignor consignor;

    @OneToMany(mappedBy = "consumableInWarehouse", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ConsumablesOnGrv> consumablesOnGrv = new ArrayList<>();

    @OneToMany(mappedBy = "consumableInWarehouseTo", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConsumableInterBranchTransfer> consumableInterBranchTransfersTo = new ArrayList<>();

    @OneToMany(mappedBy = "consumableInWarehouseFrom", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConsumableInterBranchTransfer> consumableInterBranchTransfersFrom = new ArrayList<>();
}
