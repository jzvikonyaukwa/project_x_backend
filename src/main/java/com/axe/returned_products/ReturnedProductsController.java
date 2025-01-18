package com.axe.returned_products;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returned-products")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ReturnedProductsController {

//
//    private final ReturnedProductsService returnedProductsService;
//    ReturnedProductsController(ReturnedProductsService returnedProductsService){
//        this.returnedProductsService =returnedProductsService;
//    }
//
//    @PostMapping
//    public ResponseEntity<ReturnedProducts> createReturnedProduct(@RequestBody ReturnedProducts returnedProduct) {
//        return ResponseEntity.ok(returnedProductsService.createReturnedProduct(returnedProduct));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ReturnedProducts>> getAllReturnedProducts() {
//        return ResponseEntity.ok(returnedProductsService.getReturnedProduct());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ReturnedProducts> getReturnedProductById(@PathVariable Long id) {
//        return ResponseEntity.ok(returnedProductsService.getReturnedProductById(id));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ReturnedProducts> updateReturnedProduct(@PathVariable Long id, @RequestBody ReturnedProducts returnedProductDetails) {
//      return ResponseEntity.ok(returnedProductsService.updateReturnedProducts(id, returnedProductDetails));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteReturnedProduct(@PathVariable Long id) {
//        returnedProductsService.deleteReturnedProduct(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/by-delivery-note/{deliveryNoteId}")
//    public ResponseEntity<List<ReturnedProducts>> getReturnedProductsByDeliveryNoteId(@PathVariable Long deliveryNoteId) {
//        List<ReturnedProducts> returnedProducts = returnedProductsService.getReturnedProductsByDeliveryNoteId(deliveryNoteId);
//        return ResponseEntity.ok(returnedProducts);
//    }
//
//    @GetMapping("/by-credit-note/{creditNoteId}")
//    public ResponseEntity<List<ReturnedProducts>> getReturnedProductsByCreditNoteId(@PathVariable Long creditNoteId) {
//        List<ReturnedProducts> returnedProducts = returnedProductsService.getReturnedProductsByCreditNoteId(creditNoteId);
//        return ResponseEntity.ok(returnedProducts);
//    }
}
