package com.axe.product;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.inventories.Inventory;
import com.axe.invoices.Invoice;
import com.axe.product_type.ProductType;
import com.axe.profile.Profile;
import com.axe.quotes.Quote;
import com.axe.width.Width;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String planName;

    @Column(name = "status")
    private String status;

    @Column(name = "date_work_began")
    private LocalDateTime dateWorkBegan;

    @Column(name = "date_work_completed")
    private LocalDateTime dateWorkCompleted;

    @Column(name = "last_worked_on")
    private LocalDateTime lastWorkedOn;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "priority", length = 50)
    private String priority;

    @Column(name = "can_invoice")
    private Boolean canInvoice;

    @ManyToOne()
    @JoinColumn(name = "quote_id")
    @JsonIgnore
    private Quote quote;

    @ManyToOne()
    @JoinColumn(name = "gauge_id")
    private Gauge gauge;

    @ManyToOne()
    @JoinColumn(name = "color_id")
    private Color color;

    @ManyToOne()
    @JoinColumn(name = "width_id")
    private Width width;

    @Column(name = "invoice_id", insertable = false, updatable = false)
    private Long invoiceId;

    @ManyToOne()
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne()
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "frame_name", length = 255)
    private String frameName;

    @Column(name = "frame_type", length = 255)
    private String frameType;

    @Column(name = "total_length", precision = 10, scale = 2)
    private BigDecimal totalLength;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inventory_id")
    @JsonIgnore
    private Inventory inventory;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = true)
    private ProductType productType;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AggregatedProduct> aggregatedProducts = new ArrayList<>();

    @Column(name = "cost_price")
    BigDecimal costPrice;

    @Column(name = "sell_price")
    BigDecimal sellPrice;

    public void addAggregatedProduct(AggregatedProduct aggregatedProduct) {
        this.aggregatedProducts.add(aggregatedProduct);
        aggregatedProduct.setProduct(this);
    }

    public void removeAggregatedProduct(AggregatedProduct aggregatedProduct) {
        this.aggregatedProducts.remove(aggregatedProduct);
        aggregatedProduct.setProduct(null);
    }

    
}
