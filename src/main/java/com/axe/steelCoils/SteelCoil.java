package com.axe.steelCoils;

import com.axe.consignor.Consignor;
import com.axe.grvs.GRV;
import com.axe.interBranchTransfer.steelCoils.SteelCoilInterBranchTransfer;
import com.axe.machineEvents.MachineEvent;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.productTransactions.ProductTransaction;
import com.axe.suppliers.Supplier;
import com.axe.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "steel_coils")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SteelCoil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "coil_number")
    private String coilNumber;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "est_mtr_run_on_arrival")
    private BigDecimal estimatedMeterRunOnArrival;

    @Column(name = "weight_in_kgs_on_arrival")
    private BigDecimal weightInKgsOnArrival;

    @Column(name = "est_mtrs_remaining")
    private BigDecimal estimatedMetersRemaining;

    @Column(name = "landed_cost_per_mtr")
    private BigDecimal landedCostPerMtr;

    @Column(name = "landed_cost_per_kg")
    private BigDecimal landedCostPerKg;

    @Column(name = "conversion_ratio")
    private BigDecimal conversionRatio;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "steelCoil", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<MachineEvent> machineEvents = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "steel_specification_id")
    private SteelSpecification steelSpecification;

    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne()
    @JoinColumn(name = "consignor_id")
    private Consignor consignor;

    @OneToMany(mappedBy = "steelCoil", cascade = CascadeType.ALL)
    private List<ProductTransaction> productTransactions = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "grv_id")
    private GRV grv;

    @ManyToOne()
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "steelCoilTo",cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SteelCoilInterBranchTransfer> steelCoilInterBranchTransfer = new ArrayList<>();

    @OneToMany(mappedBy = "steelCoilFrom",cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SteelCoilInterBranchTransfer> steelCoilInterBranchTransferFrom = new ArrayList<>();

}
