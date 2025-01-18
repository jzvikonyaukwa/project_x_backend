package com.axe.colors;

import com.axe.finishes.Finish;
import com.axe.product.Product;
import com.axe.steelSpecifications.SteelSpecification;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "color")
    private String color;

    @Column(name = "css_color")
    private String cssColor;

    @ManyToOne()
    @JoinColumn(name = "finishes_id")
    private Finish finish;

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<SteelSpecification> steelSpecifications = new ArrayList<>();

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

//    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<OutsourcedProduct> outsourcedProducts = new ArrayList<>();

}
