package com.axe.grvs;

import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.steelCoils.SteelCoil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grvs")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"steelCoils", "consumablesOnPurchaseOrders", "consumablesOnGrv", "purchaseOrder"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "steelCoils", "consumablesOnPurchaseOrders", "consumablesOnGrv", "purchaseOrder"})
public class GRV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_received")
    private LocalDate dateReceived;

    @Column(name = "grv_comments")
    private String comments;

    @Column(name = "supplier_grv_code")
    private String supplierGRVCode;

    @OneToMany(mappedBy = "grv", cascade = CascadeType.ALL)
    private List<SteelCoil> steelCoils = new ArrayList<>();

    @OneToMany(mappedBy = "grv", cascade = CascadeType.ALL)
    private List<ConsumablesOnPurchaseOrder> consumablesOnPurchaseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "grv", cascade = CascadeType.ALL)
    private List<ConsumablesOnGrv> consumablesOnGrv = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
}
