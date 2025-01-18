package com.axe.stocks;

import com.axe.purchaseOrders.DTOs.StockOnOrderDetails;
import com.axe.stocks.stocksDTO.SteelCoilsInStock;
import com.axe.stocks.stocksDTO.StockMovementData;
import com.axe.stocks.stocksDTO.StocksDTO;
import com.axe.warehouse.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class StocksController {

    private final StocksService stocksService;
    private final WarehouseService warehouseService;

    public StocksController(StocksService stocksService, WarehouseService warehouseService) {
        this.stocksService = stocksService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("in-stock")
    public List<StocksDTO> getAllStockOnHand(){
        return stocksService.getAllStockOnHand();
    }

    @GetMapping("stock-on-order")
    public List<StockOnOrderDetails> getStockOnOrder(){
        return stocksService.getStockOnOrder();
    }

    @GetMapping("")
    public List<SteelCoilsInStock> getAllStock(){
        return warehouseService.getAllStock();
    }

    @GetMapping("movement")
    public List<StockMovementData> getStockMovement(){
        return stocksService.getStockMovement();
    }


}
