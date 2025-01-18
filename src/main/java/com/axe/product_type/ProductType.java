package com.axe.product_type;

import com.axe.product.Product;
import com.axe.product_category.ProductCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "product_type")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private ProductCategory category;

    // Connect to products changed to a list
    @OneToMany(mappedBy = "productType")
    @JsonIgnore
    private Set<Product> product;
}
