package com.axe.wastage;



import com.axe.product.Product;
import com.axe.productTransactions.ProductTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "wastage")
@Getter
@Setter
@NoArgsConstructor
public class Wastage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mtrs_waste")
    private BigDecimal mtrsWaste;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_transaction_id")
    @JsonIgnore
    private ProductTransaction productTransaction;

//    @ManyToOne()
//    @JoinColumn(name = "product_id")
//    @JsonIgnore
//    private Product product;
}
