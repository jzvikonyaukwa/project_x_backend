package com.axe.stockOnHand;

import com.axe.stockOnHand.DTO.MadeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StockOnHandRepository extends JpaRepository<StockOnHand, Long> {

    @Query(value = """
        SELECT
         soh.id as id, amp.length, soh.product_name as productName, amp.stick_type as stickType,
         date_picked as datePicked, soh.status as status, amp.stick_type as stickType, c.color as color,
         g.gauge as gauge, sc.id as steelCoilId, sc.coil_number as steelCoilNumber,
        pt.date as manufacturedDate
       FROM
        axe.stock_on_hand as soh
       LEFT JOIN
        axe.aggregated_products as amp ON amp.id = soh.aggregate_product_id
       JOIN
        axe.product_transactions as pt ON pt.id = amp.product_transaction_id
       JOIN
        axe.steel_coils as sc ON sc.id = pt.steel_coil_id
       JOIN
        axe.steel_specifications as ss ON ss.id = sc.steel_specification_id
       JOIN
        axe.colors as c ON c.id = ss.color_id
       JOIN
        axe.gauges as g ON g.id = ss.gauge_id
    """, nativeQuery = true)
    List<MadeProduct> getAllMadeProductsInStockOnHand();

    @Query(value = """
       SELECT soh
       FROM StockOnHand soh
       JOIN soh.aggregatedProduct ap
       JOIN ap.productTransaction pts
       JOIN pts.steelCoil sc
       JOIN sc.steelSpecification ss
       JOIN ss.color c
       JOIN ss.gauge g
       WHERE 
           soh.productName = :productName AND 
           c.color = :color AND 
           g.gauge = :gauge AND 
           ap.product IS NULL AND
           soh.status = :status
    """)
    List<StockOnHand> getStockOnHandForPlanName(
            @Param("productName") String productName,
            @Param("color") String color,
            @Param("gauge") BigDecimal gauge,
            @Param("status") String status);

    @Query("""
        SELECT soh FROM StockOnHand soh
        WHERE soh.status = 'available'
    """)
    List<StockOnHand> getAvailableAllStockOnHand();
}


