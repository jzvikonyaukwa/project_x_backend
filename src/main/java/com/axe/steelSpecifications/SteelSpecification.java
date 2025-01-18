package com.axe.steelSpecifications;

import com.axe.colors.Color;
import com.axe.finishes.Finish;
import com.axe.gauges.Gauge;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.steelCoils.SteelCoil;
import com.axe.width.Width;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "steel_specifications")
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class SteelSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ISQ_grade")
    private String ISQGrade;

    @Column(name = "coating")
    private String coating;

    @ManyToOne()
    @JoinColumn(name = "gauge_id")
    private Gauge gauge;

    @ManyToOne()
    @JoinColumn(name = "width_id")
    private Width width;

    @ManyToOne()
    @JoinColumn(name = "color_id")
    private Color color;

    @OneToMany(mappedBy = "steelSpecification", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SteelCoil> steelCoils = new ArrayList<>();

    @OneToMany(mappedBy = "steelSpecification")
    @JsonIgnore
    private Set<ProductsOnPurchaseOrder> productsOnPurchaseOrders = new HashSet<>();
}
