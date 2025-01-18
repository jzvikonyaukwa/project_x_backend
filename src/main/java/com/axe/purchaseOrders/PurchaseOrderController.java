package com.axe.purchaseOrders;

import com.axe.common.charts.ChartMultiData;
import com.axe.common.charts.ChartSingleData;
import com.axe.grvs.grvsDTO.GRVDetailsDTO;
import com.axe.grvs.services.GRVsService;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.purchaseOrders.DTOs.*;
import com.axe.purchaseOrders.services.EditPurchaseOrder;
import com.axe.purchaseOrders.services.PurchaseOrderAddService;
import com.axe.purchaseOrders.services.PurchaseOrderService;
import com.axe.purchaseOrders.services.PurchaseOrdersSummaryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = { "http://localhost:4200", "http://axebuild.io", "https://axebuild.io" })
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderAddService purchaseOrderAddService;
    private final PurchaseOrdersSummaryService purchaseOrdersSummaryService;
    private final EditPurchaseOrder editPurchaseOrder;
    private final GRVsService grvsService;
    Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService,
                                   PurchaseOrderAddService purchaseOrderAddService,
                                   GRVsService grvsService,
                                   PurchaseOrdersSummaryService purchaseOrdersSummaryService,
                                   EditPurchaseOrder editPurchaseOrder) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderAddService = purchaseOrderAddService;
        this.grvsService = grvsService;
        this.purchaseOrdersSummaryService = purchaseOrdersSummaryService;
        this.editPurchaseOrder = editPurchaseOrder;
    }

    // change Parameter to allow server side filtering
    @GetMapping("")
    public ResponseEntity<PurchaseOrdersPaginationResponseDTO> getAllPurchaseOrders(int pageNo, int pageSize,
            @RequestParam(required = false) String filters) {
        return ResponseEntity.ok(purchaseOrderService.getAllPurchaseOrders(pageNo, pageSize, filters));
    }

    @GetMapping("{purchaseOrderId}")
    public PurchaseOrder getPurchaseOrder(@PathVariable Long purchaseOrderId) {
        return purchaseOrderService.getPurchaseOrder(purchaseOrderId);
    }

    @GetMapping("all-purchase-orders/on-order")
    public List<PurchaseOrder> getAllPurchaseOrdersOnOrder() {
        return purchaseOrderService.getAllPurchaseOrdersOnOrder();
    }

    @GetMapping("/products-on-order-for-supplier/{supplierId}")
    public List<PurchaseOrder> getAllSuppliersPurchaseOrdersOnOrder(@PathVariable Long supplierId) {
        return purchaseOrderService.getAllSuppliersPurchaseOrdersOnOrder(supplierId);
    }

    @GetMapping("{id}/grvs")
    public List<GRVDetailsDTO> getAllGRVForPO(@PathVariable Long id) {
        return grvsService.getAllGRVForPO(id);
    }

    @PostMapping("")
    public PurchaseOrder addPurchaseOrder(@RequestBody PurchaseOrderPostDTO purchaseOrderPostDTO) {
        logger.info("Just received a purchase order: {}", purchaseOrderPostDTO);
        return purchaseOrderAddService.addPurchaseOrder(purchaseOrderPostDTO);
    }

    @PatchMapping("")
    public PurchaseOrder updateEditedPurchaseOrder(@RequestBody PurchaseOrderPostDTO purchaseOrder) {
        return editPurchaseOrder.updateEditedPurchaseOrder(purchaseOrder);
    }

    @PatchMapping("{id}/new-status/{status}")
    public PurchaseOrder changePurchaseOrderStatus(@PathVariable Long id, @PathVariable String status) {
        return purchaseOrderService.changePurchaseOrderStatus(id, status);
    }

    @PatchMapping("update-products-on-order")
    public List<ProductsOnPurchaseOrder> updateProductsOnOrder(@RequestBody List<ProductDelivered> productsDelivered) {
        return purchaseOrderService.updateProductsOnOrder(productsDelivered);
    }

    @PatchMapping("cancel/{poId}")
    public PurchaseOrder cancelPurchaseOrder(@PathVariable Long poId) {
        return purchaseOrderService.cancelPurchaseOrder(poId);
    }

    @GetMapping("for-supplier/{supplierId}")
    public List<PurchaseOrder> getPurchaseOrdersForSupplier(@PathVariable Long supplierId) {
        return purchaseOrderService.getPurchaseOrdersForSupplier(supplierId);
    }

    @GetMapping("/consumable-summary")
    @Operation(summary = "Dashboard - Get consumables purchase values summary for a date range (last 30 days by default)")
    @Transactional(readOnly = true)
    public List<ChartSingleData> getConsumableSummaryByDateRange(
            @Parameter(description = "Start date of the period (yyyy-MM-dd)", example = "2023-01-01") @RequestParam(value = "startDate", required = false) String startDateStr,
            @Parameter(description = "End date of the period (yyyy-MM-dd)", example = "2023-01-31") @RequestParam(value = "endDate", required = false) String endDateStr) {

        logger.info("Getting consumable summary for the date range: " + startDateStr + " to " + endDateStr);

        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        return purchaseOrdersSummaryService.consumableSummaryForPeriod(startDate, endDate);
    }

    @GetMapping("/steel-coil-summary")
    @Operation(summary = "Dashboard - Get steel coil purchase summary details for a date range (last 30 days by default)")
    @Transactional(readOnly = true)
    public List<ChartSingleData> getSteelCoilSummary(
            @Parameter(description = "Start date of the period (yyyy-MM-dd)", example = "2023-01-01") @RequestParam(value = "startDate", required = false) String startDateStr,
            @Parameter(description = "End date of the period (yyyy-MM-dd)", example = "2023-01-31") @RequestParam(value = "endDate", required = false) String endDateStr) {

        logger.info("Getting steel coil summary for the date range: " + startDateStr + " to " + endDateStr);

        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        return purchaseOrdersSummaryService.steelCoilSummaryForPeriod(startDate, endDate);
    }

    @GetMapping("/monthly-summary")
    @Operation(summary = "Dashboard - Get purchase order summary with customizable date range (30-day default)")
    @Transactional(readOnly = true)
    public List<ChartMultiData> getMonthlyOrderSummary(
            @Parameter(description = "Start date of the period (yyyy-MM-dd)", example = "2023-01-01") @RequestParam(value = "startDate", required = false) String startDateStr,
            @Parameter(description = "End date of the period (yyyy-MM-dd)", example = "2023-01-31") @RequestParam(value = "endDate", required = false) String endDateStr) {
        logger.info("Getting monthly order summary for the date range: " + startDateStr + " to " + endDateStr);

        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        return purchaseOrdersSummaryService.orderSummaryForPeriod(startDate, endDate);
    }


    @DeleteMapping("{id}")
    public void deletePurchaseOrder(@Parameter(description = "Purchase Order ID") @PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);

    }

}
