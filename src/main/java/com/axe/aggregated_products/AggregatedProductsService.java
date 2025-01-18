package com.axe.aggregated_products;

import com.axe.common.utils.InventoryUtils;
import com.axe.inventories.Inventory;
import com.axe.inventories.InventoryService;
import com.axe.machineEvents.MachineEventsService;
import com.axe.product.Product;
import com.axe.product.productDTO.ProductPostDTO;
import com.axe.productTransactions.ProductTransaction;
import com.axe.productTransactions.ProductTransactionsService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.stockOnHand.DTO.ProductForStockOnHandDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class AggregatedProductsService {

    private final AggregatedProductsRepository aggregatedProductsRepository;
    private final ProductTransactionsService productTransactionsService;
    private final SteelCoilService steelCoilService;
    private  final MachineEventsService machineEventsService;

    private  final InventoryService inventoryService;

    Logger logger = LoggerFactory.getLogger(AggregatedProductsService.class);

    public AggregatedProductsService(
            AggregatedProductsRepository aggregatedProductsRepository,
            ProductTransactionsService productTransactionsService,
            SteelCoilService steelCoilService,
            MachineEventsService machineEventsService,
            InventoryService inventoryService
            ) {
        this.aggregatedProductsRepository = aggregatedProductsRepository;
        this.productTransactionsService = productTransactionsService;
        this.steelCoilService = steelCoilService;
        this.machineEventsService = machineEventsService;
        this.inventoryService = inventoryService;


    }


    public AggregatedProduct createAggregatedProduct(ProductForStockOnHandDTO product) {

        AggregatedProduct aggregatedProduct
                = new AggregatedProduct();
        aggregatedProduct.setStick(product.getStick());
        aggregatedProduct.setStickType(product.getStickType());
        aggregatedProduct.setStatus("completed");
        aggregatedProduct.setLength(product.getLength());

        ProductTransaction productTransaction = createProductTransaction(product.getSteelCoilId(),
                LocalDateTime.now(), aggregatedProduct);
        aggregatedProduct.setProductTransaction(productTransaction);
        return aggregatedProductsRepository.save(aggregatedProduct);
    }

    public AggregatedProduct getAggregatedProductById(Long id) {
        return aggregatedProductsRepository.findById(id).orElseThrow(()-> new RuntimeException("Aggregated Product with id [%s] not found".formatted(id)));
    }

    @Transactional
    public AggregatedProduct manufacturedAggregatedManufacturedProduct(ProductPostDTO product){
        AggregatedProduct amp = getAggregatedProductById(product.getProductId());
        logger.info("AMP: {}", amp);

        LocalDateTime localDateTime = parseManufactureDate(product.getDateManufactured());
        logger.info("LocalDateTime --> {}", localDateTime);

        updateProductDates(amp, localDateTime);
        logger.info("AMP: {}", amp);

        return manufacturedAggregatedProduct(amp, product.getCoilId(), localDateTime);
    }

    public AggregatedProduct manufacturedAggregatedProduct(
            AggregatedProduct amp, Long coilId, LocalDateTime localDateTime) {

        boolean result = checkProductHasNotBeenManufactured(amp);

        if( result ) {
            return amp;
        }

        amp.setStatus("completed");

        ProductTransaction productTransaction = createProductTransaction(coilId, localDateTime, amp);
        amp.setProductTransaction(productTransaction);
        amp.getProduct().setLastWorkedOn(localDateTime);

        boolean checkIfAllCompleted = amp.getProduct().getAggregatedProducts().stream().allMatch(a->a.getStatus().equals("completed"));

        if(checkIfAllCompleted){
            amp.getProduct().setStatus("completed");
            createInventoryItem(amp.getProduct(),LocalDateTime.now());
        }

        return  aggregatedProductsRepository.save(amp);
    }


    public void createInventoryItem(Product product, LocalDateTime dateTime) {
             Inventory inventory = InventoryUtils.createInventoryItem(product,dateTime);
            inventoryService.createInventory(inventory);
    }


    public void updateProductDates(AggregatedProduct amp, LocalDateTime localDateTime){

        Product product = amp.getProduct();

        if(product.getDateWorkBegan() == null){
            product.setDateWorkBegan(localDateTime);
        }
        product.setLastWorkedOn(localDateTime);


        amp.setProduct(product);
    }

    private boolean checkProductHasNotBeenManufactured(AggregatedProduct product) {
        Long productTransactionId = getProductTransactionId(product.getId());

        if (productTransactionId != null) {
            logger.info("Product has a transaction id attached. It must be manufactured already.");
            return true;
        }

        return false;
    }

    private Long getProductTransactionId(Long manufacturedProductId) {
        return aggregatedProductsRepository.getProductTransactionId(manufacturedProductId);
    }

    private LocalDateTime parseManufactureDate(String dateManufactured) {

        LocalDateTime updateDateManufactured;
        try {
            updateDateManufactured = LocalDateTime.parse(dateManufactured, DateTimeFormatter.ISO_DATE_TIME);
            // Use `dateManufactured` in your service logic
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dateManufactured: " + dateManufactured, e);
        }

        return updateDateManufactured;
    }

    private ProductTransaction createProductTransaction(Long coilId, LocalDateTime dateTime,
                                                        AggregatedProduct product) {

        // Handled the coil subtraction as well.
        ProductTransaction productTransaction = productTransactionsService.createProductTransaction(
                coilId, dateTime, product);

        // update METRES CUT in machine events
        updateTotalMetresCutOnMachineEvent(coilId, product.getLength());
        return productTransactionsService.save(productTransaction);
    }

    public void updateTotalMetresCutOnMachineEvent(Long coilId , BigDecimal totalMetresCut) {
        // cut the metres in machine events
        SteelCoil steelCoil = steelCoilService.getByCoilId(coilId);
        logger.info("Steel coil to be cut --> {}", steelCoil.getId());
        machineEventsService.updateTotalMetersCut(steelCoil.getId(), totalMetresCut);
    }

    @Transactional
    public void deleteAggregatedProduct(Long aggProductID) {
        aggregatedProductsRepository.deleteById(aggProductID);
    }

    public AggregatedProduct saveAggregatedProduct(AggregatedProduct aggregatedProductInStockOnHand) {
        return aggregatedProductsRepository.save(aggregatedProductInStockOnHand);
    }
}
