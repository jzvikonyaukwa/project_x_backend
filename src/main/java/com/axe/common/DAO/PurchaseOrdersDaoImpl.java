package com.axe.common.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.axe.common.charts.ChartSingleData;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Lazy
@Repository
public class PurchaseOrdersDaoImpl implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrdersDaoImpl.class);

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public PurchaseOrdersDaoImpl(final EntityManager entityManager$, final TransactionTemplate transactionTemplate$) {
        entityManager = entityManager$;
        transactionTemplate = transactionTemplate$;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public @Nonnull List<ChartSingleData> fetchSteelCoilSummary(@Nonnull LocalDate startDate,
            @Nonnull LocalDate endDate) {
        logger.debug("fetchSteelCoilSummary - {} - {}", startDate, endDate);
        try {
            String query = "SELECT \n" + //
                    "  new com.axe.common.charts.ChartSingleData(CONCAT(po.steelSpecification.coating, ' ', po.steelSpecification.gauge.gauge, ' ', po.steelSpecification.width.width, ' ', po.steelSpecification.color.color, ' ', po.steelSpecification.ISQGrade),\n"
                    + //
                    " CAST(SUM(po.weightOrdered) as double), CAST(null as double), CAST(null as double)) \n" + //
                    "FROM ProductsOnPurchaseOrder po \n" + //
                    "  WHERE po.purchaseOrder.dateIssued BETWEEN :startDate AND :endDate \n" + //
                    "GROUP BY po.steelSpecification.coating, po.steelSpecification.gauge.gauge, po.steelSpecification.width.width, po.steelSpecification.color.color, po.steelSpecification.ISQGrade";
            return entityManager.createQuery(query, ChartSingleData.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (final DataAccessException e) {
            logger.error("{}", e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Error accessing data in findSteelCoilSummaryByDateRange",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("{}", e.getLocalizedMessage());
            throw new NestedRuntimeException("General error in findSteelCoilSummaryByDateRange", e) {
            };
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public BigDecimal steelOrderValueForPeriod(LocalDate startDate, LocalDate endDate) {
        logger.debug("steelOrderValueForPeriod - {} - {}", startDate, endDate);
        try {
            String query = "SELECT \n" + //
                    "SUM(pop.weightOrdered * pop.purchaseCostPerKg) \n" + //
                    "FROM ProductsOnPurchaseOrder pop \n" + //
                    "JOIN pop.purchaseOrder po \n" + //
                    "WHERE po.dateIssued BETWEEN :startDate AND :endDate";
            return entityManager.createQuery(query, BigDecimal.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getSingleResult();
        } catch (final NoResultException e) {
            return BigDecimal.ZERO;
        } catch (final DataAccessException e) {
            logger.error("{}", e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Error accessing data in calculateTotalOrderSteelValueForRange",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("{}", e.getLocalizedMessage());
            throw new NestedRuntimeException("General error in calculateTotalOrderSteelValueForRange", e) {
            };
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public @Nonnull BigDecimal consumablesOrderValueForPeriod(LocalDate startDate, LocalDate endDate) {
        logger.debug("consumablesOrderValueForPeriod - {} - {}", startDate, endDate);
        try {
            String query = "SELECT \n" + //
                    "SUM(cop.costPerUnit * cop.qty) \n" + //
                    "FROM ConsumablesOnPurchaseOrder cop \n" + //
                    "JOIN cop.purchaseOrder po \n" + //
                    "WHERE po.dateIssued BETWEEN :startDate AND :endDate";
            return entityManager.createQuery(query, BigDecimal.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getSingleResult();
        } catch (final NoResultException e) {
            return BigDecimal.ZERO;
        } catch (final DataAccessException e) {
            logger.error("{}", e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Error accessing data in calculateTotalOrderConsumablesValueForRange",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("{}", e.getLocalizedMessage());
            throw new NestedRuntimeException("General error in calculateTotalOrderConsumablesValueForRange", e) {
            };
        }
    }

    public @Nonnull List<ChartSingleData> fetchConsumablesOrderedValue(LocalDate startDate, LocalDate endDate) {
        logger.debug("fetchConsumablesOrderedValue - {} - {}", startDate, endDate);
        try {
            String query = "SELECT \n" + //
                    "new com.axe.common.charts.ChartSingleData( \n" + //
                    "co.consumable.name, \n" + //
                    "CAST(SUM(co.qty * co.costPerUnit) as double), \n" + //
                    "CAST(null as double), CAST(null as double)) \n" + //
                    "FROM ConsumablesOnPurchaseOrder co \n" + //
                    " WHERE co.purchaseOrder.dateIssued BETWEEN :startDate AND :endDate \n" + //
                    " GROUP BY co.consumable.name";
            return entityManager.createQuery(query, ChartSingleData.class)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
        } catch (final NoResultException e) {
            return Collections.emptyList();
        } catch (final DataAccessException e) {
            logger.error("{}", e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Error accessing data in calculateConsumablesOrderedInRange",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("{}", e.getLocalizedMessage());
            throw new NestedRuntimeException("General error in calculateConsumablesOrderedInRange", e) {
            };
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("afterPropertiesSet()");
    }

    @Override
    public void destroy() throws Exception {
        logger.debug("destroy()");
    }
}