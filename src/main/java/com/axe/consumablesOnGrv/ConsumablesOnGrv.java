package com.axe.consumablesOnGrv;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.grvs.GRV;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "consumables_on_grv")
@Getter
@Setter
@NoArgsConstructor
public class ConsumablesOnGrv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="qty")
    private Integer qtyReceived;

    @Column(name="landed_price")
    private BigDecimal landedPrice;

    @ManyToOne()
    @JoinColumn(name = "grv_id")
    private GRV grv;

    @ManyToOne()
    @JoinColumn(name = "consumable_in_warehouse_id")
    private ConsumablesInWarehouse consumableInWarehouse;

//    @Column(name = "")
//    private Long consumableInWarehouseId;


}
