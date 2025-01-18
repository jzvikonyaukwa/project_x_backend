package com.axe.warehouse;

import com.axe.consumables.Consumable;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.steelCoils.SteelCoil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouse")
@Getter
@Setter
@NoArgsConstructor
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "warehouse")
    private List<SteelCoil> steelCoils = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ConsumablesInWarehouse> consumablesInWarehouses = new ArrayList<>();
}
