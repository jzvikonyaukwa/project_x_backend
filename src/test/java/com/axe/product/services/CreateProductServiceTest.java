package com.axe.product.services;

import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.price_history.PriceHistoryService;
import com.axe.product.Product;
import com.axe.product.ProductRepository;
import com.axe.product.productDTO.*;
import com.axe.product_type.ProductType;
import com.axe.product_type.ProductTypeService;
import com.axe.profile.Profile;
import com.axe.quotes.Quote;
import com.axe.quotes.services.QuotesService;
import com.axe.steelCoils.SteelCoilService;
import com.axe.users.User;
import com.axe.users.services.UsersService;
import com.axe.width.Width;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SteelCoilService steelCoilService;

    @Mock
    private QuotesService quotesService;

    @Mock
    private ProductTypeService productTypeService;

    @InjectMocks
    private CreateProductService createProductService;


    @Mock
    private UsersService usersService;

    @Mock
    private PriceHistoryService priceHistoryService;

    private Quote mockQuote;
    private ProductType mockProductType;
    private Profile mockProfile;
    private Color mockColor;
    private Gauge mockGauge;
    private Width mockWidth;

    @BeforeEach
    void setUp() {


        User mockUser = new User();
        mockUser.setId(999L);
        mockUser.setEmail("jotham@opki.io");

        when(usersService.getUserByEmail("jotham@opki.io"))
                .thenReturn(Optional.of(mockUser));
        lenient().when(priceHistoryService.createPriceHistory(any())).thenAnswer(inv -> inv.getArgument(0));


        mockQuote = new Quote();
        mockQuote.setId(1L);
        mockQuote.setDateLastModified(LocalDate.now());

        mockProductType = new ProductType();
        mockProductType.setId(1L);
        mockProductType.setName("Purlins");

        mockProfile = new Profile();
        mockColor = new Color();
        mockGauge = new Gauge();
        mockWidth = new Width();

        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    void createProduct_WithCreateProductDTO_Success() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO();
        dto.setProductType(mockProductType);
        dto.setProfile(mockProfile);
        dto.setColor(mockColor);
        dto.setGauge(mockGauge);
        dto.setWidth(mockWidth);
        dto.setTargetDate(LocalDate.now());
        dto.setFrameType("TestFrame");
        dto.setFrameName("TestName");
        dto.setTotalLength(BigDecimal.valueOf(12));
        dto.setTotalQuantity(2);
        dto.setCostPrice(BigDecimal.valueOf(100));
        dto.setSellPrice(BigDecimal.valueOf(150));
        dto.setAggregatedProducts(new ArrayList<>());

        when(quotesService.getQuote(anyLong())).thenReturn(mockQuote);

        // Act
        Product result = createProductService.createProduct("jotham@opki.io",dto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("scheduled", result.getStatus());
        assertEquals("normal", result.getPriority());
        assertEquals(mockProductType.getName(), result.getPlanName());
        assertEquals(dto.getTotalLength(), result.getTotalLength());
        assertEquals(dto.getTotalQuantity(), result.getTotalQuantity());
        verify(productRepository).save(any(Product.class));
    }




    @Test
    void createProduct_WithPastedRequestDTO_Success() {
        // Arrange
        PastedAggregatedRequestDTO aggregatedDTO = new PastedAggregatedRequestDTO();
        aggregatedDTO.setPlanName("TestPlan");
        aggregatedDTO.setFrameName("TestFrame");
        aggregatedDTO.setFrameType("TestFrameType");
        aggregatedDTO.setLength(BigDecimal.valueOf(6));
        aggregatedDTO.setCode("TEST123");
        aggregatedDTO.setStick("TestStick");
        aggregatedDTO.setStickType("TestStickType");

        PastedRequestDTO pastedRequestDTO = new PastedRequestDTO();
        pastedRequestDTO.setProfile(mockProfile);
        pastedRequestDTO.setColor(mockColor);
        pastedRequestDTO.setGauge(mockGauge);
        pastedRequestDTO.setWidth(mockWidth);
        pastedRequestDTO.setTargetDate(LocalDate.now());
        pastedRequestDTO.setPriority("normal");
        pastedRequestDTO.setCanInvoice(true);
        pastedRequestDTO.setAggregatedProducts(List.of(aggregatedDTO));

        when(quotesService.getQuote(anyLong())).thenReturn(mockQuote);
        when(productTypeService.getProductTypeByName(anyString())).thenReturn(mockProductType);
        lenient().when(steelCoilService.calculateWeightedAvgCostForSteelType(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(100));

        // Act
        List<Product> results = createProductService.createProduct("jotham@opki.io",pastedRequestDTO, 1L);

        // Assert
        assertFalse(results.isEmpty());
        Product result = results.get(0);
        assertEquals("scheduled", result.getStatus());
        assertEquals("normal", result.getPriority());
        assertTrue(result.getCanInvoice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_WithPurlins_AdjustsLength() {
        // Arrange
        ProductType purlinType = new ProductType();
        purlinType.setId(1L);
        purlinType.setName("Purlins");

        CreateProductDTO dto = new CreateProductDTO();
        dto.setProductType(purlinType);
        dto.setProfile(mockProfile);
        dto.setColor(mockColor);
        dto.setGauge(mockGauge);
        dto.setWidth(mockWidth);
        dto.setTargetDate(LocalDate.now());
        dto.setFrameType("TestFrame");
        dto.setFrameName("TestName");
        dto.setTotalLength(BigDecimal.valueOf(7)); // Not divisible by 6
        dto.setTotalQuantity(1);
        dto.setCostPrice(BigDecimal.valueOf(100));
        dto.setSellPrice(BigDecimal.valueOf(150));

        dto.setAggregatedProducts(new ArrayList<>());

        lenient().when(steelCoilService.calculateWeightedAvgCostForSteelType(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(100)); // Lenient stubbing

        when(quotesService.getQuote(anyLong())).thenReturn(mockQuote);

        // Act
        Product result = createProductService.createProduct("jotham@opki.io",dto, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(12), result.getTotalLength()); // Rounded to the nearest multiple of 6
        verify(productRepository).save(any(Product.class));
    }


    @Test
    void createProduct_UpdatesQuoteModificationDate() {
        // Arrange
        CreateProductDTO dto = new CreateProductDTO();
        dto.setProductType(mockProductType);
        dto.setProfile(mockProfile);
        dto.setColor(mockColor);
        dto.setGauge(mockGauge);
        dto.setWidth(mockWidth);
        dto.setTargetDate(LocalDate.now());
        dto.setTotalLength(BigDecimal.valueOf(12));
        dto.setTotalQuantity(1);

        dto.setAggregatedProducts(new ArrayList<>());

        when(quotesService.getQuote(anyLong())).thenReturn(mockQuote);
        lenient().when(steelCoilService.calculateWeightedAvgCostForSteelType(any(), any(), any()))
                .thenReturn(BigDecimal.valueOf(100));

        // Act
        createProductService.createProduct("jotham@opki.io",dto, 1L);

        // Assert
        assertEquals(LocalDate.now(), mockQuote.getDateLastModified());
        verify(quotesService).getQuote(1L);
    }
}
