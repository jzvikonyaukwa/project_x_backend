package com.axe.product_category;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public Optional<ProductCategory> getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory createProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    public ProductCategory updateProductCategory(Long id, ProductCategory updatedCategory) {
        return productCategoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    return productCategoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("ProductCategory not found with id " + id));
    }

    public void deleteProductCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }
}
