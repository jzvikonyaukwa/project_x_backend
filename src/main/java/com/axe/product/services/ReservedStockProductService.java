package com.axe.product.services;


import com.axe.product.Product;
import com.axe.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReservedStockProductService {
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ReservedStockProductService.class);

    public ReservedStockProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product updateProductReserveStock(Long id, Boolean newStatus) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with id  [%s] not found ".formatted(id)))
                ;

        if(product == null) {
            logger.info("Cutting list with id: " + id + " not found.");
            return null;
        }
        if(!newStatus){
            product.setStatus("not scheduled");
        }
        if(newStatus) {
            product.setStatus("scheduled");
        }

        return productRepository.save(product);
    }
}
