package com.axe.product.services;

import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductStatusCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.MonthlyManufacturedProductCountDTO;
import com.axe.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProductSummaryService {
    private final ProductRepository productRepository;

    public ProductSummaryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ManufacturedProductCountDTO> getManufacturedProductCountByType(LocalDate startDate, LocalDate endDate) {
        return productRepository.findManufacturedProductCountByType(startDate, endDate);
    }

    public List<ManufacturedProductStatusCountDTO> getManufacturedProductStatusCount(LocalDate startDate, LocalDate endDate) {
        return productRepository.findManufacturedProductStatusCount(startDate, endDate);
    }


    public List<MonthlyManufacturedProductCountDTO> getManufacturedProductCountByMonthAndType(int year) {
        return productRepository.findManufacturedProductCountByMonthAndType(year);
    }
}
