package com.axe.warehouse;

import com.axe.stocks.stocksDTO.SteelCoilsInStock;
import com.axe.stocks.stocksDTO.StocksDTO;
import com.axe.suppliersPhones.SupplierPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(value= """
            SELECT\s
           ss.id AS productTypeId,
            color.color AS color,
            ss.ISQ_grade AS ISQGrade,
            ss.coating,
            w.width AS width,
            gauge.gauge,
            sc.id AS steelCoilId,
            sc.coil_number AS coilNumber,
            sc.card_number AS cardNumber,
            sc.est_mtr_run_on_arrival AS estMtrsOnArrival,
            sc.est_mtrs_remaining AS meterRemaining,
            sc.weight_in_kgs_on_arrival AS weightOnArrival,
            sc.landed_cost_per_mtr AS landedCostPerMtr,
            wh.name AS warehouse,
            s.name as supplier,
            sc.status AS status,
            grv.date_received as dateReceived,
            consignor.name as consignor
           FROM axe.steel_coils AS sc\s
           JOIN
            axe.steel_specifications AS ss ON sc.steel_specification_id = ss.id
           JOIN
            axe.colors AS color ON color.id = ss.color_id
           JOIN
            axe.gauges AS gauge ON gauge.id = ss.gauge_id
           JOIN
            axe.widths AS w ON w.id = ss.width_id
           LEFT JOIN
            axe.grvs AS grv ON grv.id = sc.grv_id
           JOIN
            axe.suppliers as s ON sc.supplier_id = s.id
           JOIN
            axe.warehouse AS wh ON wh.id = sc.warehouse_id
            LEFT JOIN
                axe.consignors as consignor ON consignor.id = sc.consignor_id
            WHERE wh.id = :warehouseId ;
             """, nativeQuery = true)
    List<StocksDTO> getWarehouseStock(Long warehouseId);

    @Query( value = """
     SELECT\s
          	ISQ_grade as ISQgrade, coating,
          	g.gauge,\s
              c.color,
              w.width,
          	sc.id as steelCoilId, coil_number as axeCoilId, card_number as cardNumber, est_mtrs_remaining as estMtrsRemaining, landed_cost_per_mtr as landedCostPerMtr, status,
              wh.name as warehouse
          FROM axe.steel_specifications as ss
          JOIN axe.gauges as g ON g.id = ss.gauge_id
          JOIN axe.colors as c ON ss.color_id = c.id
          JOIN axe.widths as w ON w.id = ss.width_id
          JOIN axe.steel_coils as sc ON sc.steel_specification_id = ss.id
          JOIN axe.warehouse as wh ON wh.id = sc.warehouse_id;
    """, nativeQuery = true)
    List<SteelCoilsInStock> getAllStock();

    @Query("SELECT w FROM Warehouse w WHERE w.name = ?1")
    Warehouse getWarehouseByName(String warehouseName);

    @Query(value = """
        SELECT\s
            ss.id AS productTypeId,
            color.color AS color,
            ss.ISQ_grade AS ISQGrade,
            ss.coating,
            w.width AS width,
            gauge.gauge,
            sc.id AS steelCoilId,
            sc.coil_number AS coilNumber,
            sc.card_number AS cardNumber,
            sc.est_mtr_run_on_arrival AS estMtrsOnArrival,
            sc.est_mtrs_remaining AS meterRemaining,
            sc.weight_in_kgs_on_arrival AS weightOnArrival,
            sc.landed_cost_per_mtr AS landedCostPerMtr,
            wh.name AS warehouse,
            s.name AS supplier,
            sc.status AS status,
            grv.date_received AS dateReceived,
            consignor.name AS consignor
        FROM axe.steel_coils AS sc
        JOIN axe.steel_specifications AS ss ON sc.steel_specification_id = ss.id
        JOIN axe.colors AS color ON color.id = ss.color_id
        JOIN axe.gauges AS gauge ON gauge.id = ss.gauge_id
        JOIN axe.widths AS w ON w.id = ss.width_id
        LEFT JOIN axe.grvs AS grv ON grv.id = sc.grv_id
        JOIN axe.suppliers AS s ON sc.supplier_id = s.id
        JOIN axe.warehouse AS wh ON wh.id = sc.warehouse_id
        LEFT JOIN axe.consignors AS consignor ON consignor.id = sc.consignor_id
        WHERE wh.id = :warehouseId AND w.width = :width ;
    """, nativeQuery = true)
//    AND sc.est_mtrs_remaining > 0
    List<StocksDTO> getFilteredWarehouseStock(@Param("warehouseId") Long warehouseId, @Param("width") Float width);
}
