package com.axe.productTransactions;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.missing_metres.MissingMetres;
import com.axe.productTransactions.dtos.*;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilRepository;
import com.axe.steelCoils.exceptions.SteelCoilNotFoundException;
import com.axe.wastage.Wastage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductTransactionsService {
    private static final String COIL_NOT_FOUND_MESSAGE = "Steel coil with id [%s] not found";
    private static final String INSUFFICIENT_STEEL_MESSAGE = "Not enough steel on coil. Required: %s, Available: %s";

    private final Logger logger = LoggerFactory.getLogger(ProductTransactionsService.class);
    private final ProductTransactionsRepository productTransactionsRepository;
    private final SteelCoilRepository steelCoilRepository;

    public ProductTransactionsService(ProductTransactionsRepository productTransactionsRepository,
                                      SteelCoilRepository steelCoilRepository) {
        this.productTransactionsRepository = productTransactionsRepository;
        this.steelCoilRepository = steelCoilRepository;
    }

    public ProductTransaction save(ProductTransaction productTransaction) {
        Assert.notNull(productTransaction, "ProductTransaction cannot be null");
        return productTransactionsRepository.save(productTransaction);
    }

    public ProductTransaction createProductTransaction(Long coilId, LocalDateTime date,
                                                       AggregatedProduct amp) {
        validateInputs(coilId, date, amp);
        return createTransactionWithLength(coilId, date, amp.getLength(), builder ->
                builder.aggregatedProduct(amp));
    }


    public ProductTransaction createProductTransaction(Long coilId, LocalDateTime date,
                                                       Wastage wastage) {
        validateInputs(coilId, date, wastage);
        return createTransactionWithLength(coilId, date, wastage.getMtrsWaste(), builder ->
                builder.wastage(wastage));
    }

    public ProductTransaction createProductTransaction(Long coilId, LocalDateTime date,
                                                       MissingMetres missingMetres) {
        validateInputs(coilId, date, missingMetres);
        return createTransactionWithLength(coilId, date, missingMetres.getMtrsMissing(), builder ->
                builder.missingMetres(missingMetres));
    }

    public ProductTransactionResponse getProductTransactionByPagination(int pageNo, int pageSize) {
        validatePaginationParams(pageNo, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<ProductTransaction> productTransactions = productTransactionsRepository.findAll(pageRequest);
        return new ProductTransactionResponse(
                productTransactions.getContent(),
                productTransactions.getTotalElements()
        );
    }

    public ProductTransactionDetailsResponse getAllProductTransactionDetailsByPagination(
            int pageNo, int pageSize, String filters) {
        validatePaginationParams(pageNo, pageSize);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<ProductTransactionDetails> productTransactionsDetails =
                productTransactionsRepository.getFilteredProductTransactionDetails(pageRequest, filters);
        return new ProductTransactionDetailsResponse(
                productTransactionsDetails.getContent(),
                productTransactionsDetails.getTotalElements()
        );
    }

    public List<TransactionDTO> getProductTransactionsForThisWidth(int width) {
        Assert.isTrue(width > 0, "Width must be greater than 0");
        return productTransactionsRepository.getProductTransactionsForThisWidth(width);
    }

    private ProductTransaction createTransactionWithLength(
            Long coilId,
            LocalDateTime date,
            BigDecimal length,
            java.util.function.Function<ProductTransaction.ProductTransactionBuilder,
                    ProductTransaction.ProductTransactionBuilder> builderFunction) {

        SteelCoil steelCoil = getSteelCoil(coilId);
        handleSteelCoilsBalance(steelCoil, length);
        checkAndUpdateStatus(steelCoil);

        ProductTransaction.ProductTransactionBuilder builder = ProductTransaction.builder()
                .date(date)
                .steelCoil(steelCoil);

        return builderFunction.apply(builder).build();
    }

    private SteelCoil getSteelCoil(Long coilId) {
        return steelCoilRepository.findById(coilId)
                .orElseThrow(() -> new SteelCoilNotFoundException(
                        String.format(COIL_NOT_FOUND_MESSAGE, coilId)));
    }

    private void handleSteelCoilsBalance(SteelCoil steelCoil, BigDecimal length) {
        logger.debug("Handling steel coils balance. Length to be subtracted = {}", length);

        BigDecimal remainingAfterSubtraction = steelCoil.getEstimatedMetersRemaining().subtract(length);
        if (remainingAfterSubtraction.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientSteelException(
                    String.format(INSUFFICIENT_STEEL_MESSAGE,
                            length,
                            steelCoil.getEstimatedMetersRemaining()
                    )
            );
        }

        steelCoil.setEstimatedMetersRemaining(remainingAfterSubtraction);
        steelCoilRepository.save(steelCoil);
    }

    private void checkAndUpdateStatus(SteelCoil steelCoil) {
        if (steelCoil.getEstimatedMetersRemaining().compareTo(BigDecimal.ZERO) <= 0) {
            steelCoil.setStatus("completed");
            steelCoilRepository.save(steelCoil);
            logger.info("Steel coil {} marked as finished", steelCoil.getId());
        }
    }

    private void validateInputs(Long coilId, LocalDateTime date, Object transactionObject) {
        Assert.notNull(coilId, "Coil ID cannot be null");
        Assert.notNull(date, "Date cannot be null");
        Assert.notNull(transactionObject, "Transaction object cannot be null");
    }

    private void validatePaginationParams(int pageNo, int pageSize) {
        Assert.isTrue(pageNo >= 0, "Page number must be non-negative");
        Assert.isTrue(pageSize > 0, "Page size must be greater than 0");
    }
}

class InsufficientSteelException extends RuntimeException {
    public InsufficientSteelException(String message) {
        super(message);
    }
}