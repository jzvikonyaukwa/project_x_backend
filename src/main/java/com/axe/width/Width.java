package com.axe.width;


import com.axe.product.Product;
import com.axe.steelSpecifications.SteelSpecification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "widths")
@Getter
@Setter
@NoArgsConstructor
public class Width {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "width")
    private BigDecimal width;

//    @OneToMany(mappedBy = "width", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<OutsourcedProduct> outsourcedProducts = new ArrayList<>();

    @OneToMany(mappedBy = "width", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "width", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SteelSpecification> steelSpecifications = new ArrayList<>();
}
