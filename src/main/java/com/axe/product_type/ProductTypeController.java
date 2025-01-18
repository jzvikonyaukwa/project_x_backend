package com.axe.product_type;

import com.axe.product_type.productTypeDTO.ProductTypeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-types")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping()
    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeService.getAllProductTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductType> getProductTypeById(@PathVariable Long id) {
        return productTypeService.getProductTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ProductType createProductType(@RequestBody ProductType productType) {
        return productTypeService.createProductType(productType);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductType> updateProductType(@PathVariable Long id, @RequestBody ProductType updatedProductType) {
        try {
            ProductType productType = productTypeService.updateProductType(id, updatedProductType);
            return ResponseEntity.ok(productType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
