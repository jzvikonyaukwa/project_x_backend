package com.axe.consumable_product;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumableProductService {
    private final ConsumableProductRepository consumableProductRepository;

    public ConsumableProductService(ConsumableProductRepository consumableProductRepository) {
        this.consumableProductRepository = consumableProductRepository;
    }

    public List<ConsumableProduct> getAllConsumableProducts() {
        return consumableProductRepository.findAll();
    }


    public Optional<ConsumableProduct> getConsumableProductById(Long id) {
        return consumableProductRepository.findById(id);
    }


    public ConsumableProduct createConsumableProduct(ConsumableProduct consumableProduct) {
        return consumableProductRepository.save(consumableProduct);
    }


    public ConsumableProduct updateConsumableProduct(Long id, ConsumableProduct updatedConsumableProduct) {
        return consumableProductRepository.findById(id)
                .map(consumableProduct -> {
                    consumableProduct.setName(updatedConsumableProduct.getName());
                    consumableProduct.setSerialNumber(updatedConsumableProduct.getSerialNumber());
                    consumableProduct.setUnit(updatedConsumableProduct.getUnit());
                    return consumableProductRepository.save(consumableProduct);
                })
                .orElseThrow(() -> new RuntimeException("ConsumableProduct not found with id " + id));
    }


    public void deleteConsumableProduct(Long id) {
        consumableProductRepository.deleteById(id);
    }
}
