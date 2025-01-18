package com.axe.grvs;

import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.common.charts.ChartSingleData;
import com.axe.grvs.grvsDTO.GRVDetailsDTO;
import com.axe.grvs.grvsDTO.GRVTotalDTO;
import com.axe.grvs.grvsDTO.summaryDTO.ConsumableDetailsDTO;
import com.axe.grvs.grvsDTO.GRVMasterDetailDTO;
import com.axe.grvs.services.GRVAddService;
import com.axe.grvs.services.GRVUpdateService;
import com.axe.grvs.services.GRVsService;
import com.axe.grvs.services.GRVsSummaryService;
import com.axe.utilities.ClientVisibleException;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/grvs")
@CrossOrigin(origins = { "http://localhost:4200", "http://axebuild.io", "https://axebuild.io" })
public class GRVsController {
    private static final Logger logger = LoggerFactory.getLogger(GRVsController.class);

    private final GRVsService grvsService;
    private final GRVUpdateService grvUpdateService;
    private final GRVAddService grvAddService;
    private final GRVsSummaryService grvsSummaryService;

    public GRVsController(GRVsService grvsService, GRVUpdateService grvUpdateService, GRVAddService grvAddService,

                          GRVsSummaryService grvsSummaryService) {
        this.grvsService = grvsService;
        this.grvUpdateService = grvUpdateService;
        this.grvAddService = grvAddService;
        this.grvsSummaryService = grvsSummaryService;
    }

    @GetMapping("all-grvs")
    public List<GRV> getAllGRVs() {
        return grvsService.getAllGRVs();
    }

    @GetMapping("latest")
    @Operation(summary = "Dashboard - Get list of latest GRV totals (last 5 by default | sane limit 1000)")
    @Transactional(readOnly = true)
    public List<GRVTotalDTO> getLatestGRVTotals(@RequestParam(required = false) Integer limit) {
        logger.info("Getting latest GRV totals with limit: {}", limit);
        try {
        Integer requestLimit = Optional.ofNullable(limit).orElse(5);
        return grvsService.getLatestGRVTotals(Math.min(1000, requestLimit));
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error fetching grvs for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching latest GRV totals.");
        }
    }

    @PostMapping("get-rows")
    @Operation(summary = "Get grvs master details based on ag-grid request")
    public ServerSideGetRowsResponse<List<GRVMasterDetailDTO>> processAgGridQuoteRequest(
            @RequestBody ServerSideGetRowsRequest request) {
        logger.info("Getting grvs for AG Grid request: {}", request);
        try {
            return grvsService.fetchGRVMasterDetailGridRows(request);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error fetching grvs for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching grvs.");
        }
    }

    @GetMapping("{id}/details")
    public GRVDetailsDTO getGRVInformation(@PathVariable Long id) {
        return grvsService.getGRVInformation(id);
    }

    @PostMapping("add")
    public ResponseEntity<GRVDetailsDTO> addGRV(@RequestBody GRVDetailsDTO grv) {
        logger.info("Adding GRV: {}", grv);
        try {
            return ResponseEntity.ok(grvAddService.createGRV(grv));
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error creating grv: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error creating grv.");
        }
    }

    @PutMapping("update")
    public GRVDetailsDTO updateGRV(@RequestBody GRVDetailsDTO grv) {
        return grvUpdateService.updateGRV(grv);
    }

    @GetMapping("get-all-grvs-for-supplier/{id}")
    public List<GRV> getAllGRVsForSupplier(@PathVariable Long id) {
        return grvsService.getAllGRVsForSupplier(id);
    }

    @GetMapping("/consumables-summary-details")
    public ResponseEntity<List<ConsumableDetailsDTO>> getConsumableDetailsByDateRange(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);
        return ResponseEntity.ok(grvsSummaryService.getConsumableDetailsByDateRange(startDate, endDate));
    }

    @GetMapping("/steel-coil-summary-details")
    @Operation(summary = "Dashboard - Get steel coil summary details for a date range (last 30 days by default)")
    @Transactional(readOnly = true)
    public List<ChartSingleData> getSteelCoilSummaryDetails(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr) {
        logger.info("Getting steel coil summary details for date range: {} - {}", startDateStr, endDateStr);

        return grvsSummaryService.getSteelCoilSummaryDetails(startDateStr, endDateStr);
    }
}
