package com.axe.stockOnHand;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.product.Product;
import com.axe.stockOnHand.DTO.MadeProduct;
import com.axe.stockOnHand.DTO.PickedStockPostDTO;
import com.axe.stockOnHand.DTO.ProductForStockOnHandDTO;
import com.axe.stockOnHand.DTO.ProductPickedDTO;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/stock-on-hand")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class StockOnHandController {

    private final StockOnHandService stockOnHandService;

    public StockOnHandController(StockOnHandService stockOnHandService) {
        this.stockOnHandService = stockOnHandService;
    }

    @GetMapping("")
    public List<StockOnHand> getAllStockOnHand(){
        return stockOnHandService.getAllStockOnHand();
    }

    @GetMapping("/available")
    public List<StockOnHand> getAllAvailableStockOnHand(){
        return stockOnHandService.getAvailableAllStockOnHand();
    }

    @PostMapping("")
    public StockOnHand addProductToStockOnHand(@RequestBody ProductForStockOnHandDTO product){
        return stockOnHandService.addProductToStockOnHand(product);
    }

    @GetMapping("/products")
    public List<MadeProduct> getAllMadeProductsInStockOnHand(){
        return stockOnHandService.getAllMadeProductsInStockOnHand();
    }

//    this.baseUrl + '/product-type/' + productType + '/' + color + '/' + gauge,
    @GetMapping("/product-type/{productType}/{color}/{gauge}")
    public List<StockOnHand> getStockOnHandForProjectName(@PathVariable String productType, @PathVariable String color,
                                                          @PathVariable BigDecimal gauge){
        return stockOnHandService.getStockOnHandForProjectName(productType, color, gauge);
    }
    @PatchMapping("/product-picked")
    public AggregatedProduct productPicked(@RequestBody ProductPickedDTO productPickedDTO){
        return stockOnHandService.productPicked(productPickedDTO);
    }
}
//product-type/Roof%20Sheet/Traffic%20Green/0.47'