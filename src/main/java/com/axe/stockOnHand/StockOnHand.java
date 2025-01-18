package com.axe.stockOnHand;

import com.axe.aggregated_products.AggregatedProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_on_hand")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StockOnHand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "date_picked")
    private LocalDate datePicked;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "aggregate_product_id")
    private AggregatedProduct aggregatedProduct;

}
