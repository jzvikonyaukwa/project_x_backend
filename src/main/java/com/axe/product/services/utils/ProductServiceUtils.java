package com.axe.product.services.utils;

import com.axe.aggregated_products.AggregatedProduct;
import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.product.productDTO.CreateProductDTO;
import com.axe.product.Product;
import com.axe.product.productDTO.ProductDTO;
import com.axe.steelCoils.SteelCoilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductServiceUtils {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceUtils.class);
    private static final BigDecimal SEGMENT_LENGTH = BigDecimal.valueOf(6);
    private static final String STATUS_SCHEDULED = "scheduled";


    public static BigDecimal adjustLengthForProductType(BigDecimal totalLength) {
        BigDecimal cutsNeeded = totalLength.divide(SEGMENT_LENGTH, 0, RoundingMode.CEILING);
        return cutsNeeded.multiply(SEGMENT_LENGTH);
    }

    public static boolean checkStockAvailability(SteelCoilService steelCoilService, Color color, Gauge gauge, BigDecimal lengthNeeded, BigDecimal width) {
        BigDecimal totalLength = steelCoilService.getTotalMtrsForAcceptedCuttingLists(
                gauge.getGauge(),
                color.getColor(),
                width
        );

        if (totalLength == null) totalLength = BigDecimal.ZERO;
        BigDecimal lengthRequired = totalLength.add(lengthNeeded);

        return steelCoilService.isThereEnoughStock(
                1L,
                color.getColor(),
                gauge.getGauge(),
                lengthRequired,
                width
        );
    }

    public static List<AggregatedProduct> createAggregatedProducts(CreateProductDTO createProductDTO, Product product, String productTypeName) {
        return switch (productTypeName) {
            case "Purlins", "Battens" -> createSegmentedProducts(createProductDTO, product);
            case "Roof Sheet" -> createRoofSheetProducts(createProductDTO, product);
            default -> createGenericProducts(createProductDTO, product);
        };
    }

    public static List<AggregatedProduct> createAggregatedProducts(ProductDTO productDTO, Product product, String productTypeName) {
        return switch (productTypeName) {
            case "Purlins", "Battens" -> createSegmentedProducts(productDTO, product);
            default -> createGenericProducts(productDTO, product);
        };
    }

    private static List<AggregatedProduct> createSegmentedProducts(CreateProductDTO createProductDTO, Product product) {
        List<AggregatedProduct> aggregatedProducts = new ArrayList<>();
        BigDecimal totalLength = createProductDTO.getTotalLength();
        BigDecimal numberOfSegments = totalLength.divide(SEGMENT_LENGTH, 0, RoundingMode.CEILING);

        for (int i = 0; i < numberOfSegments.intValue(); i++) {
            aggregatedProducts.add(AggregatedProduct.builder()
                    .stick(null)
                    .stickType(null)
                    .code(createProductDTO.getCode())
                    .length(SEGMENT_LENGTH)
                    .status(STATUS_SCHEDULED)
                    .product(product)
                    .build());
        }

        product.setTotalLength(SEGMENT_LENGTH.multiply(numberOfSegments));
        return aggregatedProducts;
    }

    private static List<AggregatedProduct> createSegmentedProducts(ProductDTO productDTO, Product product) {
        List<AggregatedProduct> aggregatedProducts = new ArrayList<>();
        BigDecimal totalLength = productDTO.getTotalLength();
        BigDecimal numberOfSegments = totalLength.divide(SEGMENT_LENGTH, 0, RoundingMode.CEILING);

        for (int i = 0; i < numberOfSegments.intValue(); i++) {
            aggregatedProducts.add(AggregatedProduct.builder()
                    .stick(null)
                    .stickType(null)
                    .code(null)
                    .length(SEGMENT_LENGTH)
                    .status(STATUS_SCHEDULED)
                    .product(product)
                    .build());
        }

        product.setTotalLength(SEGMENT_LENGTH.multiply(numberOfSegments));
        return aggregatedProducts;
    }

    private static List<AggregatedProduct> createRoofSheetProducts(CreateProductDTO createProductDTO, Product product) {
        return createProductDTO.getAggregatedProducts().stream()
                .flatMap(lengthQuantity -> IntStream.range(0, Math.toIntExact(lengthQuantity.getQuantity()))
                        .mapToObj(i -> AggregatedProduct.builder()
                                .stick("SHEETS")
                                .stickType("SHEETS")
                                .code(createProductDTO.getCode())
                                .length(lengthQuantity.getLength())
                                .status(STATUS_SCHEDULED)
                                .product(product)
                                .build())
                )
                .collect(Collectors.toList());
    }

    private static List<AggregatedProduct> createGenericProducts(CreateProductDTO createProductDTO, Product product) {
        return createProductDTO.getAggregatedProducts().stream()
                .map(productLengthQuantityDTO -> AggregatedProduct.builder()
                        .stick(null)
                        .stickType(null)
                        .code(createProductDTO.getCode())
                        .length(productLengthQuantityDTO.getLength())
                        .status(STATUS_SCHEDULED)
                        .product(product)
                        .build())
                .collect(Collectors.toList());
    }

    private static List<AggregatedProduct> createGenericProducts(ProductDTO productDTO, Product product) {
        return productDTO.getAggregatedProducts().stream()
                .map(aggregatedProduct -> AggregatedProduct.builder()
                        .stick(aggregatedProduct.getStick())
                        .stickType(aggregatedProduct.getStickType())
                        .code(aggregatedProduct.getCode())
                        .length(aggregatedProduct.getLength())
                        .status(STATUS_SCHEDULED)
                        .product(product)
                        .build())
                .collect(Collectors.toList());
    }
}
