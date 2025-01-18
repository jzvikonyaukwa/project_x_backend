package com.axe.product;

import com.axe.product.productDTO.ProductSummaryDetailsDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductStatusCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.MonthlyManufacturedProductCountDTO;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteSummaryItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> , JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.inventory.id = :inventoryId")
    List<Product> findByInventoryId(@Param("inventoryId") Long inventoryId);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM Product p
                WHERE  p.id = :productId
            AND (
                p.status <> 'completed'
                AND (
                    SELECT count(p)
                    FROM AggregatedProduct ap
                    WHERE ap.product.id = p.id
                        AND ap.status = 'completed'
                ) = 0
            )
            """)
    /**
     * Deletes a cutting list if it is not completed and does not contain any
     * completed manufactured products.
     *
     * @param cuttingListId The ID of the cutting list to delete.
     */
    void deleteUnprocessedProduct(Long productId);

    @Query(value = """
            SELECT p.id
              FROM axe.products as p
              JOIN axe.widths as w ON w.id = p.width_id
              WHERE p.status='in-progress' AND(w.width = :width OR (:width = 182 AND w.width = 100))""", nativeQuery = true)
    Long findProductIDInProgressForMachine(BigDecimal width);


    @Query(value = """
        SELECT
            pr.id as productId,
            pr.last_worked_on as productDateLastWorkedOn,
            pr.status as productStatus,
            pr.priority,
            pr.target_date as targetDate,
            pr.plan_name as planName,
            pr.can_invoice as canInvoice,
            pr.date_work_began as productDateWorkBegan,
            pr.date_work_completed as productDateWorkCompleted,
            p.profile as profile,
            p.id as profileId,
            c.id as colorId,
            c.color,
            g.id as gaugeId,
            g.gauge,
            w.id as widthId,
            w.width,
            client.name as clientName,
            q.status as quoteStatus,
            q.date_accepted as quoteDateAccepted,
            SUM(CASE WHEN ap.status = 'completed' THEN ap.length ELSE 0 END) AS completedLength,
            SUM(CASE WHEN ap.status = 'scheduled' THEN ap.length ELSE 0 END) AS scheduledLength
        FROM
            axe.products as pr
        JOIN
         axe.aggregated_products ap on ap.product_id =pr.id
        JOIN
            axe.colors as c ON c.id = pr.color_id
        JOIN
            axe.gauges as g ON g.id = pr.gauge_id
        JOIN
            axe.widths as w ON w.id = pr.width_id
       
        LEFT JOIN
            axe.profile as p ON p.id = pr.profile_id
        JOIN
            axe.quotes as q ON q.id = pr.quote_id
        JOIN
            axe.projects as proj ON proj.id = q.project_id
        JOIN
            axe.clients as client ON proj.client_id = client.id
        WHERE
            q.status = 'accepted'
            AND (w.width = :width OR (:width = 182 AND w.width = 100))
            AND pr.status = 'in-progress'
        GROUP BY
            pr.id,
            pr.last_worked_on,
            pr.status,
            pr.priority,
            pr.target_date,
            pr.plan_name,
            pr.can_invoice,
            pr.date_work_began,
            pr.date_work_completed,
            p.profile,
            p.id,
            c.id,
            c.color,
            g.id,
            g.gauge,
            w.id,
            w.width,
            client.name,
            q.status,
            q.date_accepted;
        """, nativeQuery = true)
    ProductSummaryDetailsDTO findProductInProgressForMachine(Integer width);




    @Query(value = """
            SELECT
                pr.id as productId,
                pr.last_worked_on as productDateLastWorkedOn,
                pr.status as productStatus,
                pr.priority,
                pr.target_date as targetDate,
                pr.plan_name as planName,
                pr.can_invoice as canInvoice,
                pr.date_work_began as productDateWorkBegan,
                pr.date_work_completed as productDateWorkCompleted,
                p.profile as profile,
                p.id as profileId,
                c.id as colorId,
                c.color,
                g.id as gaugeId,
                g.gauge,
                w.id as widthId,
                w.width,
                client.name as clientName,
                q.status as quoteStatus,
                q.date_accepted as quoteDateAccepted,
                SUM(CASE WHEN ap.status = 'completed' THEN ap.length ELSE 0 END) AS completedLength,
                SUM(CASE WHEN ap.status = 'scheduled' THEN ap.length  ELSE 0 END) AS scheduledLength
            FROM
                axe.products as pr
            JOIN
                axe.aggregated_products ap on ap.product_id =pr.id
            JOIN
                axe.colors as c ON c.id = pr.color_id
            JOIN
                axe.gauges as g ON g.id = pr.gauge_id
            JOIN
                axe.widths as w ON w.id = pr.width_id
    
            LEFT JOIN
                axe.profile as p ON p.id = pr.profile_id
            JOIN
                axe.quotes as q ON q.id = pr.quote_id
            JOIN
                axe.projects as proj ON proj.id = q.project_id
            JOIN
                axe.clients as client ON proj.client_id = client.id
            WHERE
                q.status = 'accepted'
            AND (w.width = :width OR (:width = 182 AND w.width = 100))
                AND pr.status = 'scheduled'
            GROUP BY
                pr.id,
                pr.last_worked_on,
                pr.status,
                pr.priority,
                pr.target_date,
                pr.plan_name,
                pr.can_invoice,
                pr.date_work_began,
                pr.date_work_completed,
                p.profile,
                p.id,
                c.id,
                c.color,
                g.id,
                g.gauge,
                w.id,
                w.width,
                client.name,
                q.status,
                q.date_accepted;
    """, nativeQuery = true)
    List<ProductSummaryDetailsDTO> getProductsScheduledForProduct(Integer width);

    @Query("SELECT pr FROM Product pr " +
            "JOIN FETCH pr.quote q " +
            "JOIN FETCH q.project p " +
            "JOIN FETCH p.client c " +
            "WHERE pr.id = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Long id);


    // Manufactured Product Summary
    //TODO: Might need to change the date being used to the date the product was manufactured
    @Query("SELECT new com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductCountDTO(p.frameType, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.targetDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.frameType")
    List<ManufacturedProductCountDTO> findManufacturedProductCountByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductStatusCountDTO(p.status, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE p.targetDate BETWEEN :startDate AND :endDate " +
            "GROUP BY p.status")
    List<ManufacturedProductStatusCountDTO> findManufacturedProductStatusCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.axe.product.productDTO.manufacturedProductSummaryDTO.MonthlyManufacturedProductCountDTO(FUNCTION('MONTH', p.targetDate), p.frameType, COUNT(p.id)) " +
            "FROM Product p " +
            "WHERE FUNCTION('YEAR', p.targetDate) = :year " +
            "GROUP BY FUNCTION('MONTH', p.targetDate), p.frameType")
    List<MonthlyManufacturedProductCountDTO> findManufacturedProductCountByMonthAndType(@Param("year") int year);


    @Query("SELECT new com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteSummaryItemDTO(p.planName, SUM(p.totalLength)) " +
            "FROM Product p " +
            "WHERE p.quote.dateIssued BETWEEN :startDate AND :endDate " +
            "GROUP BY p.planName " +
            "UNION ALL " +
            "SELECT new com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteSummaryItemDTO(coq.consumable.name, COUNT(coq)) " +
            "FROM ConsumableOnQuote coq " +
            "WHERE coq.quote.dateIssued BETWEEN :startDate AND :endDate " +
            "GROUP BY coq.consumable.name")
    List<QuoteSummaryItemDTO> findSummary(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}
