package com.axe.product.services;


import com.axe.aggregated_products.AggregatedProduct;
import com.axe.aggregated_products.AggregatedProductsService;
import com.axe.clients.Client;
import com.axe.common.utils.InventoryUtils;
import com.axe.price_history.PriceHistoryService;
import com.axe.product.productDTO.*;
import com.axe.product.services.utils.ProductSpecifications;


import com.axe.inventories.Inventory;
import com.axe.inventories.InventoryService;
import com.axe.product.Product;
import com.axe.product.ProductRepository;
import com.axe.projects.Project;
import com.axe.quotes.Quote;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteSummaryItemDTO;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.users.User;
import com.axe.users.services.UsersService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final InventoryService inventoryService;

    private final PriceHistoryService priceHistoryService;

    private final UsersService usersService;



    private final AggregatedProductsService aggregatedProductsService;

    private final SteelCoilService steelCoilService;


    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(ProductRepository productRepository,
                          InventoryService inventoryService,
                          AggregatedProductsService aggregatedProductsService,
                          SteelCoilService steelCoilService,
                          PriceHistoryService priceHistoryService,
                          UsersService usersService

    ) {
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
        this.aggregatedProductsService = aggregatedProductsService;
        this.steelCoilService = steelCoilService;
        this.priceHistoryService = priceHistoryService;
        this.usersService = usersService;

    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public Product createProduct(Product product) {

        logger.info("product: {}", product);
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setPlanName(updatedProduct.getPlanName());
                    product.setStatus(updatedProduct.getStatus());
                    product.setInventory(updatedProduct.getInventory());
                    product.setProductType(updatedProduct.getProductType());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id " + id));
    }


    @Transactional
    public void deleteProduct(final Long productId$) {
        logger.info("In deleteproduct(). productId: {}", productId$);

        Product product = productRepository
                .findById(productId$)
                // check if the cutting list is already completed
                .map(currentProduct -> {
                    if ("completed".equals(currentProduct.getStatus()))
                        throw new RuntimeException(
                                "Product list with ID [%s] is already completed and cannot be deleted"
                                        .formatted(productId$));
                    else
                        return currentProduct;

                })
                // check if the cutting list contains completed items
                .map(currentProduct -> {
                    if (currentProduct.getAggregatedProducts().stream() //
                            .anyMatch( //
                                    productCompleted -> "completed".equals(productCompleted.getStatus())))
                        //
                        throw new RuntimeException(
                                "Product list with ID [%s] is already completed and cannot be deleted"
                                        .formatted(productId$));
                    else
                        return currentProduct;
                })
                // Throw an exception if the cutting list is not found
                .orElseThrow(() ->  new RuntimeException(
                "Product list with ID [%s] is already completed and cannot be deleted"
                        .formatted(productId$)));

        // Filter out only the completed products
        List<AggregatedProduct> completedAggregatedProducts = product.getAggregatedProducts().stream() //
                .filter(aggregatedProduct -> "completed".equals(product.getStatus())) //
                .toList();

        // Clear the list of manufactured products
        product.getAggregatedProducts().clear();

        deleteProductHistory(productId$);

        // Add only the completed products back
        product.getAggregatedProducts().addAll(completedAggregatedProducts);


        productRepository.deleteUnprocessedProduct(productId$);
    }



    private void updateProductDates(Product product, LocalDateTime dateTime) {
        Product p = productRepository.findById(product.getId())
                .orElseThrow(()-> new RuntimeException("Product with id [%s] not found".formatted(product.getId())));
        if(p.getDateWorkBegan() == null){
            p.setDateWorkBegan(dateTime);
        }
        p.setLastWorkedOn(dateTime);
        productRepository.save(p);
    }

    //  fix this not sure if needed
    private void checkIfProductIsCompleted(Product product, LocalDateTime dateTime){
        Product p = productRepository.findById(product.getId())
                .orElseThrow(()-> new RuntimeException("Product with id [%s] not found".formatted(product.getId())));

            if(!p.getStatus().equals("completed")){
                return;
            }

        completeProduct(p, dateTime);
    }

    private void completeProduct(Product product, LocalDateTime dateTime) {
        Product p = productRepository.findById(product.getId())
                .orElseThrow(()-> new RuntimeException("Product with id [%s] not found"
                        .formatted(product.getId())));

        p.setStatus("completed");
        p.setDateWorkCompleted(dateTime);
        productRepository.save(p);
    }

    private LocalDateTime parseProductDate(String dateManufactured) {
        return LocalDateTime.parse(dateManufactured, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


    private void createInventoryItem(Product product, LocalDateTime dateTime) {
        logger.info("creating inventory-=====> id: {}, status: {}", product.getId(), product.getStatus());
        Inventory inventory = InventoryUtils.createInventoryItem(product,dateTime);
        inventoryService.createInventory(inventory);

    }

    public Product productStatusChange(Long id, String status) {
        logger.info("Changing status of manufactured product with id: {} to status: {}", id, status);
        String COMPLETED_STATUS = "completed";
        Product product = productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product with id [%s] not found".formatted(id)));


        product.setStatus(status);

        if (COMPLETED_STATUS.equals(status)) {
            checkCompletedAggregatedProducts(COMPLETED_STATUS, product);
        }

        return productRepository.save(product);
    }


    @Transactional
    public Product productManufactured(Product product, ProductPostDTO productPostDTO, LocalDateTime localDateTime) {
        boolean pCompleted = true;

        for(AggregatedProduct amp : product.getAggregatedProducts()) {

//            productPostDTO.setProductId(amp.getId());

            amp = aggregatedProductsService.manufacturedAggregatedProduct(amp, productPostDTO.getCoilId(), localDateTime);

            if(amp.getStatus().equals("completed")) {
                logger.info("Aggregated Product is completed --> {}", amp);
            }else{
                logger.info("Aggregated Product is NOT completed --> {}", amp);
                pCompleted = false;
            }
        }

        if(pCompleted) {
            logger.info("All aggregated products are completed");
            product.setStatus("completed");
        }

        return productRepository.save(product);
    }


    // To Implement later and check logic
    @Transactional
    public Product productManufactured(ProductPostDTO productPostDTO) {

        logger.info("Product to be manufactured --> {}", productPostDTO);

        Product product = productRepository.findById(productPostDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product with id [%s] not found".formatted(productPostDTO.getProductId())));

//        LocalDateTime localDateTime = parseProductDate(productPostDTO.getDateManufactured());
        LocalDateTime dateManufactured;
        try {
            dateManufactured = LocalDateTime.parse(productPostDTO.getDateManufactured(), DateTimeFormatter.ISO_DATE_TIME);
            // Use `dateManufactured` in your service logic
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format for dateManufactured: " + productPostDTO.getDateManufactured(), e);
        }


        logger.info("LocalDateTime --> {}", dateManufactured);

        product = productManufactured(product, productPostDTO, dateManufactured);
        createInventoryItem(product, dateManufactured);

        updateProductDates(product, dateManufactured);

        checkIfProductIsCompleted(product, dateManufactured);

        return product;
    }

    @Transactional
    public Product updateProductPrice(String userEmail,PriceAndProductTypeDTO priceAndProductTypeDTO) {
        Optional<User> optionalUser = usersService.getUserByEmail(userEmail);
        User user = optionalUser.orElseThrow(() ->
                new RuntimeException("No user found with email: " + userEmail));

        logger.info("In updateCuttingList().priceAndProductTypeDTO: {}", priceAndProductTypeDTO);
        Product product = productRepository.findById(priceAndProductTypeDTO.getCuttingListId())
                .orElseThrow(()->new RuntimeException("Product with id [%s] not found".formatted(priceAndProductTypeDTO.getCuttingListId())));


        if(product.getGauge().getGauge().equals(priceAndProductTypeDTO.getGauge().getGauge())) {
            logger.info("Gauge is the same.");
        } else {
            logger.info("Gauge is different.");
            product.setGauge(priceAndProductTypeDTO.getGauge());
        }

        if (priceAndProductTypeDTO.getPricePerMeter() != null){
            priceHistoryService.recordPriceChange(
                    user.getId(),
                    product.getId(),
                    product.getSellPrice(),
                    priceAndProductTypeDTO.getPricePerMeter()
            );
            logger.info("Price changed from {} to {} for productId={}, recorded by userId={}",
                    product.getSellPrice(), priceAndProductTypeDTO.getPricePerMeter(),
                    product.getId(), user.getId());

            product.setSellPrice(priceAndProductTypeDTO.getPricePerMeter());


        }

        return productRepository.save(product);
    }



    public Product changeProductStatus(Long productId, String status) {

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException(""))
                ;

        BigDecimal width = product.getWidth().getWidth();

        if (status.equals("in-progress")) {
            logger.info("Status is in-progress. Checking if there is a cutting list in progress for the machine.");

            Long inProgressProductId
                    = getProductIdInProgressForMachine(width);

            if (inProgressProductId != null) {
                logger.info("There is a product  in progress for the machine. ID = " + inProgressProductId);

                Product inProgressProduct =productRepository.findById(inProgressProductId)
                        .orElseThrow(()-> new RuntimeException(""))
                        ;
                inProgressProduct.setStatus("scheduled");
                productRepository.save(inProgressProduct);
            } else {
                logger.info("No cutting list in progress for the machine.");
            }
        } else if (status.equals("scheduled")) {
            logger.info("Status is not in-progress.");
        }

        product.setStatus(status);
        return productRepository.save(product);
    }


    private Long getProductIdInProgressForMachine(BigDecimal width) {
        logger.info("In the getProductIdInProgressForMachine method. width: " + width);
        return productRepository.findProductIDInProgressForMachine(width);
    }

    public ProductSummaryDetailsDTO getProductInProgressForMachine(Integer width) {
        return productRepository.findProductInProgressForMachine(width);
    }

    public List<ProductSummaryDetailsDTO> getProductsScheduledForProduct(Integer width) {
        return productRepository.getProductsScheduledForProduct(width);
    }

    public ProductInformationDTO getProductInformation(Long id) {

        Product product = productRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));

        ProductInformationDTO dto = new ProductInformationDTO();
        dto.setProduct(product);

        // Extract related data
        Quote quote = product.getQuote();
        if (quote != null) {
            dto.setQuoteStatus(quote.getStatus().getValue());
            dto.setQuoteAcceptedDate(quote.getDateAccepted());

            Project project = quote.getProject();
            if (project != null) {
                dto.setProjectName(project.getName());

                Client client = project.getClient();
                if (client != null) {
                    dto.setClientName(client.getName());
                }
            }
        }

        return dto;
    }

    public Product changeProductPriority(Long productId, String priority) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + productId));
        product.setPriority(priority);
        return saveProduct(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<QuoteSummaryItemDTO> findSummary(LocalDate startDate, LocalDate endDate) {
        return productRepository.findSummary(startDate, endDate);
    }

    public ProductCompletedResponse getAllCompletedProducts(Pageable pageable,String filters) {

        Specification<Product> spec = ProductSpecifications.withFilters(filters);

        Page<Product> productCompletedResponse =
                productRepository.findAll(spec,pageable);
        return new ProductCompletedResponse(
                productCompletedResponse.getContent(),
                productCompletedResponse.getTotalElements()
        );
    }


    public Product manufacturedProductStatusChange(Long id, String status) {
        logger.info("Changing status of manufactured product with id: {} to status: {}", id, status);
        String COMPLETED_STATUS = "completed";
        AggregatedProduct aggregatedProduct = aggregatedProductsService.getAggregatedProductById(id);
        Product product = productRepository.findById(aggregatedProduct.getProduct().getId())
                .orElseThrow(()-> new RuntimeException(" Product with id [%s] not found".formatted(id) ));


        if (product == null) {
            logger.error("Manufactured product with id: {} not found", id);
            return null;
        }

        if (COMPLETED_STATUS.equals(status)) {
            boolean allDone = checkCompletedAggregatedProducts(COMPLETED_STATUS, product);
            if(allDone)
                product.setStatus(status);

        }

        return productRepository.save(product);
    }

    private boolean checkCompletedAggregatedProducts(String COMPLETED_STATUS, Product product) {
        boolean allDone = product.getAggregatedProducts().stream()
                .allMatch(amp -> COMPLETED_STATUS.equals(amp.getStatus()));

        if (allDone) {
            LocalDateTime localDateTime = product.getAggregatedProducts().stream()
                    .map(amp -> amp.getProductTransaction() != null ? amp.getProductTransaction().getDate() : LocalDateTime.now())
                    .findFirst()
                    .orElse(LocalDateTime.now());

            createInventoryItem(product, localDateTime);
        }
        return allDone;
    }


    // TODO : Refactor This
    @Transactional
    public ManufacturedResponse groupOfSheetsManufactured(List<ProductPostDTO> products) {
        logger.info("In the groupO-------------------------->{}", products.size());
        String status = "completed";
        LocalDateTime localDateTime = parseProductDate(products.get(0).getDateManufactured());

        logger.info("Product to be manufactured :::::::: {}" , products);
        SteelCoil steelCoil = steelCoilService.getByCoilId(products.get(0).getCoilId());

        if (steelCoil == null) {
            return new ManufacturedResponse(false, "Steel coil not found", 0, 0);
        }else {
            logger.info("Steel coil.id: {}", steelCoil.getId());
            logger.info("Length remaining: {}", steelCoil.getEstimatedMetersRemaining());
        }

        long startTime = System.currentTimeMillis();

        Product mp =  processAggregatedProducts(products);

        if(mp == null)
            throw new RuntimeException("manufactured products have no product id");

        updateProductDates(mp, localDateTime);

        boolean allDone = checkCompletedAggregatedProducts(status, mp);
        if(allDone) {
            mp.setStatus(status);
            mp = productRepository.save(mp);
            createInventoryItem(mp, localDateTime);

        }
        else{
            productRepository.save(mp);
        }


        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Processing time for GROUPED MANUFACTURED PRODUCTS: {} milliseconds", duration);

        return new ManufacturedResponse(true, "Successfully processed  products",
                products.size(), endTime - startTime);

    }

    @Transactional
    public Product processAggregatedProducts(List<ProductPostDTO> productPostDTO) {

        AggregatedProduct amp = null;
        for (ProductPostDTO productDTO : productPostDTO) {
            AggregatedProduct aggregatedProduct = aggregatedProductsService.getAggregatedProductById(productDTO.getProductId());


                amp = aggregatedProductsService.manufacturedAggregatedProduct(aggregatedProduct, productDTO.getCoilId(), LocalDateTime.now());

                if (amp.getStatus().equals("completed")) {
                    logger.info("Aggregated Product is completed --> {}", amp);
                } else {
                    logger.info("Aggregated Product is NOT completed --> {}", amp);
                }
            }
        if(amp != null)
            return amp.getProduct();

        return  null;
    }

    public BigDecimal calculateWeightedAvgCostForSteelType(Long colorId, Long gaugeId, Long widthId) {
        logger.debug("Calc weighted average cost for steel type with colorId: {}, gaugeId: {}, widthId: {}", colorId, gaugeId, widthId);
        return steelCoilService.calculateWeightedAvgCostForSteelType(colorId, gaugeId, widthId);
    }

    @Transactional
    public void deleteProductHistory(final Long productId) {
        logger.info("In deleteproduct(). productId: {}", productId);

        priceHistoryService.deleteAllByProductId(productId);

    }

}
