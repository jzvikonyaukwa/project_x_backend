package com.axe.consignor;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.steelCoils.SteelCoil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consignors")
@Getter
@Setter
@NoArgsConstructor
public class Consignor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "consignor", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<SteelCoil> steelCoils = new ArrayList<>();

    @OneToMany(mappedBy = "consignor", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ConsumablesInWarehouse> consumablesInWarehouses = new ArrayList<>();
}
