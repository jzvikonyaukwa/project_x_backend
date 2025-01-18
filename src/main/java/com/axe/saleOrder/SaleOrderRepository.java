package com.axe.saleOrder;

import com.axe.saleOrder.models.ProductsTotalLengthOnOrder;
import com.axe.saleOrder.models.SaleOrderData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {

    @Query( value = """
        SELECT
          so.id as saleOrderId, so.status AS salesOrderStatus, so.date_created as salesOrderIssued,
          so.target_date as targetDate, i.date_invoiced, i.paid,
          q.date_issued, q.date_last_modified,
          c.name as clientName, i.id as invoiceId,
          pr.id as cuttingListId,pr.status as cuttingListStatus
          FROM axe.sale_orders as so
          LEFT JOIN axe.invoices as i ON i.sale_order_id = so.id
          JOIN axe.quotes as q ON q.id = so.quote_id
          JOIN
            axe.projects as p ON q.project_id = p.id
          JOIN
            axe.clients as c ON c.id = p.client_id
          LEFT JOIN
            axe.products as pr ON pr.quote_id = q.id
          ORDER BY saleOrderId DESC
    """, nativeQuery = true
    )
    List<SaleOrderData> getAllSaleOrdersSummaryDetails();

    @Query(value = """
            SELECT
                pt.width,
                 g.gauge,
                 c.color,
                 SUM(mp.length) AS totalLengthReserved,
                 COUNT(DISTINCT cl.id) AS cuttingListCount
             FROM
                 axe.quotes as q
             JOIN
                 axe.cutting_lists as cl ON cl.quote_id = q.id
             JOIN
                 axe.product_types as pt ON cl.product_type_id = pt.id
             JOIN
                 axe.manufactured_products as mp ON mp.cutting_list_id = cl.id
             JOIN
                 axe.gauges as g ON cl.gauge_id = g.id
             JOIN
                 axe.colors as c ON cl.color_id = c.id
            JOIN
                axe.projects as p ON q.project_id = p.id
            JOIN
                 axe.sale_orders as so ON q.id = so.quote_id
            JOIN
                 axe.invoices as i ON i.sale_order_id = so.id

             GROUP BY
                 cl.gauge_id, cl.color_id, pt.width;
    """ , nativeQuery = true)
    List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderReserved();

    @Query(value = """
            SELECT
                pt.width,
                 g.gauge,
                 c.color,
                 SUM(mp.length) AS totalLengthReserved,
                 COUNT(DISTINCT cl.id) AS cuttingListCount
             FROM
                 axe.quotes as q
             JOIN
                 axe.cutting_lists as cl ON cl.quote_id = q.id
             JOIN
                 axe.product_types as pt ON cl.product_type_id = pt.id
             JOIN
                 axe.manufactured_products as mp ON mp.cutting_list_id = cl.id
             JOIN
                 axe.gauges as g ON cl.gauge_id = g.id
             JOIN
                 axe.colors as c ON cl.color_id = c.id
            JOIN
                axe.projects as p ON q.project_id = p.id
            JOIN
                 axe.sale_orders as so ON q.id = so.quote_id
            JOIN
                 axe.invoices as i ON i.sale_order_id = so.id
       
             GROUP BY
                 cl.gauge_id, cl.color_id, pt.width;
    """ , nativeQuery = true)
    List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderNotReserved();

    @Query(value= """
        SELECT
             w.width,
             g.gauge,
             c.color,
             SUM(mp.length ) AS totalLengthSoldNotReserved,
             COUNT(DISTINCT pr.id) AS cuttingListCount
          FROM
             axe.quotes as q
          JOIN
             axe.products as pr ON pr.quote_id = q.id
          LEFT JOIN
             axe.product_types as pt ON cl.product_type_id = pt.id
          JOIN
             axe.manufactured_products as mp ON mp.cutting_list_id = cl.id
          JOIN
             axe.gauges as g ON cl.gauge_id = g.id
          JOIN
             axe.widths as w ON cl.width_id = w.id
          JOIN
             axe.colors as c ON cl.color_id = c.id
          GROUP BY
             cl.gauge_id, cl.color_id, w.width;
    """, nativeQuery = true)
    List<ProductsTotalLengthOnOrder> getTotalLengthOnOrder();

}
