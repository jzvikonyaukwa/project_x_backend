package com.axe.product.services;

import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.price_history.PriceHistoryService;
import com.axe.product.Product;
import com.axe.product.ProductRepository;
import com.axe.product.productDTO.CreateProductDTO;
import com.axe.product_type.ProductType;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SteelCoilService steelCoilService;

    @Mock
    private QuotesService quotesService;

    @InjectMocks
    private UpdateProductService updateProductService;


    @Mock
    private UsersService usersService;

    @Mock
    private PriceHistoryService priceHistoryService;

    private ProductType mockProductType;
    private Profile mockProfile;
    private Color mockColor;
    private Gauge mockGauge;
    private Width mockWidth;
    private Quote mockQuote;
    @BeforeEach
    void setUp() {

        User mockUser = new User();
        mockUser.setId(999L);
        mockUser.setEmail("jotham@opki.io");

        lenient().when(usersService.getUserByEmail("jotham@opki.io"))
                .thenReturn(Optional.of(mockUser));
        lenient().when(priceHistoryService.createPriceHistory(any())).thenAnswer(inv -> inv.getArgument(0));


        // Default behavior for save()
        mockQuote = new Quote();
        mockQuote.setId(1L);
        mockQuote.setDateLastModified(LocalDate.now());

        mockProductType = new ProductType();
        mockProductType.setId(1L);
        mockProductType.setName("Purlins");

        mockProfile = new Profile();
        mockProfile.setProfile("Profile1");

        mockColor = new Color();
        mockColor.setColor("Blue");

        mockGauge = new Gauge();
        mockWidth = new Width();
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Long productId = 1L;
        Long quoteId = 100L;

        // Stub saving here, where it’s actually used
        lenient().when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setId(productId);
        createProductDTO.setProductType(mockProductType);
        createProductDTO.setProfile(mockProfile);
        createProductDTO.setColor(mockColor);
        createProductDTO.setGauge(mockGauge);
        createProductDTO.setWidth(mockWidth);
        createProductDTO.setTotalLength(BigDecimal.valueOf(30));
        createProductDTO.setSellPrice(BigDecimal.valueOf(50));
        createProductDTO.setCostPrice(BigDecimal.valueOf(40));
        createProductDTO.setTargetDate(LocalDate.now());

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        Quote quote = new Quote();
        quote.setId(quoteId);

        // Now we also need to ensure the product is found, or else
        // we'll get "Product list with id [1] not found".
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(existingProduct));

        when(quotesService.getQuote(quoteId)).thenReturn(quote);

        // Act
        Product updatedProduct = updateProductService.updateProduct("jotham@opki.io",createProductDTO, quoteId);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals("scheduled", updatedProduct.getStatus());
        assertEquals("normal", updatedProduct.getPriority());
        assertEquals("Purlins", updatedProduct.getPlanName());
        assertEquals("Profile1", updatedProduct.getProfile().getProfile());
        assertEquals("Blue", updatedProduct.getColor().getColor());
        assertEquals(BigDecimal.valueOf(30), updatedProduct.getTotalLength());
        assertEquals(BigDecimal.valueOf(50), updatedProduct.getSellPrice());
        assertEquals(BigDecimal.valueOf(40), updatedProduct.getCostPrice());
        assertEquals(quote, updatedProduct.getQuote());

        verify(productRepository).save(updatedProduct);
    }


    @Test
    void testUpdateProductProductNotFound() {
        // Arrange
        Long productId = 1L;


        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setId(productId);
        createProductDTO.setProductType(mockProductType);
        createProductDTO.setProfile(mockProfile);
        createProductDTO.setColor(mockColor);
        createProductDTO.setGauge(mockGauge);
        createProductDTO.setWidth(mockWidth);
        createProductDTO.setTotalLength(BigDecimal.valueOf(30));
        createProductDTO.setSellPrice(BigDecimal.valueOf(50));
        createProductDTO.setCostPrice(BigDecimal.valueOf(40));
        createProductDTO.setTargetDate(LocalDate.now());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateProductService.updateProduct("jotham@opki.io",createProductDTO, mockQuote.getId()));
        assertEquals("Product list with id [1] not found", exception.getMessage());
    }

    @Test
    void testUpdateProductQuoteNotFound() {
        // Arrange
        Long productId = 1L;

        // Build CreateProductDTO so it has the correct ID, product type, etc.
        CreateProductDTO createProductDTO = new CreateProductDTO();
        createProductDTO.setId(productId);
        createProductDTO.setProductType(mockProductType);
        createProductDTO.setProfile(mockProfile);
        createProductDTO.setColor(mockColor);
        createProductDTO.setGauge(mockGauge);
        createProductDTO.setWidth(mockWidth);
        createProductDTO.setTotalLength(BigDecimal.valueOf(30));
        createProductDTO.setSellPrice(BigDecimal.valueOf(50));
        createProductDTO.setCostPrice(BigDecimal.valueOf(40));
        createProductDTO.setTargetDate(LocalDate.now());

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        // Make sure the product is found
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Make quote service return null, so we expect "Quote with id [100] not found"
        when(quotesService.getQuote(1L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> updateProductService.updateProduct("jotham@opki.io",createProductDTO, mockQuote.getId()));

        assertEquals("Quote with id [1] not found", exception.getMessage());
    }
}
