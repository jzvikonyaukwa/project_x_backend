package com.axe.warehouse;

import com.axe.stocks.stocksDTO.StocksDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("all-warehouses")
    public List<Warehouse> getAllWarehouses(){
        return warehouseService.getAllWarehouses();
    }

    @GetMapping("warehouse-stock/{warehouseId}")
    public List<StocksDTO> getWarehouseStock(@PathVariable Long warehouseId){
        return warehouseService.getWarehouseStock(warehouseId);
    }
    @GetMapping("stock/product/{warehouseId}/{width}")
    public List<StocksDTO> getAllWarehouseStock(@PathVariable Long warehouseId,@PathVariable Float width){
        return warehouseService.getAllStock(warehouseId,width);
    }

}
