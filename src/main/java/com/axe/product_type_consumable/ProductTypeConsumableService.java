package com.axe.product_type_consumable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeConsumableService {
    private final ProductTypeConsumableRepository productTypeConsumableRepository;

    public ProductTypeConsumableService(ProductTypeConsumableRepository productTypeConsumableRepository) {
        this.productTypeConsumableRepository = productTypeConsumableRepository;
    }

    public List<ProductTypeConsumable> getAllProductTypeConsumables() {
        return productTypeConsumableRepository.findAll();
    }

    public Optional<ProductTypeConsumable> getProductTypeConsumableById(Long id) {
        return productTypeConsumableRepository.findById(id);
    }

    public ProductTypeConsumable createProductTypeConsumable(ProductTypeConsumable productTypeConsumable) {
        return productTypeConsumableRepository.save(productTypeConsumable);
    }

    public ProductTypeConsumable updateProductTypeConsumable(Long id, ProductTypeConsumable updatedConsumable) {
        return productTypeConsumableRepository.findById(id)
                .map(consumable -> {
//                    consumable.setProductType(updatedConsumable.getProductType());
                    consumable.setConsumableProduct(updatedConsumable.getConsumableProduct());
                    consumable.setQuantity(updatedConsumable.getQuantity());
                    return productTypeConsumableRepository.save(consumable);
                })
                .orElseThrow(() -> new RuntimeException("ProductTypeConsumable not found with id " + id));
    }

    public void deleteProductTypeConsumable(Long id) {
        productTypeConsumableRepository.deleteById(id);
    }
}
