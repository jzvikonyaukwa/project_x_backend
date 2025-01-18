package com.axe.warehouse;

import com.axe.stocks.stocksDTO.SteelCoilsInStock;
import com.axe.stocks.stocksDTO.StocksDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<Warehouse> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public List<StocksDTO> getWarehouseStock(Long warehouseId) {
        return warehouseRepository.getWarehouseStock(warehouseId);
    }

    public List<StocksDTO> getAllStock(Long warehouseId, Float width ) {
        return warehouseRepository.getFilteredWarehouseStock(warehouseId, width);
    }

    public List<SteelCoilsInStock> getAllStock() {
        return warehouseRepository.getAllStock();
    }

    public Warehouse getWarehouseByName(String warehouseName) {
        return warehouseRepository.getWarehouseByName(warehouseName);
    }
}
