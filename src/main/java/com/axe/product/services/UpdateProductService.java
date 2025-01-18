package com.axe.product.services;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.price_history.PriceHistoryService;
import com.axe.product.productDTO.CreateProductDTO;


import com.axe.product.Product;
import com.axe.product.ProductRepository;
import com.axe.quotes.Quote;
import com.axe.quotes.services.QuotesService;
import com.axe.steelCoils.SteelCoilService;
import com.axe.users.User;
import com.axe.users.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.axe.product.services.utils.ProductServiceUtils.*;




import java.util.*;

@Service
public class UpdateProductService {
    private static final Logger logger = LoggerFactory.getLogger(UpdateProductService.class);
    private static final BigDecimal SEGMENT_LENGTH = BigDecimal.valueOf(6);
    private static final String SCHEDULED_STATUS = "scheduled";
    private static final String NORMAL_PRIORITY = "normal";
    private final ProductRepository productRepository;

    private final SteelCoilService steelCoilService;

    private final PriceHistoryService priceHistoryService;
    private final UsersService usersService;


    private final QuotesService quotesService;


    public UpdateProductService(ProductRepository productRepository,
                                SteelCoilService steelCoilService,
                                QuotesService quotesService,
                                PriceHistoryService priceHistoryService,
                                UsersService usersService
    ){
        this.productRepository = productRepository;
        this.steelCoilService = steelCoilService;
        this.quotesService = quotesService;
        this.priceHistoryService = priceHistoryService;
        this.usersService = usersService;
    }

    private BigDecimal adjustLengthForProductType(BigDecimal totalLength) {
        BigDecimal cutsNeeded = totalLength.divide(SEGMENT_LENGTH, 0, RoundingMode.HALF_UP);
        logger.info("cutsNeeded = {}", cutsNeeded.multiply(SEGMENT_LENGTH));
        return cutsNeeded.multiply(SEGMENT_LENGTH);
    }


    public Product updateProduct(String userEmail,CreateProductDTO createProductDTO, Long quoteId) {
        logger.info("Updating product: {} with quote ID: {}",
                createProductDTO.getProductType().getName(), quoteId);

        // Fetch both the product and quote in a single transaction
        Product existingProduct = productRepository.findById(createProductDTO.getId())
                .orElseThrow(() -> new RuntimeException(
                        "Product list with id [%s] not found".formatted(createProductDTO.getId())));

        Quote quote = quotesService.getQuote(quoteId);
        if(quote == null) throw new RuntimeException("Quote with id [%s] not found".formatted(quoteId));

        Optional<User> optionalUser = usersService.getUserByEmail(userEmail);
        User user = optionalUser.orElseThrow(() ->
                new RuntimeException("No user found with email: " + userEmail));

        BigDecimal oldPrice = existingProduct.getSellPrice() == null
                ? BigDecimal.ZERO
                : existingProduct.getSellPrice();

        // Update the product data
        updateProductDetails(existingProduct, createProductDTO);

        // Make sure the quote relationship is maintained
        existingProduct.setQuote(quote);

        Product updatedProduct = productRepository.save(existingProduct);


        if (didPriceChange(oldPrice, updatedProduct.getSellPrice())) {
            priceHistoryService.recordPriceChange(
                    user.getId(),
                    updatedProduct.getId(),
                    oldPrice,
                    updatedProduct.getSellPrice()
            );
            logger.info("Price changed from {} to {} for productId={}, recorded by userId={}",
                    oldPrice, updatedProduct.getSellPrice(),
                    updatedProduct.getId(), user.getId());
        }

        // Save and return
        return updatedProduct;
    }


    private boolean didPriceChange(BigDecimal oldPrice, BigDecimal newPrice) {
        if (newPrice == null) {
            return false;
        }

        return oldPrice.compareTo(newPrice) != 0;
    }


    private void updateProductDetails(Product existingProduct, CreateProductDTO createProductDTO) {
        String productTypeName= createProductDTO.getProductType().getName();
        // Update basic product attributes
        existingProduct.setStatus(SCHEDULED_STATUS);
        existingProduct.setPriority(NORMAL_PRIORITY);
        existingProduct.setPlanName(productTypeName);
        existingProduct.setProfile(createProductDTO.getProfile());
        existingProduct.setColor(createProductDTO.getColor());
        existingProduct.setCanInvoice(true);
        existingProduct.setGauge(createProductDTO.getGauge());
        existingProduct.setWidth(createProductDTO.getWidth());
        existingProduct.setTargetDate(createProductDTO.getTargetDate());
        existingProduct.setTotalLength(createProductDTO.getTotalLength());
        existingProduct.setSellPrice(createProductDTO.getSellPrice());
        existingProduct.setCostPrice(createProductDTO.getCostPrice());

        // Handle aggregated products
        List<AggregatedProduct> newAggregatedProducts = createAggregatedProducts(createProductDTO, existingProduct, productTypeName);

        // Remove old aggregated products
        List<AggregatedProduct> existingAggregatedProducts = new ArrayList<>(existingProduct.getAggregatedProducts());
        for (AggregatedProduct aggregatedProduct : existingAggregatedProducts) {
            existingProduct.removeAggregatedProduct(aggregatedProduct);
        }

        // Add new aggregated products
        for (AggregatedProduct newAggregatedProduct : newAggregatedProducts) {
            existingProduct.addAggregatedProduct(newAggregatedProduct);
        }

        // Handle stock and pricing
        handleStockAndPricing(existingProduct, createProductDTO);

    }




    private void handleStockAndPricing(Product existingProduct, CreateProductDTO createProductDTO) {
        BigDecimal totalLength = createProductDTO.getTotalLength();
        String productTypeName = createProductDTO.getProductType().getName();

        // Adjust length for specific product types
        if (isSegmentedProductType(productTypeName)) {
            totalLength = adjustLengthForProductType(totalLength);
        }

        BigDecimal width = existingProduct.getWidth().getWidth();
        if (checkStockAvailability(steelCoilService, createProductDTO.getColor(), createProductDTO.getGauge(), totalLength, width)) {
            existingProduct.setCostPrice(createProductDTO.getCostPrice());
            existingProduct.setSellPrice(createProductDTO.getSellPrice());
        }
    }

    private boolean isSegmentedProductType(String productTypeName) {
        return "Battens".equals(productTypeName) || "Purlins".equals(productTypeName);
    }


}
