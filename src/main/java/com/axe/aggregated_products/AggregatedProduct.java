package com.axe.aggregated_products;

import com.axe.product.Product;
import com.axe.productTransactions.ProductTransaction;
import com.axe.stockOnHand.StockOnHand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "aggregated_products")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AggregatedProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stick")
    private String stick;

    @Column(name = "stick_type")
    private String stickType;

    @Column(name = "code")
    private String code;

    @Column(name = "length")
    private BigDecimal length;

    @Column(name = "status")
    private String status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_transaction_id")
    @JsonIgnore
    private ProductTransaction productTransaction;

    @OneToOne(mappedBy = "aggregatedProduct", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private StockOnHand stockOnHand;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
}
