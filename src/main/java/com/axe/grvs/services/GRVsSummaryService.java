package com.axe.grvs.services;

import com.axe.common.charts.ChartSingleData;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.grvsDTO.summaryDTO.ConsumableDetailsDTO;
import com.axe.grvs.grvsDTO.summaryDTO.MonthlySummaryDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GRVsSummaryService {
    private static final Logger logger = LoggerFactory.getLogger(GRVsSummaryService.class);
    
    private final GRVsRepository grvsRepository;
    public GRVsSummaryService(GRVsRepository grVsRepository) {
        this.grvsRepository = grVsRepository;
    }

    public List<ConsumableDetailsDTO> getConsumableDetailsByDateRange(LocalDate startDate, LocalDate endDate) {
        return grvsRepository.findConsumableDetailsByDateRange(startDate, endDate);
    }

    public List<ChartSingleData> getSteelCoilSummaryDetails(String startDateStr, String endDateStr) {
        logger.debug("getSteelCoilSummaryDetails - {} - {}", startDateStr, endDateStr);
        try{

        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        List<Object[]> results = grvsRepository.getSteelCoilSummaryDetails(startDate, endDate);
        return results.stream()
                .map(result -> new ChartSingleData(((String) result[0]).trim()
                , ((Number) result[1]).doubleValue()
                , null
                , null))
                .collect(Collectors.toList());
        } catch (DataAccessException e) {
            logger.error(e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Failed to get latest GRV totals", e.getMostSpecificCause()) {
            };
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new NestedRuntimeException("Failed to get latest GRV totals", e) {
            };
        }
    }

    public List<MonthlySummaryDTO> getMonthlySummary(int year) {
        List<Object[]> steelCoilResults = grvsRepository.getMonthlySteelCoilCount(year);
        List<Object[]> consumableResults = grvsRepository.getMonthlyConsumableCount(year);

        List<MonthlySummaryDTO> summary = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            int finalMonth = month;
            long steelCoilsCount = steelCoilResults.stream()
                    .filter(result -> ((Number) result[0]).intValue() == finalMonth)
                    .map(result -> ((Number) result[1]).longValue())
                    .findFirst()
                    .orElse(0L);


            long consumablesCount = consumableResults.stream()
                    .filter(result -> ((Number) result[0]).intValue() == finalMonth)
                    .map(result -> ((Number) result[1]).longValue())
                    .findFirst()
                    .orElse(0L);

            summary.add(new MonthlySummaryDTO(month, steelCoilsCount, consumablesCount));
        }

        return summary.stream().filter(dto -> dto.getSteelCoilsCount() > 0 || dto.getConsumablesCount() > 0).collect(Collectors.toList());
    }
}
