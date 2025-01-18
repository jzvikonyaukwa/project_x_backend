package com.axe.product_type;

import com.axe.product_type.productTypeDTO.ProductTypeDTO;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }


    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeRepository.findAll(
            Sort.by(Sort.Order.asc("name"))
        ).stream()
                .map(pt -> new ProductTypeDTO(
                        pt.getId(),
                        pt.getName(),
                        pt.getCode(),
                        (pt.getCategory() != null) ? pt.getCategory().getName() : null
                ))
                .toList();
    }

    public Optional<ProductType> getProductTypeById(Long id) {

        return productTypeRepository.findById(id);
    }

    public ProductType getProductTypeByName(String planName) {
        return productTypeRepository.findByName(planName).orElseThrow(()-> new RuntimeException("Can not find product type with name [%s]".formatted(planName)));
    }


    // To remove 1 method
    public ProductType findProductTypeById(Long id) {
        return productTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductType not found with id " + id));
    }

    public ProductType createProductType(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    public ProductType updateProductType(Long id, ProductType updatedProductType) {
        return productTypeRepository.findById(id)
                .map(productType -> {
                    productType.setName(updatedProductType.getName());
                    productType.setCode(updatedProductType.getCode());
                    productType.setCategory(updatedProductType.getCategory());
                    return productTypeRepository.save(productType);
                })
                .orElseThrow(() -> new RuntimeException("ProductType not found with id " + id));
    }


    public void deleteProductType(Long id) {
        productTypeRepository.deleteById(id);
    }
}
