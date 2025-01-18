package com.axe.purchaseOrders;

import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.grvs.GRV;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.suppliers.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_orders")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "date_issued")
    private LocalDate dateIssued;

    @Column(name = "notes")
    private String notes;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "status")
    private String status;

    @ManyToOne()
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductsOnPurchaseOrder> productPurchases = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConsumablesOnPurchaseOrder> consumablesOnPurchaseOrders = new HashSet<>();

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GRV> grvs = new HashSet<>();

}
