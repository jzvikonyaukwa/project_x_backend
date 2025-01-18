package com.axe.product;


import com.auth0.jwt.JWT;
import com.axe.product.productDTO.*;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.ManufacturedProductStatusCountDTO;
import com.axe.product.productDTO.manufacturedProductSummaryDTO.MonthlyManufacturedProductCountDTO;
import com.axe.product.services.*;
import com.axe.utilities.ClientVisibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ProductController {
    private final ProductService productService;
    private final CreateProductService createProductService;

    private final UpdateProductService updateProductService;

    private final ProductSummaryService productSummaryService;

    private final ReservedStockProductService reservedStockProductService;

    private final Logger logger = LoggerFactory.getLogger(Product.class);

    public ProductController(ProductService productService,
                             CreateProductService createProductService,
                             UpdateProductService updateProductService,
                             ProductSummaryService productSummaryService,
                             ReservedStockProductService reservedStockProductService
                             ) {
        this.productService = productService;
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.productSummaryService = productSummaryService;
        this.reservedStockProductService = reservedStockProductService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductInformationDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductInformation(id));
    }


    @PostMapping("/create-product-v2/{quoteId}")
    public Product createProductV2(@RequestHeader(AUTHORIZATION) String header,@RequestBody CreateProductDTO createProductDTO, @PathVariable Long quoteId){
        logger.info("IN V2 createProductV2");

        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return createProductService.createProduct(userEmail,createProductDTO, quoteId);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while accepting quote");
        }

    }

    @PostMapping("/create-pasted-product-v2/{quoteId}")
    public List<Product> createPastedProductV2(@RequestHeader(AUTHORIZATION) String header,@RequestBody PastedRequestDTO pastedRequestDTOS, @PathVariable Long quoteId){
        logger.info("IN V2 createPastedProductV2");

        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return createProductService.createProduct(userEmail, pastedRequestDTOS, quoteId);

       } catch (ClientVisibleException e) {
        throw new RuntimeException(e.getLocalizedMessage());
       } catch (Exception e) {
        throw new RuntimeException("Server encountered an error while accepting quote");
      }
     }


    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            Product product = productService.updateProduct(id, updatedProduct);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete-product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("update-product-price")
    public Product updateProductPrice(@RequestHeader(AUTHORIZATION) String header,@RequestBody PriceAndProductTypeDTO priceAndProductTypeDTO){
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
        return productService.updateProductPrice(userEmail,priceAndProductTypeDTO);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while accepting quote");
        }
    }


    @PutMapping("update/quote-v2/{quoteId}")
    public Product updateProduct(@RequestHeader(AUTHORIZATION) String header,@RequestBody CreateProductDTO createProductDTO, @PathVariable Long quoteId){
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
              return updateProductService.updateProduct(userEmail,createProductDTO, quoteId);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while accepting quote");
        }
    }

    @PatchMapping("change-status/{productId}/{status}")
    public Product changeCuttingListStatus(@PathVariable Long productId, @PathVariable String status){
        return productService.changeProductStatus(productId, status);
    }

    @GetMapping("in-progress/width/{width}")
    public ProductSummaryDetailsDTO getProductInProgressForMachine(@PathVariable Integer width){
        logger.info("ProductController: getProductInProgressForMachine: width: " + width);
        return productService.getProductInProgressForMachine(width);
    }

    @GetMapping("/width/{width}")
    public List<ProductSummaryDetailsDTO> getCuttingListsScheduledForProduct(@PathVariable Integer width){
        return productService.getProductsScheduledForProduct(width);
    }

    @PatchMapping("product-manufactured")
    public Product productManufactured(@RequestBody ProductPostDTO product){
        logger.info("Product to be manufactured :::::::: {}" , product);
        return productService.productManufactured(product);
    }

    @PatchMapping("change-priority/{productId}/{priority}")
    public Product changeCuttingListPriority(@PathVariable Long productId, @PathVariable String priority){
        return productService.changeProductPriority(productId, priority);
    }

    // Manufactured Product Summary
    @GetMapping("/count-by-type")
    public List<ManufacturedProductCountDTO> getManufacturedProductCountByType(@RequestParam(value = "startDate", required = false) String startDateStr,
                                                                               @RequestParam(value = "endDate",required = false)String endDateStr) {
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);
        logger.info("Getting manufactured product count by type between {} and {}", startDate, endDate);
        return productSummaryService.getManufacturedProductCountByType(startDate, endDate);
    }

    @GetMapping("/status-count")
    public List<ManufacturedProductStatusCountDTO> getManufacturedProductStatusCount(@RequestParam(value = "startDate", required = false) String startDateStr,
                                                                                     @RequestParam(value = "endDate",required = false)String endDateStr) {
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);
        return productSummaryService.getManufacturedProductStatusCount(startDate, endDate);
    }

    @GetMapping("/monthly-count-by-type")
    public List<MonthlyManufacturedProductCountDTO> getManufacturedProductCountByMonthAndType(@RequestParam(value = "year", required = false) Integer year) {
        if(year == null){
            year = Year.now().getValue();
        }
        return productSummaryService.getManufacturedProductCountByMonthAndType(year);
    }

    @GetMapping("/completed")
    public ProductCompletedResponse getAllCompletedProducts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String filters
            ) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        return productService.getAllCompletedProducts(pageable,filters);
    }
    @PutMapping("/{id}/{newStatus}")
    public Product updateCuttingListReserveStock(@PathVariable Long id, @PathVariable Boolean newStatus){
        return reservedStockProductService.updateProductReserveStock(id, newStatus);
    }

    @PatchMapping("{id}/change-status/{status}")
    public Product manufacturedProductStatusChange(@PathVariable Long id, @PathVariable String status){
        return productService.manufacturedProductStatusChange(id, status);
    }

    @GetMapping("average-price/{widthId}/{gaugeId}/{colorId}")
    public BigDecimal getAveragePriceForProduct(@PathVariable Long widthId, @PathVariable Long gaugeId,
                                                @PathVariable Long colorId){
        return productService.calculateWeightedAvgCostForSteelType(widthId, gaugeId, colorId);
    }

    private String extractEmailFromAuthorizationHeader(final String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = header.substring(7);
        return JWT.decode(token).getClaim("email").asString();
    }

}
