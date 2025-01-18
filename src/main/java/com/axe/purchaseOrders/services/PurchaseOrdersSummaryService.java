package com.axe.purchaseOrders.services;

import com.axe.common.DAO.PurchaseOrdersDaoImpl;
import com.axe.common.charts.ChartMultiData;
import com.axe.common.charts.ChartSingleData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PurchaseOrdersSummaryService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrdersSummaryService.class);

    private final DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("MMM-yy");
    private final DateTimeFormatter dayMonthFormatter = DateTimeFormatter.ofPattern("dd-MMM");
    private final DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private final PurchaseOrdersDaoImpl purchaseOrdersDao;

    public PurchaseOrdersSummaryService(PurchaseOrdersDaoImpl purchaseOrdersDao) {
        this.purchaseOrdersDao = purchaseOrdersDao;
    }

    public List<ChartSingleData> consumableSummaryForPeriod(LocalDate startDate, LocalDate endDate) {
        logger.debug("consumableSummaryForPeriod - {} - {}", startDate, endDate);

        return purchaseOrdersDao.fetchConsumablesOrderedValue(startDate, endDate);
    }

    public List<ChartSingleData> steelCoilSummaryForPeriod(LocalDate fromDate, LocalDate thruDate) {
        logger.debug("steelCoilSummaryForPeriod - {} - {}", fromDate, thruDate);

        return purchaseOrdersDao.fetchSteelCoilSummary(fromDate, thruDate);
    }

    public List<ChartMultiData> orderSummaryForPeriod(LocalDate startDate, LocalDate endDate) {
        logger.debug("orderSummaryForPeriod - {} - {}", startDate, endDate);
        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate);
        logger.debug(" - durationInDays - {}", durationInDays);

        if (durationInDays > 31) {
            return summarizeOrdersByMonth(startDate, endDate);
        } else if (durationInDays > 7) {
            return summarizeOrdersByWeek(startDate, endDate);
        }

        return summarizeOrdersByDay(startDate, endDate);
    }

    private List<ChartMultiData> summarizeOrdersByDay(LocalDate fromDate, LocalDate thruDate) {
        logger.debug("summarizeOrdersByDay - {} - {}", fromDate, thruDate);
        List<ChartMultiData> dailySummaries = new ArrayList<>();
        for (LocalDate date = fromDate; !date.isAfter(thruDate); date = date.plusDays(1)) {
            logger.trace(" - {}", date);
            BigDecimal totalSteelOrdered = purchaseOrdersDao.steelOrderValueForPeriod(date,
                    date);
            BigDecimal totalConsumablesOrdered = purchaseOrdersDao
                    .consumablesOrderValueForPeriod(date, date);

            List<ChartSingleData> series = Arrays.asList(
                    new ChartSingleData("Steel Coils",
                            totalSteelOrdered != null ? totalSteelOrdered.doubleValue() : 0d, null, null),
                    new ChartSingleData("Consumables",
                            totalConsumablesOrdered != null ? totalConsumablesOrdered.doubleValue() : 0d, null,
                            null));

                            dailySummaries.add(new ChartMultiData(fullDateFormatter.format(date), series));
        }
        return dailySummaries;
    }

    private List<ChartMultiData> summarizeOrdersByWeek(LocalDate startDate, LocalDate endDate) {
        logger.debug("summarizeOrdersByWeek - {} - {}", startDate, endDate);
        List<ChartMultiData> weeklySummaries = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            logger.trace(" - {}", date);
            BigDecimal totalSteelOrdered = purchaseOrdersDao.steelOrderValueForPeriod(date, date);
            BigDecimal totalConsumablesOrdered = purchaseOrdersDao
                    .consumablesOrderValueForPeriod(date, date);
            List<ChartSingleData> series = Arrays.asList(
                    new ChartSingleData("Steel Coils",
                            totalSteelOrdered != null ? totalSteelOrdered.doubleValue() : 0d, null, null),
                    new ChartSingleData("Consumables",
                            totalConsumablesOrdered != null ? totalConsumablesOrdered.doubleValue() : 0d, null,
                            null));

                            weeklySummaries.add(new ChartMultiData(dayMonthFormatter.format(date), series));
        }
        return weeklySummaries;
    }

    private List<ChartMultiData> summarizeOrdersByMonth(LocalDate startDate, LocalDate endDate) {
        logger.debug("summarizeOrdersByMonth - {} - {}", startDate, endDate);
        List<ChartMultiData> monthlySummaries = new ArrayList<>();
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate
                .plusMonths(1)) {
            LocalDate startOfMonth = currentDate.withDayOfMonth(1);
            LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
            logger.trace(" - {} - {}", startOfMonth, endOfMonth);
            BigDecimal totalSteelOrdered = purchaseOrdersDao.steelOrderValueForPeriod(startOfMonth, endOfMonth);
            BigDecimal totalConsumablesOrdered = purchaseOrdersDao.consumablesOrderValueForPeriod(startOfMonth,
                    endOfMonth);

            List<ChartSingleData> series = Arrays.asList(
                    new ChartSingleData("Steel Coils",
                            totalSteelOrdered != null ? totalSteelOrdered.doubleValue() : 0d, null, null),
                    new ChartSingleData("Consumables",
                            totalConsumablesOrdered != null ? totalConsumablesOrdered.doubleValue() : 0d, null,
                            null));

            monthlySummaries.add(new ChartMultiData(yearMonthFormatter.format(currentDate), series));
        }
        return monthlySummaries;
    }
}
