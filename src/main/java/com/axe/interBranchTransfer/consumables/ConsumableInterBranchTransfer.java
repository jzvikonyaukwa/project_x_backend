package com.axe.interBranchTransfer.consumables;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumable_inter_branch_transfer")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ConsumableInterBranchTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "qty")
    private Integer qty;

    @ManyToOne()
    @JoinColumn(name = "consumable_in_warehouse_id_from")
    private ConsumablesInWarehouse consumableInWarehouseFrom;

    @ManyToOne()
    @JoinColumn(name = "consumable_in_warehouse_id_to")
    private ConsumablesInWarehouse consumableInWarehouseTo;
}
