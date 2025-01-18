package com.axe.grvs;

import com.axe.grvs.grvsDTO.GRVWithDetailsSQLQuery;
import com.axe.grvs.grvsDTO.summaryDTO.ConsumableDetailsDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GRVsRepository extends JpaRepository<GRV, Long> //
, JpaSpecificationExecutor<GRV> {

    @Query(value = """
            SELECT
             grv.id as grvId, date_received as dateReceived, grv_comments as grvComments, supplier_grv_code as supplierGrvCode,
             steelCoilSupplier.id as steelCoilSupplierId, steelCoilSupplier.name as steelCoilSupplierName,
             sc.status, sc.id as steelCoilId, coil_number as coilNumber, card_number as cardNumber,
             est_mtr_run_on_arrival as estimatedMeterRunOnArrival, weight_in_kgs_on_arrival as weightInKgsOnArrival,
             est_mtrs_remaining as estimatedMetersRemaining, sc.landed_cost_per_mtr as landedCostPerMtr,
             ISQ_grade as isqGrade, width.width, color.color, finish.name as finish, coating, gauge.gauge,
             steelCoilWarehouse.id as steelCoilWarehouseId, steelCoilWarehouse.name as steelCoilWarehouse,
             c.id as consumableId, c.serial_number as serialNumber, c.name as consumableName,
             c.consumable_category_id as consumableCategoryId, c.uom, c.source_country_id as sourceId,
             consumableSupplier.id as consumableSupplierId, consumableSupplier.name as consumableSupplierName,
             consumableWarehouse.name as consumableWarehouse, consumableWarehouse.id as consumableWarehouseId, 
             cog.qty as qtyOrdered, cog.id as consumableOnGrvId, cog.landed_price as landedPrice, ciw.id as consumableInWarehouseId
              FROM axe.grvs as grv
              LEFT JOIN axe.steel_coils sc ON sc.grv_id = grv.id
              LEFT JOIN axe.consumables_on_grv as cog ON cog.grv_id = grv.id
              LEFT JOIN axe.consumables_in_warehouse as ciw ON ciw.id = cog.consumable_in_warehouse_id
              LEFT JOIN axe.warehouse as consumableWarehouse ON consumableWarehouse.id = ciw.warehouse_id
              LEFT JOIN axe.consumables as c ON c.id = ciw.consumable_id
              LEFT JOIN axe.consumable_on_purchase_order as copo ON c.id = copo.consumable_id
              LEFT JOIN axe.purchase_orders as po ON po.id = copo.purchase_order_id
              LEFT JOIN axe.suppliers as consumableSupplier ON consumableSupplier.id=po.supplier_id
              LEFT JOIN axe.suppliers as steelCoilSupplier ON steelCoilSupplier.id=sc.supplier_id
              LEFT JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
              LEFT JOIN axe.warehouse as steelCoilWarehouse ON steelCoilWarehouse.id = sc.warehouse_id
              LEFT JOIN axe.widths as width ON ss.width_id = width.id
              LEFT JOIN axe.colors as color ON ss.color_id = color.id
              LEFT JOIN axe.finishes as finish ON color.finishes_id = finish.id
              LEFT JOIN axe.gauges as gauge ON gauge.id = ss.gauge_id             
             WHERE grv.id = :id 
             and (
(po.id is null and sc.id is not null) 
or
(po.id is not null and grv.purchase_order_id = po.id)
)
        """, nativeQuery = true)
    List<GRVWithDetailsSQLQuery> getGRVWithDetails(Long id);

    @Query("SELECT grv FROM GRV grv")
    List<GRV> getAllGRVs();

    // TODO: ADD back-> WHERE grv.id != 1
    @Query(value = """
            SELECT
             grv.id as grvId, date_received as dateReceived, grv_comments as grvComments, supplier_grv_code as supplierGrvCode,
             steelCoilSupplier.id as steelCoilSupplierId, steelCoilSupplier.name as steelCoilSupplierName,
             sc.status, sc.id as steelCoilId, coil_number as coilNumber, card_number as cardNumber,
             est_mtr_run_on_arrival as estimatedMeterRunOnArrival, weight_in_kgs_on_arrival as weightInKgsOnArrival,
             est_mtrs_remaining as estimatedMetersRemaining, sc.landed_cost_per_mtr as landedCostPerMtr,
             ISQ_grade as isqGrade, width.width, color.color, finish.name as finish, coating, gauge.gauge,
             steelCoilWarehouse.id as steelCoilWarehouseId, steelCoilWarehouse.name as steelCoilWarehouse,
             c.id as consumableId, c.serial_number as serialNumber, c.name as consumableName,
             c.consumable_category_id as consumableCategoryId, c.uom, c.source_country_id as sourceId,
             consumableSupplier.id as consumableSupplierId, consumableSupplier.name as consumableSupplierName,
             consumableWarehouse.name as consumableWarehouse, consumableWarehouse.id as consumableWarehouseId, 
             cog.qty as qtyOrdered, cog.id as consumableOnGrvId, cog.landed_price as landedPrice, ciw.id as consumableInWarehouseId
              FROM axe.grvs as grv
              LEFT JOIN axe.consumables_on_grv as cog ON cog.grv_id = grv.id
              LEFT JOIN axe.consumables_in_warehouse as ciw ON ciw.id = cog.consumable_in_warehouse_id
              LEFT JOIN axe.warehouse as consumableWarehouse ON consumableWarehouse.id = ciw.warehouse_id
              LEFT JOIN axe.consumables as c ON c.id = ciw.consumable_id
              LEFT JOIN axe.consumable_on_purchase_order as copo ON c.id = copo.consumable_id
              LEFT JOIN axe.purchase_orders as po ON po.id = copo.purchase_order_id
              LEFT JOIN axe.suppliers as consumableSupplier ON consumableSupplier.id=po.supplier_id
              LEFT JOIN axe.steel_coils as sc ON grv.id = sc.grv_id
              LEFT JOIN axe.suppliers as steelCoilSupplier ON steelCoilSupplier.id=sc.supplier_id
              LEFT JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
              LEFT JOIN axe.warehouse as steelCoilWarehouse ON steelCoilWarehouse.id = sc.warehouse_id
              LEFT JOIN axe.widths as width ON ss.width_id = width.id
              LEFT JOIN axe.colors as color ON ss.color_id = color.id
              LEFT JOIN axe.finishes as finish ON color.finishes_id = finish.id
              LEFT JOIN axe.gauges as gauge ON gauge.id = ss.gauge_id
              WHERE grv.purchase_order_id = :id ;
        """, nativeQuery = true)
    List<GRVWithDetailsSQLQuery> getAllGRVForPO(Long id);


    @Query("""
        SELECT new com.axe.grvs.grvsDTO.summaryDTO.ConsumableDetailsDTO(
            c.name,
            COUNT(c.id)
        )
        FROM GRV grv
        JOIN grv.consumablesOnGrv cog ON cog.grv.id = grv.id
        JOIN ConsumablesInWarehouse ciw ON ciw.id = cog.consumableInWarehouse.id
        JOIN ciw.consumable c
        WHERE grv.dateReceived BETWEEN :startDate AND :endDate
        GROUP BY c.name, ciw.warehouse.name
        ORDER BY c.name DESC
        """)
    List<ConsumableDetailsDTO> findConsumableDetailsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
        SELECT
            TRIM(CONCAT(COALESCE(width.width, ''), ' ', COALESCE(color.color, ''), ' ', COALESCE(gauge.gauge, ''))) as specification,
            COUNT(sc.id) as count
        FROM grvs as grv
        JOIN steel_coils as sc ON grv.id = sc.grv_id
        JOIN steel_specifications as ss ON sc.steel_specification_id = ss.id
        JOIN widths as width ON ss.width_id = width.id
        JOIN colors as color ON ss.color_id = color.id
        JOIN gauges as gauge ON gauge.id = ss.gauge_id
        WHERE grv.date_received BETWEEN :startDate AND :endDate
        GROUP BY specification
        ORDER BY count DESC;
        """, nativeQuery = true)
    List<Object[]> getSteelCoilSummaryDetails(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
        SELECT MONTH(grv.date_received) as month, COUNT(sc.id) as steelCoilsCount
        FROM axe.grvs as grv
        JOIN axe.steel_coils as sc ON grv.id = sc.grv_id
        WHERE YEAR(grv.date_received) = :year
        GROUP BY MONTH(grv.date_received)
        ORDER BY MONTH(grv.date_received);
        """, nativeQuery = true)
    List<Object[]> getMonthlySteelCoilCount(int year);

    @Query(value = """
        SELECT MONTH(grv.date_received) as month, COUNT(cog.id) as consumablesCount
        FROM axe.grvs as grv
        JOIN axe.consumables_on_grv as cog ON grv.id = cog.grv_id
        WHERE YEAR(grv.date_received) = :year
        GROUP BY MONTH(grv.date_received)
        ORDER BY MONTH(grv.date_received);
        """, nativeQuery = true)
    List<Object[]> getMonthlyConsumableCount(int year);










}
