package com.axe.stocks;

import com.axe.purchaseOrders.DTOs.StockOnOrderDetails;
import com.axe.purchaseOrders.services.PurchaseOrderService;
import com.axe.steelCoils.SteelCoilService;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.stocks.stocksDTO.StockMovementData;
import com.axe.stocks.stocksDTO.StocksDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StocksService {

    private final SteelSpecificationService steelSpecificationService;
    private final PurchaseOrderService purchaseOrderService;

    private final SteelCoilService steelCoilService;

    public StocksService(SteelSpecificationService steelSpecificationService,
                         PurchaseOrderService purchaseOrderService, SteelCoilService steelCoilService) {
        this.steelSpecificationService = steelSpecificationService;
        this.purchaseOrderService = purchaseOrderService;
        this.steelCoilService = steelCoilService;
    }

    public List<StocksDTO> getAllStockOnHand() {
        return steelSpecificationService.getStockOnHand();
    }

    public List<StockOnOrderDetails> getStockOnOrder() {
        return purchaseOrderService.getStockOnOrder();
    }

    public List<StockMovementData> getStockMovement() {
        return steelCoilService.getStockMovement();
    }

}
