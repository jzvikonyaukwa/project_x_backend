package com.axe.purchaseOrders;


import com.axe.purchaseOrders.DTOs.StockOnOrderDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> , JpaSpecificationExecutor<PurchaseOrder> {

    @Query("SELECT p FROM PurchaseOrder p ")
    List<PurchaseOrder> findAllByStatusOrdered();

    @Query("SELECT p FROM PurchaseOrder p WHERE p.status = 'pending' AND p.supplier.id = :supplierId")
    List<PurchaseOrder> getAllSuppliersPurchaseOrdersOnOrder(Long supplierId);


    @Query(value= """
        SELECT\s
        po.id as purchaseOrderId, po.expected_delivery_date as expectedDeliveryDate, po.date_issued as dateOrdered,
           s.id as supplierId, s.name as supplierName, popo.id as productOnPurchaseOrderId, popo.grv_id,
        popo.weight_ordered as weightOrdered, ss.id as steelSpecificationId,
           ss.ISQ_grade as isqGrade, w.width as width, ss.coating, c.color as color, g.gauge as gauge
       FROM\s
        axe.purchase_orders as po
       JOIN axe.products_on_purchase_order as popo ON popo.purchase_order_id = po.id
       LEFT JOIN axe.grvs as grv ON grv.id = popo.grv_id
       JOIN axe.steel_specifications as ss ON popo.steel_specification_id = ss.id
       JOIN axe.suppliers as s on s.id= po.supplier_id
       JOIN axe.colors as c on ss.color_id = c.id
       JOIN axe.gauges as g on ss.gauge_id = g.id
       JOIN axe.widths as w ON w.id = ss.width_id
       WHERE popo.grv_id IS NULL
""", nativeQuery = true)
    List<StockOnOrderDetails> getStockOnOrder();

    @Query("""
    SELECT p 
    FROM PurchaseOrder p 
        WHERE p.supplier.id = :supplierId AND p.status = 'pending'
    ORDER BY p.id DESC
    """)
    List<PurchaseOrder> getPurchaseOrdersForSupplier(Long supplierId);

    Page<PurchaseOrder> findByStatus(String status, Pageable pageable);









}
