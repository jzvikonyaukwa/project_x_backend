package com.axe.productsOnPurchaseOrder;

import org.springframework.stereotype.Service;

@Service
public class ProductsOnPurchaseOrderService {

    private final ProductsOnPurchaseOrderRepository productsOnPurchaseOrderRepository;

    public ProductsOnPurchaseOrderService(ProductsOnPurchaseOrderRepository productsOnPurchaseOrderRepository) {
        this.productsOnPurchaseOrderRepository = productsOnPurchaseOrderRepository;
    }

//    public ProductsOnPurchaseOrder addProductsOnPurchaseOrder(ProductsOnPurchaseOrder productsOnPurchaseOrder) {
//        return productsOnPurchaseOrderRepository.save(productsOnPurchaseOrder);
//    }

    public ProductsOnPurchaseOrder getProductOnPurchaseOrderById(Long id){
        return productsOnPurchaseOrderRepository.findById(id).orElse(null);
    }

    public ProductsOnPurchaseOrder updateProductOnPurchaseOrder(ProductsOnPurchaseOrder product){
        return productsOnPurchaseOrderRepository.save(product);
    }

    public ProductsOnPurchaseOrder changeProductStatus(String newStatus, Long id) {
        ProductsOnPurchaseOrder product = productsOnPurchaseOrderRepository.findById(id).orElse(null);
        assert product != null;
        product.setStatus(newStatus);
        return updateProductOnPurchaseOrder(product);
    }
}
