package com.axe.gauges;

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
@Table(name = "gauges")
@Getter
@Setter
@NoArgsConstructor
public class Gauge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "gauge")
    private BigDecimal gauge;

    @OneToMany(mappedBy = "gauge", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SteelSpecification> steelSpecifications = new ArrayList<>();

    @OneToMany(mappedBy = "gauge", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

}
