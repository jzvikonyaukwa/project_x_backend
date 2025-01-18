package com.axe.productTransactions;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.missing_metres.MissingMetres;
import com.axe.steelCoils.SteelCoil;
import com.axe.wastage.Wastage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="date")
    private LocalDateTime date;

    @OneToOne(mappedBy = "productTransaction",cascade = CascadeType.ALL)
    private AggregatedProduct aggregatedProduct;

    @OneToOne(mappedBy = "productTransaction",cascade = CascadeType.ALL)
    private Wastage wastage;

    @OneToOne(mappedBy = "productTransaction",cascade = CascadeType.ALL)
    private MissingMetres missingMetres;

    @ManyToOne()
    @JoinColumn(name = "steel_coil_id")
    @JsonIgnore
    private SteelCoil steelCoil;

}