package com.axe.steelCoils;

import com.axe.saleOrder.models.TotalMtrsRemaingForEachCategoryDTO;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilTransactionInformation;
import com.axe.stocks.stocksDTO.StockMovementData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SteelCoilRepository extends JpaRepository<SteelCoil, Long>, PagingAndSortingRepository<SteelCoil, Long> {

    @Query(value = """
            SELECT COALESCE(SUM(sc.est_mtrs_remaining),0) AS total_mtrs_remaining
            FROM axe.steel_coils AS sc
            JOIN axe.steel_specifications AS ss ON sc.steel_specification_id = ss.id
            JOIN axe.colors AS c ON c.id = ss.color_id
            JOIN axe.gauges AS g ON g.id = ss.gauge_id
            JOIN axe.widths AS w ON w.id = ss.width_id
            WHERE
                sc.status != 'finished'
            AND
                sc.warehouse_id = :warehouseId
            AND
                c.color = :color
            AND
                g.gauge = :gauge
            AND
                w.width = :width ;
                                                 \s""", nativeQuery = true)
    BigDecimal isThereEnoughStock(Long warehouseId, String color, BigDecimal gauge, BigDecimal width);

    @Query(value = """
       SELECT AVG(landed_cost_per_mtr) AS landedCostPerMtr
       FROM steel_coils as sc
       JOIN steel_specifications as ss ON ss.id = sc.steel_specification_id
       WHERE ss.gauge_id = :gaugeId AND ss.color_id = :colorId AND ss.width_id = :widthId""", nativeQuery = true)
    Float getAvgPriceForSteelType(Long colorId, Long gaugeId, Long widthId);

    @Query(value = """
    WITH AvailableCoils AS (
        -- Calculate available length and value for each matching coil
        -- Uses COALESCE to handle NULL values, preventing calculation errors
        SELECT
            COALESCE(coil.est_mtrs_remaining, 0) AS availableCoilLength,
            COALESCE(coil.landed_cost_per_mtr, 0) * COALESCE(coil.est_mtrs_remaining, 0) AS availableCoilValue
        FROM steel_coils AS coil
        LEFT JOIN steel_specifications AS specifications 
            ON specifications.id = coil.steel_specification_id
        WHERE (
            -- Filter by exact specification match
            specifications.gauge_id = :gaugeId
            AND specifications.color_id = :colorId
            AND specifications.width_id = :widthId
        )
    )
    -- Calculate the weighted average cost per meter across all matching coils
    -- All calculations use COALESCE to ensure non-NULL results
    SELECT
        -- COALESCE(SUM(coilStock.availableCoilValue), 0) AS totalCoilValue,
        -- COALESCE(SUM(coilStock.availableCoilLength), 0) AS totalCoilLength,
        COALESCE(
            (SUM(coilStock.availableCoilValue) / 
            NULLIF(SUM(coilStock.availableCoilLength), 0)  -- Prevent division by zero
            ),
            0
        ) AS weightedAverageCostPerMeter  -- Cost per meter of coil material
    FROM AvailableCoils AS coilStock""", nativeQuery = true)
    BigDecimal calculateWeightedAvgCostForSteelType(Long colorId, Long gaugeId, Long widthId);

    @Query(value = """
       SELECT EXISTS(SELECT * FROM axe.steel_coils as sc
       JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
       JOIN axe.colors as c ON c.id = ss.color_id
       JOIN axe.gauges as g ON g.id = ss.gauge_id
       WHERE sc.warehouse_id  = :warehouseId AND c.id = :colorId AND g.id = :gaugeId AND sc.est_mtrs_remaining >= :requiredMtrs )\s""", nativeQuery = true)
    Long isThereEnoughStockForSheets(Long warehouseId, Long colorId, Long gaugeId, BigDecimal requiredMtrs);

    //TODO: N+1 query
    @Query(value = """

            SELECT DISTINCT sc.id                     AS steelCoilId,
                coil_number               AS coilNumber,
                card_number               AS cardNumber,
                sc.est_mtr_run_on_arrival AS estMtrsOnArrival,
                est_mtrs_remaining        AS estMtrsRemaining,
                isq_grade                 AS IsqGrade,
                w.width,
                wh.NAME                   AS warehouse,
                finish.NAME               AS finish,
                color.color,
                color.css_color           AS cssColor,
                coating,
                gauge.gauge,
                s.NAME                    AS supplierName,
                sc.status -- Added this line
FROM   axe.steel_coils AS sc
       JOIN axe.steel_specifications AS ss
         ON sc.steel_specification_id = ss.id
       LEFT JOIN axe.warehouse AS wh
              ON wh.id = sc.warehouse_id
       LEFT JOIN axe.suppliers AS s
              ON s.id = sc.supplier_id
       LEFT JOIN axe.colors AS color
              ON ss.color_id = color.id
       JOIN axe.finishes AS finish
         ON finish.id = color.finishes_id
       JOIN axe.gauges AS gauge
         ON gauge.id = ss.gauge_id
       JOIN axe.widths AS w
         ON w.id = ss.width_id
WHERE  wh.id = 1
       AND ( sc.status = 'in-use' )
            AND  w.width = :width
            ;
""", nativeQuery = true)
    List<SteelCoilDetailsDTO> getAvailableSteelCoilsForMachine(BigDecimal width);

    //TODO: N+1 query
    @Query(value = """
SELECT DISTINCT
    sc.id AS steelCoilId,
    coil_number AS coilNumber,
    card_number AS cardNumber,
    sc.est_mtr_run_on_arrival AS estMtrsOnArrival,
    est_mtrs_remaining AS estMtrsRemaining,
    ISQ_grade AS IsqGrade,
    w.width,
    wh.name AS warehouse,
    finish.name AS finish,
    color.color,
    color.css_color AS cssColor,
    coating,
    gauge.gauge,
    s.name AS supplierName,
    sc.status
FROM
    axe.steel_coils AS sc
JOIN
    axe.steel_specifications AS ss ON sc.steel_specification_id = ss.id
LEFT JOIN
    axe.warehouse AS wh ON wh.id = sc.warehouse_id
LEFT JOIN
    axe.suppliers AS s ON s.id = sc.supplier_id
LEFT JOIN
    axe.colors AS color ON ss.color_id = color.id
JOIN
    axe.finishes AS finish ON finish.id = color.finishes_id
JOIN
    axe.gauges AS gauge ON gauge.id = ss.gauge_id
JOIN
    axe.widths AS w ON w.id = ss.width_id
WHERE
    wh.id = 1
    AND (sc.status = 'in-stock' OR sc.status = 'in-use')
    AND (w.width = :width)
    AND (:gauge IS NULL OR gauge.gauge = :gauge)
    AND (:color IS NULL OR color.color = :color)
ORDER BY
    (sc.status = 'in-use') DESC, sc.id;


  """, nativeQuery = true)
    List<SteelCoilDetailsDTO> getFilteredAvailableSteelCoils (BigDecimal width,Double gauge, String color);

    @Query(value = """
            SELECT DISTINCT sc.id as steelCoilId, sc.coil_number as coilNumber, card_number as cardNumber,
             sc.est_mtr_run_on_arrival as estMtrsOnArrival, sc.weight_in_kgs_on_arrival as weightInKgsOnArrival,
             est_mtrs_remaining as estMtrsRemaining,ISQ_grade as IsqGrade, w.width, wh.name as warehouse,
             finish.name as finish, color.color, color.css_color as cssColor,
             coating, gauge.gauge, s.name as supplierName, sc.landed_cost_per_mtr as landedCostPerMtr,
             consignor.name as consignor, grv.id as grvId, grv.date_received as dateReceived
             FROM axe.steel_coils as sc
             JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
             JOIN axe.warehouse as wh ON wh.id = sc.warehouse_id
             LEFT JOIN axe.grvs as grv ON sc.grv_id = grv.id
             LEFT JOIN axe.purchase_orders as po ON po.id = grv.purchase_order_id
             LEFT JOIN axe.suppliers as s on s.id = po.supplier_id
             LEFT JOIN axe.colors as color ON ss.color_id = color.id
             JOIN axe.finishes as finish ON finish.id=color.finishes_id
             JOIN axe.gauges as gauge ON gauge.id = ss.gauge_id
             JOIN axe.widths as w ON w.id = ss.width_id
             LEFT JOIN axe.consignors as consignor ON consignor.id = sc.consignor_id
             WHERE sc.id=:steelCoilId""", nativeQuery = true)
    SteelCoilDetailsDTO getSteelCoilDetails(Long steelCoilId);

    @Query(value = """
            SELECT pt.id as productTransactionId, date,\s
              soh.id as stockOnHandId, soh.length as stockOnHandLength,
              ap.id as manufacturedProductId, ap.length as manufacturedProductLength,\s
              w.id as wastageId, w.mtrs_waste as wastageLength,
              pr.plan_name as planName, pr.frame_type as frameType,
              ap.code as code, pr.frame_name as frameName, c.name as client, q.id as quoteId
              FROM axe.steel_coils as sc
              JOIN axe.product_transactions as pt ON pt.steel_coil_id = sc.id
              LEFT JOIN axe.stock_on_hand as soh ON soh.product_transaction_id = pt.id
              LEFT JOIN axe.aggregated_products as ap ON ap.product_transaction_id = pt.id
              LEFT JOIN axe.products as pr ON pr.id = ap.product_id
              LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
              LEFT JOIN axe.quotes as q on pr.quote_id = q.id
              LEFT JOIN axe.projects as p on p.id = q.project_id
              LEFT JOIN axe.clients as c ON c.id = p.client_id
              WHERE sc.id = :steelCoilId
        """, nativeQuery = true)
    List<SteelCoilTransactionInformation> getSteelCoilTransactions(Long steelCoilId);


    // TODO: check this
    @Query(value = """
             SELECT
                COALESCE(SUM(p.total_length), 0) AS total_length
            FROM
                quotes q
                LEFT JOIN products p ON q.id = p.quote_id
                LEFT JOIN axe.widths w ON w.id = p.width_id
                LEFT JOIN axe.gauges as g on g.id = p.gauge_id
                LEFT JOIN axe.colors as c on c.id = p.color_id
                LEFT JOIN aggregated_products ap ON p.id = ap.product_id
            WHERE
                q.status = 'accepted'
                AND p.status = 'scheduled'
                AND g.gauge = :gauge
                AND c.color = :color
                AND w.width = :width ;
            """, nativeQuery = true)
    BigDecimal getTotalMtrsForAcceptedCuttingLists(BigDecimal gauge, String color, BigDecimal width);

    @Query(value = """

        SELECT w.width, g.gauge, c.color, SUM(sc.est_mtrs_remaining) AS totalMtrsRemaining
           FROM
           axe.steel_specifications as ss
           JOIN
           axe.steel_coils as sc ON sc.steel_specification_id = ss.id
           JOIN
           axe.gauges as g ON ss.gauge_id = g.id
           JOIN
           axe.colors as c ON ss.color_id = c.id
           JOIN
           axe.widths as w ON ss.width_id = w.id
           GROUP BY
           ss.gauge_id, ss.color_id, ss.width_id;
    """, nativeQuery = true)
    List<TotalMtrsRemaingForEachCategoryDTO> getAllStockLevels();

    @Query(value = """
       SELECT
            SUM(sc.est_mtrs_remaining) AS totalMtrsRemaining
         FROM
            axe.steel_specifications as ss
         JOIN
            axe.steel_coils as sc ON sc.steel_specification_id = ss.id
         JOIN
            axe.gauges as g ON ss.gauge_id = g.id
         JOIN
            axe.colors as c ON ss.color_id = c.id
         JOIN
            axe.widths as w ON ss.width_id = w.id
         WHERE
            g.gauge = :gauge AND c.color = :color AND w.width = :width
         GROUP BY
            ss.gauge_id, ss.color_id, ss.width_id;
    """ , nativeQuery = true)
    BigDecimal getStockLevelsForSteelType(BigDecimal width, BigDecimal gauge, String color);

    @Query(value = """
            SELECT * FROM axe.steel_coils as sc
            WHERE sc.coil_number = :coilId
            """, nativeQuery = true)
    SteelCoil getSteelCoilByCoilId(String coilId);


    @Query(value = """
            SELECT card_number
               FROM axe.steel_coils as sc
               JOIN axe.steel_specifications as ss ON ss.id = sc.steel_specification_id
               JOIN axe.widths as w ON w.id = ss.width_id
               WHERE w.width = :width
               ORDER BY sc.id DESC
               LIMIT 1;
            """, nativeQuery = true)
    String getLastCardNumberForSteelCategory(BigDecimal width);

    @Query(value = """
            SELECT sc_to.coil_number as coilNumber, sc_to.card_number as cardNumber, sc_to.est_mtr_run_on_arrival as meters,
               sc_to.weight_in_kgs_on_arrival as weight, sc_to.landed_cost_per_mtr as landedCost, s.name as supplier, date as consignmentTransferDate,
               sc_to.grv_id as grvId, sc_ibt.id as interBranchTransferId, w.name as warehouse, sc_from.coil_number as coilNumberFrom, sc_from.card_number as cardNumberFrom
               FROM axe.steel_coil_inter_branch_transfer  as sc_ibt
               LEFT JOIN  axe.steel_coils as sc_to ON sc_ibt.steel_coil_id_to = sc_to.id
               LEFT JOIN axe.steel_coils AS sc_from ON sc_from.id = sc_ibt.steel_coil_id_from
               JOIN axe.suppliers as s ON s.id = sc_to.supplier_id
               JOIN axe.warehouse as w on w.id = sc_to.warehouse_id
            """, nativeQuery = true)
    List<StockMovementData> getStockMovement();

    @Query(value = """
            SELECT * FROM axe.steel_coils as sc
            WHERE sc.warehouse_id = :id AND sc.coil_number = :steelCoilNumber
            """, nativeQuery = true)
    Optional<SteelCoil> getSteelCoilByCoilNumberInWarehouse(Long id, String steelCoilNumber);


    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM axe.steel_coils AS sc
        JOIN axe.steel_specifications AS ss ON sc.steel_specification_id = ss.id
        JOIN axe.colors AS c ON ss.color_id = c.id
        JOIN axe.gauges AS g ON ss.gauge_id = g.id
        WHERE sc.status = 'in-use'
          AND g.gauge = :gauge
          AND c.color = :color
          AND ss.coating = :coating
    )
    """, nativeQuery = true)
    int searchForSteelCoilUsingGaugeColorAndCoating(
            @Param("gauge") BigDecimal gauge,
            @Param("color") String color,
            @Param("coating") String coating);


    @Query("""
    SELECT sc.landedCostPerMtr
    FROM SteelCoil sc
    WHERE sc.steelSpecification.color.id = :colorId
      AND sc.steelSpecification.gauge.id = :gaugeId
      AND sc.steelSpecification.width.id = :widthId
    ORDER BY sc.steelSpecification.id DESC
""")
    Optional<BigDecimal> findLatestLandedCostPerMtr(@Param("colorId") Long colorId,
                                                    @Param("gaugeId") Long gaugeId,
                                                    @Param("widthId") Long widthId);


}
