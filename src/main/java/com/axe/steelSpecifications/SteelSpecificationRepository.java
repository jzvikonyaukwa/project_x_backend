package com.axe.steelSpecifications;

import com.axe.stocks.stocksDTO.StocksDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SteelSpecificationRepository extends JpaRepository<SteelSpecification, Long> {

    @Query(value= """
        SELECT
          ss.id AS productTypeId,
          color.color AS color,
          ss.ISQ_grade AS ISQGrade,
          ss.coating,
          w.width AS width,
          gauge.gauge,
          sc.id AS steelCoilId,
          sc.coil_id AS coildId,
          sc.est_mtr_run_on_arrival AS estMtrsOnArrival,
          sc.est_mtrs_remaining AS meterRemaining,
          sc.weight_in_kgs_on_arrival AS weightOnArrival,
          sc.landed_cost_per_mtr AS landedCostPerMtr,
          wh.name AS warehouse,
          sc.status AS status
          FROM
            axe.steel_specifications AS ss
          JOIN
            axe.colors AS color ON color.id = ss.color_id
          JOIN
            axe.gauges AS gauge ON gauge.id = ss.gauge_id
          JOIN
            axe.widths AS w ON w.id = ss.width_id
          JOIN
            axe.steel_coils AS sc ON sc.steel_specification_id = ss.id
          JOIN
            axe.warehouse AS wh ON wh.id = sc.warehouse_id
          WHERE
            sc.status='in-stock' AND wh.id = 1 ; ;""", nativeQuery = true)
    List<StocksDTO> getStockOnHand();

//    AND ROUND(ss.width, 3) = :width
    @Query(value= """
            SELECT * FROM axe.steel_specifications as ss
              WHERE ss.color_id = :colorId AND ss.ISQ_grade = :isqGrade
              AND ss.coating = :coating AND ss.gauge_id = :gaugeId AND ss.width_id = :widthId ;""", nativeQuery = true)
    Optional<SteelSpecification> findSteelSpecification(Long colorId, String isqGrade, Long widthId, String coating, Long gaugeId);

//    @Query(value= """
//    SELECT ss.id as steelSpecificationId, ss.ISQ_grade as isqGrade, ss.width, ss.coating, gauge.gauge,
//    color.color as color, finish.name as finish
//    FROM axe.suppliers as sup
//    JOIN axe.grvs as grv ON grv.supplier_id = sup.id
//    JOIN axe.steel_coils AS sc ON grv.id = sc.grv_id
//    JOIN axe.steel_specifications as ss ON ss.id = sc.steel_specification_id
//    JOIN axe.gauges as gauge ON gauge.id = ss.gauge_id
//    JOIN axe.colors as color ON color.id = ss.color_id
//    JOIN axe.finishes as finish ON finish.id = ss.finish_id
//    WHERE sup.id = :id ;""", nativeQuery = true)
//    List<SupplierProductsDTO> getAllProductTypesSupplierBySupplier(Long id);



}
