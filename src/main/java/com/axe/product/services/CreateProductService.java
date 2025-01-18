package com.axe.product.services;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.price_history.PriceHistory;
import com.axe.price_history.PriceHistoryService;
import com.axe.product.productDTO.CreateProductDTO;
import com.axe.product.productDTO.PastedAggregatedRequestDTO;
import com.axe.product.productDTO.PastedRequestDTO;
import com.axe.product.productDTO.ProductDTO;
import com.axe.product.Product;
import com.axe.product.ProductRepository;
import com.axe.product_type.ProductType;
import com.axe.product_type.ProductTypeService;
import com.axe.profile.Profile;
import com.axe.quotes.Quote;
import com.axe.quotes.services.QuotesService;
import com.axe.steelCoils.SteelCoilService;
import com.axe.users.User;
import com.axe.users.services.UsersService;
import com.axe.width.Width;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.axe.product.services.utils.ProductServiceUtils.*;


@Service
public class CreateProductService {
    private final Logger logger = LoggerFactory.getLogger(CreateProductService.class);
    private final PriceHistoryService priceHistoryService;
    private final UsersService usersService;

    private static final BigDecimal SEGMENT_LENGTH = BigDecimal.valueOf(6);
    private static final String SCHEDULED = "scheduled";
    private static final String NORMAL_PRIORITY = "normal";
    private final ProductRepository productRepository;

    private final SteelCoilService steelCoilService;
    private final QuotesService quotesService;

    private final ProductTypeService productTypeService;



    public CreateProductService(ProductRepository productRepository,
                                SteelCoilService steelCoilService,
                                QuotesService quotesService,
                                ProductTypeService productTypeService,
                                PriceHistoryService priceHistoryService,
                                UsersService usersService
                                ){
        this.steelCoilService =steelCoilService;
        this.productRepository = productRepository;
        this.quotesService = quotesService;
        this.productTypeService = productTypeService;
        this.priceHistoryService = priceHistoryService;
        this.usersService = usersService;

    }

    public Product createProduct(String userEmail,CreateProductDTO createProductDTO, Long quoteId) {
        Optional<User> optionalUser = usersService.getUserByEmail(userEmail);
        User user = optionalUser.orElseThrow(() ->
                new RuntimeException("No user found with email: " + userEmail));

        Quote quote = updateQuoteModificationDate(quoteId);
        ProductType productType = createProductDTO.getProductType();

            if (productType.getName().equals("Purlins")) {
                adjustPurlinLength(createProductDTO);
            }

            Product product = createProductInternal(createProductDTO, quote, productType);

            recordInitialPriceHistory(product, user);


        return product;
    }

    private void recordInitialPriceHistory(Product product, User user) {
        if (product.getSellPrice() != null) {
            PriceHistory history = new PriceHistory();
            history.setUserId(user != null ? user.getId() : null);
            history.setProductId(product.getId());
            history.setChangedAt(LocalDateTime.now());
            history.setOldPrice(BigDecimal.ZERO);
            history.setNewPrice(product.getSellPrice());

            priceHistoryService.createPriceHistory(history);
            logger.info("PriceHistory recorded for productId={}, newPrice={}",
                    product.getId(), product.getSellPrice());
        }
    }

    // todo:  refactor create product with pasted
    public List<Product> createProduct(String userEmail,PastedRequestDTO pastedRequestDTOS, Long quoteId) {
        Optional<User> optionalUser = usersService.getUserByEmail(userEmail);
        User user = optionalUser.orElseThrow(() ->
                new RuntimeException("No user found with email: " + userEmail));

        Quote quote = updateQuoteModificationDate(quoteId);

        Map<String, List<PastedAggregatedRequestDTO>> groupedByPlanAndFrameName = pastedRequestDTOS.getAggregatedProducts().stream()
                .collect(Collectors.groupingBy(dto -> dto.getPlanName() + "|" + dto.getFrameName()));


        List<Product> createdProducts = new ArrayList<>();



        for (Map.Entry<String, List<PastedAggregatedRequestDTO>> entry : groupedByPlanAndFrameName.entrySet()) {
            String planAndFrameKey = entry.getKey();
            String planName = planAndFrameKey.split("\\|")[0];

            List<PastedAggregatedRequestDTO> group = entry.getValue();

            ProductType productType = productTypeService.getProductTypeByName(planName);

            ProductDTO productDTO = createProductDTOFromGroup(group,pastedRequestDTOS,productType);


            Product product = createProductInternal(productDTO, quote, productType);

            createdProducts.add(product);

        }

        if(createdProducts.get(0) !=null) {
            recordInitialPriceHistory(createdProducts.get(0), user);
        }

        return createdProducts;


    }

    private ProductDTO createProductDTOFromGroup(List<PastedAggregatedRequestDTO> group,PastedRequestDTO pastedRequestDTOS,ProductType productType) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setProfile(pastedRequestDTOS.getProfile());
        productDTO.setColor(pastedRequestDTOS.getColor());
        productDTO.setGauge(pastedRequestDTOS.getGauge());
        productDTO.setWidth(pastedRequestDTOS.getWidth());
        productDTO.setTargetDate(pastedRequestDTOS.getTargetDate());
        productDTO.setFrameType(group.get(0).getFrameType());
        productDTO.setFrameName(group.get(0).getFrameName());

        BigDecimal totalLength = group.stream()
                .map(PastedAggregatedRequestDTO::getLength)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        productDTO.setTotalLength(totalLength);

        int totalQuantity = group.size();

        productDTO.setTotalQuantity(totalQuantity);
        productDTO.setPlanName(group.get(0).getPlanName());
        productDTO.setPriority(pastedRequestDTOS.getPriority());
        productDTO.setCanInvoice(pastedRequestDTOS.getCanInvoice());
        productDTO.setProductType(productType);
        productDTO.setAggregatedProducts(createAggregatedProductsPasted(group));
        productDTO.setStatus("scheduled");

        return productDTO;
    }

    private List<AggregatedProduct> createAggregatedProductsPasted(List<PastedAggregatedRequestDTO> pastedAggregatedRequestDTOS ){

        return pastedAggregatedRequestDTOS.stream().map(pastedAggregatedRequestDTO ->
                        AggregatedProduct
                                .builder()
                                .code(pastedAggregatedRequestDTO.getCode())
                                .status("scheduled")
                                .length(pastedAggregatedRequestDTO.getLength())
                                .stickType(pastedAggregatedRequestDTO.getStickType())
                                .stick(pastedAggregatedRequestDTO.getStick())
                                .build())
                .toList();
    }

    private Quote updateQuoteModificationDate(Long quoteId) {
        Quote quote = quotesService.getQuote(quoteId);
        quote.setDateLastModified(LocalDateTime.now().toLocalDate());
        return quote;
    }
    private void adjustPurlinLength(CreateProductDTO createProductDTO){
        if (createProductDTO.getTotalLength().remainder(SEGMENT_LENGTH).compareTo(BigDecimal.ZERO) != 0) {
            createProductDTO.getTotalLength().add(SEGMENT_LENGTH);
        }
    }

    private Product createProductInternal(Object productDetailsDTO, Quote quote, ProductType productType) {
        Product product;

        if (productDetailsDTO instanceof CreateProductDTO createProductDTO) {
            product = buildProductFromDTO(createProductDTO, quote, productType);
            product.setAggregatedProducts(createAggregatedProducts(createProductDTO, product, productType.getName()));
        } else if (productDetailsDTO instanceof ProductDTO productDTO) {
            product = buildProductFromDTO(productDTO, quote, productType);
            product.setAggregatedProducts(createAggregatedProducts(productDTO, product, productType.getName()));
        } else {
            throw new IllegalArgumentException("Unsupported DTO type: " + productDetailsDTO.getClass().getSimpleName());
        }

        BigDecimal totalLength = calculateTotalLength(product);
        int totalQuantity = (int)product.getAggregatedProducts().stream().count();

        totalLength = adjustLengthForProductType( totalLength, productType);


        product.setTotalLength(totalLength);
        product.setProductType(productType);
        product.setTotalQuantity(totalQuantity);
        return productRepository.save(product);
    }


    private Product buildProductFromDTO(Object productDetailsDTO, Quote quote, ProductType productType) {
        Profile profile;
        Color color;
        Gauge gauge;
        Width width;
        LocalDate targetDate;
        String frameType;
        String frameName;
        BigDecimal totalLength;
        Integer totalQuantity;
        BigDecimal costPrice;
        BigDecimal sellPrice;

        if (productDetailsDTO instanceof CreateProductDTO createProductDTO) {
            profile = createProductDTO.getProfile();
            color = createProductDTO.getColor();
            gauge = createProductDTO.getGauge();
            width = createProductDTO.getWidth();
            targetDate = createProductDTO.getTargetDate();
            frameType = createProductDTO.getFrameType();
            frameName = createProductDTO.getFrameName();
            totalLength = createProductDTO.getTotalLength();
            totalQuantity = createProductDTO.getTotalQuantity();
            costPrice = createProductDTO.getCostPrice();
            sellPrice = createProductDTO.getSellPrice();
        } else if (productDetailsDTO instanceof ProductDTO productDTO) {
            profile = productDTO.getProfile();
            color = productDTO.getColor();
            gauge = productDTO.getGauge();
            width = productDTO.getWidth();
            targetDate = productDTO.getTargetDate();
            frameType = productDTO.getFrameType();
            frameName = productDTO.getFrameName();
            totalLength = productDTO.getTotalLength();
            totalQuantity = productDTO.getTotalQuantity();
            costPrice = steelCoilService.calculateWeightedAvgCostForSteelType(
                    productDTO.getColor().getId(),
                    productDTO.getGauge().getId(),
                    productDTO.getWidth().getId()
            ).setScale(4, RoundingMode.CEILING);
            sellPrice = steelCoilService.calculateWeightedAvgCostForSteelType(
                    productDTO.getColor().getId(),
                    productDTO.getGauge().getId(),
                    productDTO.getWidth().getId()
            ).setScale(4, RoundingMode.CEILING);



        } else {
            throw new IllegalArgumentException("Unsupported DTO type: " + productDetailsDTO.getClass().getSimpleName());
        }


        return Product.builder()
                .quote(quote)
                .status(SCHEDULED)
                .priority(NORMAL_PRIORITY)
                .planName(productType.getName())
                .profile(profile)
                .color(color)
                .canInvoice(true)
                .gauge(gauge)
                .width(width)
                .targetDate(targetDate)
                .frameType(frameType)
                .frameName(frameName)
                .totalLength(totalLength)
                .totalQuantity(totalQuantity)
                .costPrice(costPrice)
                .sellPrice(sellPrice)
                .build();
    }



    private BigDecimal calculateTotalLength(Product product) {
        return product.getAggregatedProducts().stream()
                .map(AggregatedProduct::getLength)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal adjustLengthForProductType(BigDecimal totalLength, ProductType productType) {
        if (productType.getName().equals("Battens") || productType.getName().equals("Purlins")) {
            BigDecimal cutsNeeded = totalLength.divide(SEGMENT_LENGTH, 0, RoundingMode.CEILING);
            return cutsNeeded.multiply(SEGMENT_LENGTH);
        }
        return totalLength;
    }

}
