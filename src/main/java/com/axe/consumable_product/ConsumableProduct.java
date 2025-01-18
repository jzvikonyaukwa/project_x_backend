package com.axe.consumable_product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consumable_product")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ConsumableProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String serialNumber;

    @Column(nullable = false)
    private String unit;
}
