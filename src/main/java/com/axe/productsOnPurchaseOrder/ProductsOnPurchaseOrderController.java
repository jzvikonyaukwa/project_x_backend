package com.axe.productsOnPurchaseOrder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products-on-purchase-orders")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProductsOnPurchaseOrderController {

    private final ProductsOnPurchaseOrderService productsOnPurchaseOrderService;

    public ProductsOnPurchaseOrderController(ProductsOnPurchaseOrderService productsOnPurchaseOrderService) {
        this.productsOnPurchaseOrderService = productsOnPurchaseOrderService;
    }

    @PatchMapping("change-status/{id}/new-status/{newStatus}")
    public ProductsOnPurchaseOrder changeProductStatus(@PathVariable Long id, @PathVariable String newStatus){
        return productsOnPurchaseOrderService.changeProductStatus(newStatus, id);
    }

}
