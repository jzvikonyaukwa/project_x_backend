package com.axe.product_type_consumable;

import com.axe.consumable_product.ConsumableProduct;
import com.axe.product_type.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_type_consumable")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductTypeConsumable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Todo:: Redesign this ProductTypeConsumable
//    @ManyToOne
//    @JoinColumn(name = "product_type_id", nullable = false)
//    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "consumable_product_id", nullable = false)
    private ConsumableProduct consumableProduct;

    @Column(nullable = false)
    private BigDecimal quantity;

}
