package com.axe.consumables;

import com.axe.consumables.DTOs.ConsumableLowStockData;
import com.axe.consumables.DTOs.InventoryBalanceHistory;
import com.axe.consumablesOnPurchaseOrder.dtos.ConsumablesOnPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ConsumablesRepository extends JpaRepository<Consumable, Long> {

    @Query(value = """
    SELECT c.id as consumableId, serial_number, c.name as name,
    pc.name as category, uom, po.id as purchaseOrderId, qty as qtyOnOrder,
    date_issued as dateIssued, expected_delivery_date as expectedDeliverDate, paid as isPaid, notes
    FROM axe.consumables as c
    JOIN axe.consumable_category as pc ON pc.id = c.consumable_category_id
    JOIN axe.consumable_on_purchase_order as copo ON copo.consumable_id = c.id
    JOIN axe.purchase_orders as po ON po.id=copo.purchase_order_id
    WHERE copo.grv_id IS NULL ;
""", nativeQuery = true)
    List<ConsumablesOnPurchaseOrder> getConsumablesOnOrder();

    @Query(value = """
       SELECT ciw.id as consumableInWarehouseId, qty, c.id as consumableId, c.name as name,\s
            c.serial_number as serialNumber, cc.name as category, c.uom, sc.country as sourceCountry,
            w.name as warehouseName
        FROM
           axe.consumables_in_warehouse AS ciw
        JOIN
           axe.consumables AS c ON c.id = ciw.consumable_id
        JOIN
           axe.warehouse AS w ON w.id = ciw.warehouse_id
        JOIN
           axe.consumable_category AS cc ON cc.id = c.consumable_category_id
        JOIN
           axe.source_country AS sc ON sc.id = c.source_country_id
        WHERE
           (ciw.warehouse_id = 1 AND ciw.qty <= c.min_qty_alert_owned) -- For Warehouse 1, use min_qty_alert_owned
        OR
           (ciw.warehouse_id = 2 AND ciw.qty <= c.min_qty_alert_consignment); -- For Warehouse 2, use min_qty_alert_consignment
    """, nativeQuery = true)
    List<ConsumableLowStockData> getLowStockConsumables();

//    @Query(value = """
//            SELECT\s
//                w.id as warehouseId, w.name as warehouseName,\s
//                  c.id as consumableId, c.serial_number as serialNumber, c.name, c.uom, \s
//                  cc.name as category, sc.country as sourceCountry, \s
//                  ciw.id as consumableInWarehouseId, qty, avg_landed_price as avgLandedPrice
//              FROM axe.consumables_in_warehouse as ciw
//              JOIN
//                axe.consumables AS c ON c.id = ciw.consumable_id
//              JOIN
//                axe.warehouse AS w ON w.id = ciw.warehouse_id
//              LEFT JOIN
//                axe.consumable_category AS cc ON cc.id = c.consumable_category_id
//              LEFT JOIN
//                axe.source_country AS sc ON sc.id = c.source_country_id
//            WHERE w.id = :warehouseId ;
//    """, nativeQuery = true)
//    List<ConsumableDetailsSQL> getAllConsumablesInWarehouse(Long warehouseId);

   @Query(value = """
SELECT
    transactionDate,
    SUM(transactionQuantity) OVER (
        PARTITION BY
            itemName
        ORDER BY
            transactionDate DESC,
            CASE transactionType
                WHEN 'BALANCE' THEN 0
                WHEN 'INVOICE' THEN 1
                WHEN 'GRV' THEN 2
            END,
            referenceID DESC ROWS UNBOUNDED PRECEDING
    ) AS stockBalance,
    transactionQuantity,
    transactionValue,
    unitCost,
    transactionType,
    referenceID,
    itemName
FROM
    (
        SELECT
            CURRENT_DATE() AS transactionDate,
            COALESCE(ciw.avg_landed_price, 0) AS unitCost,
            COALESCE(ciw.avg_landed_price * ciw.qty, 0) AS transactionValue,
            ciw.qty AS transactionQuantity,
            'BALANCE' AS transactionType,
            NULL AS referenceID,
            c.name AS itemName
        FROM
            consumables_in_warehouse ciw
            JOIN consumables c ON c.id = ciw.consumable_id
        WHERE
            ciw.warehouse_id = :warehouseID AND c.id = :consumableID
        UNION ALL
        SELECT
            i.date_invoiced AS transactionDate,
            COALESCE(coq.unit_price, 0) AS unitCost,
            COALESCE(coq.unit_price * coq.qty, 0) AS transactionValue,
            coq.qty AS transactionQuantity,
            'INVOICE' AS transactionType,
            i.id AS referenceID,
            c.name AS itemName
        FROM
            consumables_on_quote coq
            JOIN consumables c ON c.id = coq.consumables_id
            JOIN quotes q ON q.id = coq.quote_id
            JOIN sale_orders so ON so.quote_id = q.id
            JOIN invoices i ON i.sale_order_id = so.id
        WHERE
            c.id = :consumableID AND coq.invoice_id IS NOT NULL
            AND i.date_invoiced >= :startDate
        UNION ALL
        SELECT
            g.date_received AS transactionDate,
            COALESCE(cog.landed_price, 0) AS unitCost,
            COALESCE(cog.landed_price * (cog.qty * -1), 0) AS transactionValue,
            cog.qty * -1 AS transactionQuantity,
            'GRV' AS transactionType,
            g.id AS referenceID,
            c.name AS itemName
        FROM
            consumables_on_grv cog
            JOIN consumables_in_warehouse ciw ON ciw.id = cog.consumable_in_warehouse_id
            JOIN consumables c ON c.id = ciw.consumable_id
            JOIN grvs g ON g.id = cog.grv_id
            JOIN purchase_orders po ON po.id = g.purchase_order_id
         WHERE
            c.id = :consumableID
            AND g.date_received >= :startDate
    ) AS allTransactions
ORDER BY
    ISNULL (itemName) DESC,
    itemName ASC,
    transactionDate DESC,
    CASE transactionType
        WHEN 'BALANCE' THEN 0
        WHEN 'INVOICE' THEN 1
        WHEN 'GRV' THEN 2
    END,
    referenceID DESC
   """, nativeQuery = true)
   List<InventoryBalanceHistory> historyForConsumable(Long warehouseID, Long consumableID, LocalDate startDate);
 
}
