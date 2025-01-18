package com.axe.consignor;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsignorRepository extends JpaRepository<Consignor, Long> {

    @Query(value = """
        SELECT DISTINCT sc.id as steelCoilId, coil_number as coilNumber, card_number as cardNumber,
        sc.est_mtr_run_on_arrival as estMtrsOnArrival, est_mtrs_remaining as estMtrsRemaining,
        sc.weight_in_kgs_on_arrival as weightInKgsOnArrival, sc.landed_cost_per_mtr as landedCostPerMtr,
        ISQ_grade as IsqGrade, w.width, wh.name as warehouse,
        finish.name as finish, color.color, color.css_color as cssColor, coating, gauge.gauge,
        s.name as supplierName, consignor.name as consignor
        FROM axe.steel_coils as sc
        JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
        LEFT JOIN axe.warehouse as wh ON wh.id = sc.warehouse_id
        LEFT JOIN axe.suppliers as s on s.id = sc.supplier_id
        LEFT JOIN axe.colors as color ON ss.color_id = color.id
        LEFT JOIN axe.consignors as consignor ON consignor.id = sc.consignor_id
        JOIN axe.finishes as finish ON finish.id=color.finishes_id
        JOIN axe.gauges as gauge ON gauge.id = ss.gauge_id
        JOIN axe.widths as w ON w.id = ss.width_id
         WHERE consignor.id = :consignorId
    """, nativeQuery = true)
    List<SteelCoilDetailsDTO> getConsignorSteel(Long consignorId);

    @Query("SELECT c FROM ConsumablesInWarehouse c WHERE c.consignor.id = :consignorId ORDER BY c.consumable.name")
    List<ConsumablesInWarehouse> getConsignorConsumables(Long consignorId);
}
