package com.axe.quotes.services;

import com.axe.clients.Client;
import com.axe.common.DAO.QuotesDaoImpl;
import com.axe.common.enums.QuoteStatus;
import com.axe.projects.Project;
import com.axe.quotes.Quote;
import com.axe.product.services.ProductService;
import com.axe.quotes.QuotesRepository;
import com.axe.quotes.quotesDTO.*;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuoteSummaryService {
    private static final Logger logger = LoggerFactory.getLogger(QuoteSummaryService.class);

    private final QuotesDaoImpl quotesDao;
    private final ProductService productService;
    private final QuotesRepository quoteRepository;

    // use a date format easy to read and appropriate for the frontend
    private final DateTimeFormatter dailyDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private final DateTimeFormatter weeklyDateFormatter = DateTimeFormatter.ofPattern("dd-MMM");
    private final DateTimeFormatter monthlyDateFormmater = DateTimeFormatter.ofPattern("MMM-yyyy");

    public QuoteSummaryService(ProductService productService,
            QuotesRepository quoteRepository, QuotesDaoImpl quotesDao) {
        this.productService = productService;
        this.quoteRepository = quoteRepository;
        this.quotesDao = quotesDao;
    }

    public QuoteSummaryDTO getQuoteSummary(LocalDate startDate, LocalDate endDate) {
        List<QuoteSummaryItemDTO> items = productService.findSummary(startDate, endDate);

        // Transform and aggregate items
        Map<String, BigDecimal> aggregatedItems = items.stream()
                .map(this::mapPlanName)
                .collect(Collectors.groupingBy(
                        QuoteSummaryItemDTO::getPlanName,
                        Collectors.reducing(BigDecimal.ZERO, QuoteSummaryItemDTO::getCount, BigDecimal::add)
                ));

        // Convert the map back to a list of QuoteSummaryItemDTO
        List<QuoteSummaryItemDTO> transformedItems = aggregatedItems.entrySet().stream()
                .map(entry -> new QuoteSummaryItemDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        QuoteSummaryDTO summary = new QuoteSummaryDTO();
        summary.setStartDate(startDate);
        summary.setEndDate(endDate);
        summary.setItems(transformedItems);
        return summary;
    }

    private QuoteSummaryItemDTO mapPlanName(QuoteSummaryItemDTO item) {
        String mappedPlanName = switch (item.getPlanName().toUpperCase()) {
            case "SHEETS" -> "Roof Sheets";
            case "BRACES", "FLOOR JOISTS", "ROOF PANELS", "TRUSSES", "WALL FRAMES" -> "Framecad";
            case "PURLINS", "BATTENS" -> "Purlin and Batten";
            default -> item.getPlanName();
        };
        return new QuoteSummaryItemDTO(mappedPlanName, item.getCount());
    }
    public QuoteStatusCount getQuoteStatusCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return quoteRepository.getQuoteStatusCountByDateRange(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<com.axe.quotes.QuoteSummaryDTO> getQuotesCountPerDay(LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting quotes count per day between {} and {}", startDate, endDate);
        // calc number of days between start and end date
        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate);
        logger.trace("Duration in days: {}", durationInDays);
        if (durationInDays > 31) {
            // calc quotes for each month
            return calculateQuotesPerMonth(startDate, endDate);
        } else if (durationInDays > 7) {
            // calc quotes for each week
            return calculateQuotesWithinMonth(startDate, endDate);
        } else {
            // calc quotes each day
            return calculateQuotesForCurrentWeek(startDate, endDate);
        }
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuotesForCurrentWeek(LocalDate startDate, LocalDate endDate) {
        logger.debug("Calculating quotes per day between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> dailySummaries = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            logger.trace(" - date: {}", currentDate);

            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {
                Long count = quotesDao.countQuotesByStatusInRange(currentDate, currentDate, status);
                statusCounts.put(status, count);
            }
            String formattedDate = dailyDateFormatter.format(currentDate);
            com.axe.quotes.QuoteSummaryDTO dailySummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts, null,
                    formattedDate);
            dailySummaries.add(dailySummary);
        }

        logger.debug("Quotes per day: {}", dailySummaries);

        return dailySummaries;
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuotesWithinMonth(LocalDate startDate, LocalDate endDate) {
        logger.debug("Calculating quotes per week between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> dailySummaries = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            logger.trace(" - date: {}", currentDate);

            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {
                Long count = quotesDao.countQuotesByStatusInRange(currentDate, currentDate, status);
                statusCounts.put(status, count);
            }
            String formattedDate = weeklyDateFormatter.format(currentDate);
            com.axe.quotes.QuoteSummaryDTO dailySummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts, null,
                    formattedDate);
            dailySummaries.add(dailySummary);
        }

        logger.debug("Quotes per day: {}", dailySummaries);

        return dailySummaries;
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuotesPerMonth(LocalDate startDate, LocalDate endDate) {
        logger.debug("Calculating quotes per month between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> monthlySummaries = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate
                .plusMonths(1)) {
            LocalDate minDate = currentDate.withDayOfMonth(1);
            LocalDate maxDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
            logger.trace("Calculating quotes for date range: {} - {}", minDate, maxDate);

            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {
                Long count = quotesDao.countQuotesByStatusInRange(minDate, maxDate, status);
                statusCounts.put(status, count);
            }
            String formattedDate = monthlyDateFormmater.format(minDate);
            com.axe.quotes.QuoteSummaryDTO monthSummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts, null,
                    formattedDate);
            monthlySummaries.add(monthSummary);
        }

        logger.debug("Quotes per month: {}", monthlySummaries);

        return monthlySummaries;
    }

    @Transactional(readOnly = true)
    public List<QuoteDetailsDTO> getLatestQuotes() {
        logger.debug("Getting latest quotes");
        Sort sortOrder =  Sort.by(
            Sort.Order.desc("dateLastModified"),
            Sort.Order.desc("id")
        );
        Pageable pageRequest = PageRequest.of(0, 5, sortOrder);
        List<Quote> quotes = quoteRepository.findAll(pageRequest).toList();

        return quotes.stream()
        .map( ($) -> {
            Project project = $.getProject();
            Client client = project != null ? project.getClient() : null;

            return new QuoteDetailsDTO($.getId()
            , client != null ? client.getName() : ""
            , project != null ? project.getName() : ""
            , $.getDateIssued()
            , $.getDateLastModified()
            , $.getDateAccepted()
            , $.getStatus().name()
            , $.getNotes()
            , client != null ? client.getId() : 0L);
        })
        .collect(Collectors.toList());
    }

    public List<com.axe.quotes.QuoteSummaryDTO> getQuoteTotalsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting quote totals by date range between {} and {}", startDate, endDate);

        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate);
        if (durationInDays > 31) {
            // calc quotes for each month
            return calculateQuoteTotalsForCurrentYear(startDate, endDate);
        } else if (durationInDays > 7) {
            // calc quotes for each week
            return calculateQuotesValuesForCurrentMonth(startDate, endDate);
        } else {
            // calc quotes each day
            return calculateQuotesValuesCurrentWeek(startDate, endDate);
        }
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuotesValuesCurrentWeek(LocalDate startDate,            LocalDate endDate) {
        logger.debug("Calculating values quotes per day between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> dailyTotals = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            logger.trace("Calculating quotes values for date: {}", currentDate);

            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            Map<QuoteStatus, Double> statusTotalValues = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {

                Long count = quotesDao.countQuotesByStatusInRange(currentDate, currentDate, status);
                logger.debug(" - count: " + count);
                statusCounts.put(status, count);

                BigDecimal consumableTotal = quotesDao.totalConsumablesOnQuotesByStatusInRange(currentDate,
                        currentDate, status);
                logger.debug(" - consumableTotal: " + consumableTotal);

                BigDecimal manufacturedTotal = quotesDao.totalManufacturedProductsOnQuotesByStatusInRange(
                        currentDate, currentDate, status);
                logger.debug(" - manufacturedTotal: " + manufacturedTotal);
                consumableTotal = Optional.ofNullable(consumableTotal).orElse(BigDecimal.ZERO);
                manufacturedTotal = Optional.ofNullable(manufacturedTotal).orElse(BigDecimal.ZERO);
                BigDecimal total = consumableTotal.add(manufacturedTotal);
                logger.debug(" - total: " + total);
                statusTotalValues.put(status, total.doubleValue());
            }
            String formattedDate = dailyDateFormatter.format(currentDate);
            com.axe.quotes.QuoteSummaryDTO daySummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts,
                    statusTotalValues,
                    formattedDate);
            dailyTotals.add(daySummary);
        }

        return dailyTotals;
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuotesValuesForCurrentMonth(LocalDate startDate,
            LocalDate endDate) {
        logger.debug("Calculating quotes values for month between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> monthlyTotals = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            logger.trace("Calculating quotes values for date: {}", currentDate);
            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            Map<QuoteStatus, Double> statusTotalValues = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {
                Long count = quotesDao.countQuotesByStatusInRange(currentDate, currentDate, status);
                logger.debug(" - count: " + count);
                statusCounts.put(status, count);

                BigDecimal consumableTotal = quotesDao.totalConsumablesOnQuotesByStatusInRange(
                        currentDate, currentDate, status);
                logger.debug(" - consumableTotal: " + consumableTotal);

                BigDecimal manufacturedTotal = quotesDao.totalManufacturedProductsOnQuotesByStatusInRange(
                        currentDate, currentDate, status);
                logger.debug(" - manufacturedTotal: " + manufacturedTotal);
                consumableTotal = Optional.ofNullable(consumableTotal).orElse(BigDecimal.ZERO);
                manufacturedTotal = Optional.ofNullable(manufacturedTotal).orElse(BigDecimal.ZERO);
                BigDecimal total = consumableTotal.add(manufacturedTotal);
                logger.debug(" - total: " + total);
                statusTotalValues.put(status, total.doubleValue());
            }
            String formattedDate = weeklyDateFormatter.format(currentDate);
            com.axe.quotes.QuoteSummaryDTO weekSummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts,
                    statusTotalValues,
                    formattedDate);
            monthlyTotals.add(weekSummary);
        }

        return monthlyTotals;
    }

    private List<com.axe.quotes.QuoteSummaryDTO> calculateQuoteTotalsForCurrentYear(LocalDate startDate,
            LocalDate endDate) {
        logger.debug("Calculating values quotes per month between {} and {}", startDate, endDate);

        List<com.axe.quotes.QuoteSummaryDTO> monthlyTotals = new ArrayList<>();

        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate
                .plusMonths(1)) {
            logger.trace("Calculating quotes values for date: {}", currentDate);

            LocalDate minDate = currentDate.withDayOfMonth(1);
            LocalDate maxDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
            logger.trace("Calculating quotes for date range: {} - {}", minDate, maxDate);

            Map<QuoteStatus, Long> statusCounts = new EnumMap<>(QuoteStatus.class);
            Map<QuoteStatus, Double> statusTotalValues = new EnumMap<>(QuoteStatus.class);
            for (QuoteStatus status : QuoteStatus.values()) {

                Long count = quotesDao.countQuotesByStatusInRange(minDate, maxDate, status);
                logger.debug(" - count: " + count);
                statusCounts.put(status, count);

                BigDecimal consumableTotal = quotesDao.totalConsumablesOnQuotesByStatusInRange(minDate,
                        maxDate, status);
                logger.debug(" - consumableTotal: " + consumableTotal);

                BigDecimal manufacturedTotal = quotesDao.totalManufacturedProductsOnQuotesByStatusInRange(
                        minDate, maxDate, status);
                logger.debug(" - manufacturedTotal: " + manufacturedTotal);
                consumableTotal = Optional.ofNullable(consumableTotal).orElse(BigDecimal.ZERO);
                manufacturedTotal = Optional.ofNullable(manufacturedTotal).orElse(BigDecimal.ZERO);
                BigDecimal total = consumableTotal.add(manufacturedTotal);
                logger.debug(" - total: " + total);
                statusTotalValues.put(status, total.doubleValue());
            }
            String formattedDate = monthlyDateFormmater.format(minDate);
            com.axe.quotes.QuoteSummaryDTO monthSummary = new com.axe.quotes.QuoteSummaryDTO(statusCounts,
                    statusTotalValues,
                    formattedDate);

            monthlyTotals.add(monthSummary);
        }

        return monthlyTotals;
    }
}
