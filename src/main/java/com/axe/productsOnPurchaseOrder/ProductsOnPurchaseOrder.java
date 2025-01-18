package com.axe.productsOnPurchaseOrder;

import com.axe.grvs.GRV;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.purchaseOrders.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products_on_purchase_order")
@Getter
@Setter
@NoArgsConstructor
public class ProductsOnPurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "weight_ordered")
    private BigDecimal weightOrdered;

    @Column(name = "purchase_cost_per_kg")
    private BigDecimal purchaseCostPerKg;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "steel_specification_id")
    private SteelSpecification steelSpecification;

}
