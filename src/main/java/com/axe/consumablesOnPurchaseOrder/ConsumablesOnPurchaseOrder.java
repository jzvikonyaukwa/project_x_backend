package com.axe.consumablesOnPurchaseOrder;

import com.axe.consumables.Consumable;
import com.axe.grvs.GRV;
import com.axe.purchaseOrders.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Table(name = "consumable_on_purchase_order")
@Getter
@Setter
@NoArgsConstructor
public class ConsumablesOnPurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "status")
    private String status;

    @Column(name = "cost_per_unit")
    private BigDecimal costPerUnit;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "consumable_id")
    private Consumable consumable;

    @ManyToOne
    @JoinColumn(name = "grv_id")
    private GRV grv;

}
