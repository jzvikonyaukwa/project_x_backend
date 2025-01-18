package com.axe.aggregated_products;

import com.axe.product.productDTO.ProductPostDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/aggregated-products")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class AggregatedProductController {
    private final AggregatedProductsService aggregatedProductService;

    public AggregatedProductController(AggregatedProductsService aggregatedProductService) {
        this.aggregatedProductService = aggregatedProductService;
    }

    @PatchMapping("product-manufactured")
    public AggregatedProduct productManufactured(@RequestBody ProductPostDTO product){
        return aggregatedProductService.manufacturedAggregatedManufacturedProduct(product);
    }
}
