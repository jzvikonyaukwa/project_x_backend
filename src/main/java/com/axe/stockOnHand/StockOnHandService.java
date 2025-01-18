package com.axe.stockOnHand;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.aggregated_products.AggregatedProductsService;
import com.axe.product.Product;
import com.axe.product.services.ProductService;
import com.axe.stockOnHand.DTO.MadeProduct;
import com.axe.stockOnHand.DTO.ProductForStockOnHandDTO;
import com.axe.stockOnHand.DTO.ProductPickedDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StockOnHandService {

    private final StockOnHandRepository stockOnHandRepository;
    private final AggregatedProductsService aggregatedProductsService;
    private final ProductService productService;

    Logger logger = LoggerFactory.getLogger(StockOnHandService.class);

    public StockOnHandService(StockOnHandRepository stockOnHandRepository,
                              AggregatedProductsService aggregatedProductsService,
                              ProductService productService) {
        this.stockOnHandRepository = stockOnHandRepository;
        this.aggregatedProductsService = aggregatedProductsService;
        this.productService = productService;
    }

    public List<StockOnHand> getAllStockOnHand() {
        return stockOnHandRepository.findAll();
    }

    public StockOnHand saveStockOnHand(StockOnHand stockOnHand) {
        return stockOnHandRepository.save(stockOnHand);
    }

    @Transactional
    public StockOnHand addProductToStockOnHand(ProductForStockOnHandDTO product) {

        logger.info("createProductForStockOnHand: {}", product);
        StockOnHand stockOnHand = new StockOnHand();
        stockOnHand.setProductName(product.getProductName());
        stockOnHand.setDateAdded(LocalDate.now());

        AggregatedProduct aggregatedProduct
                = aggregatedProductsService.createAggregatedProduct(product);

        stockOnHand.setAggregatedProduct(aggregatedProduct);
        stockOnHand.setStatus("available");
        return saveStockOnHand(stockOnHand);
    }

    public List<MadeProduct> getAllMadeProductsInStockOnHand() {
        return stockOnHandRepository.getAllMadeProductsInStockOnHand();
    }

    public List<StockOnHand> getStockOnHandForProjectName(String productType, String color, BigDecimal gauge) {

        logger.info("productName = {} color = {} gauge = {}", productType, color, gauge);
        List<StockOnHand> madeProducts = stockOnHandRepository.getStockOnHandForPlanName(productType, color, gauge, "available");
        logger.info("madeProducts.size() = {}", madeProducts.size());
        return madeProducts;
    }

    @Transactional
    public AggregatedProduct productPicked(ProductPickedDTO productPickedDTO) {

        logger.info("productPicked: {}", productPickedDTO);

        AggregatedProduct aggregatedProductToDelete = aggregatedProductsService.getAggregatedProductById(productPickedDTO.getAggrProdId());
        StockOnHand stockOnHandItem = findStockOnHand(productPickedDTO.getStockOnHandId());

        AggregatedProduct aggregatedProductInStockOnHand = stockOnHandItem.getAggregatedProduct();

        validateAggregatedProductLength(aggregatedProductInStockOnHand, aggregatedProductToDelete);

        Product product = productService.getProductById(productPickedDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        removeAggregatedProductFromProduct(product, aggregatedProductToDelete);

        aggregatedProductInStockOnHand.setProduct(product);
        aggregatedProductInStockOnHand.setStick(aggregatedProductToDelete.getStick());
        aggregatedProductInStockOnHand.setStickType(aggregatedProductToDelete.getStickType());
        aggregatedProductInStockOnHand.setCode(aggregatedProductToDelete.getCode());

        AggregatedProduct updatedAggregatedProduct = aggregatedProductsService.saveAggregatedProduct(aggregatedProductInStockOnHand);

        boolean checkIfAllCompleted = updatedAggregatedProduct.getProduct().getAggregatedProducts().stream().allMatch(a->a.getStatus().equals("completed"));

        logger.info("checkIfAllCompleted: {}", checkIfAllCompleted);
        if(checkIfAllCompleted){
            updatedAggregatedProduct.getProduct().setStatus("completed");
            aggregatedProductsService.createInventoryItem(updatedAggregatedProduct.getProduct(), LocalDateTime.now());
        }

        updateStockOnHandItem(stockOnHandItem);

        logger.info("Attempting to delete AggregatedProduct with ID: {}", updatedAggregatedProduct.getId());
        aggregatedProductsService.deleteAggregatedProduct(aggregatedProductToDelete.getId());

        logger.info("Product picking process completed successfully");
        return updatedAggregatedProduct;
    }

    private StockOnHand findStockOnHand(Long id) {
        return stockOnHandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Stock on hand item not found"));
    }

    private void validateAggregatedProductLength(AggregatedProduct inStock, AggregatedProduct toDelete) {
        if (!Objects.equals(inStock.getLength(), toDelete.getLength())) {
            throw new ValidationException("Aggregated product lengths do not match: " +
                    "in stock length = " + inStock.getLength() +
                    ", to delete length = " + toDelete.getLength());
        }
    }

    private void removeAggregatedProductFromProduct(Product product, AggregatedProduct aggregatedProductToDelete) {
        product.getAggregatedProducts().remove(aggregatedProductToDelete);
        productService.saveProduct(product);
    }

    private void updateStockOnHandItem(StockOnHand stockOnHandItem) {
        stockOnHandItem.setStatus("picked");
        stockOnHandItem.setDatePicked(LocalDate.now());
        stockOnHandRepository.save(stockOnHandItem);
    }

    public List<StockOnHand> getAvailableAllStockOnHand() {
        return stockOnHandRepository.getAvailableAllStockOnHand();
    }
}
