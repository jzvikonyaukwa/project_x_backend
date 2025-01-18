package com.axe.grvs.services;

import com.axe.consumables.CalculateWeightedAvgPriceService;
import com.axe.consumables.Consumable;
import com.axe.consumables.ConsumablesService;
import com.axe.consumables.DTOs.ConsumablePostDTO;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseRepository;
import com.axe.consumablesInWarehouse.ConsumableInWarehouseService;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesInWarehouse.exceptions.ConsumableInWarehouseNotFoundException;
import com.axe.consumablesOnGrv.ConsumablesOnGrvService;
import com.axe.gauges.Gauge;
import com.axe.grvs.GRV;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.exceptions.NoProductsFoundException;
import com.axe.grvs.grvsDTO.GRVDetailsDTO;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.purchaseOrders.services.PurchaseOrderService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.suppliers.Supplier;
import com.axe.suppliers.SuppliersService;
import com.axe.utilities.RateNotSavedException;
import com.axe.utilities.SteelCoilNumberExists;
import com.axe.warehouse.Warehouse;
import com.axe.weightConversionsServices.GaugeRateMapper;
import com.axe.weightConversionsServices.WeightConversionService;
import com.axe.width.Width;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GRVAddServiceTest {
    private MockedStatic<LoggerFactory> mockedLoggerFactory;
    private Logger logger;


    @InjectMocks
    private SuppliersService suppliersService;
    @InjectMocks
    private SteelSpecificationService steelSpecificationService;
    @InjectMocks
    private WeightConversionService weightConversionService;
    @InjectMocks
    private SteelCoilService steelCoilService;
    @InjectMocks
    private ConsumablesService consumablesService;
    @InjectMocks
    private ConsumableInWarehouseService consumableInWarehouseService;

    @InjectMocks
    private GRVsRepository grvsRepository;
    @InjectMocks
    private PurchaseOrderService purchaseOrderService;
    @InjectMocks
    private CalculateWeightedAvgPriceService calculateWeightedAvgPriceService;
    @InjectMocks
    private ConsumableInWarehouseRepository consumableInWarehouseRepository;
    @InjectMocks
    ConsumablesOnGrvService consumablesOnGrvService;
    @Mock
    GaugeRateMapper gaugeRateMapper;
    @Mock
    GRVDetailsDTO postGRV;

    @InjectMocks
    private GRVAddService grvAddService;

//    @BeforeEach
//    void setUp() {
//        // Mock LoggerFactory
//        logger = Mockito.mock(Logger.class);
//        mockedLoggerFactory = Mockito.mockStatic(LoggerFactory.class);
//        mockedLoggerFactory.when(() -> LoggerFactory.getLogger(GRVAddService.class))
//                .thenReturn(logger);
//        // Initialize mocks created above
//        MockitoAnnotations.openMocks(this);
//    }
//    @AfterEach
//    void tearDown() {
//        mockedLoggerFactory.close();  // Ensure this is always called for static Logger
//    }
//
//
//
//
//    @Test
//    void testHandlePurchaseOrderWithInvalidId() {
//        //given
//        GRVDetailsDTO postGRV = new GRVDetailsDTO();
//        postGRV.setPurchaseOrderId(999L);
//        GRV newGRV = new GRV();
//
//        //when
//        when(purchaseOrderService.getPurchaseOrder(anyLong())).thenReturn(null);
//
//        //then
//        assertThrows(RuntimeException.class, () ->
//            grvAddService.handlePurchaseOrder(postGRV, newGRV));
//
//        verify(purchaseOrderService).getPurchaseOrder(999L);
//    }
//    @Test
//    void testHandlePurchaseOrderWithNonExistentId() {
//        // given
//        Long nonExistentId = 999L;
//        //ç
//        when(purchaseOrderService.getPurchaseOrder(nonExistentId)).thenReturn(null);
//
//        // then
//        assertThrows(RuntimeException.class, () -> grvAddService.handlePurchaseOrder(new GRVDetailsDTO(), new GRV()),
//                "Expected to throw for non-existent ID");
//    }
//
//    @Test
//    void testHandlePurchaseOrderWithValidId() {
//        // given
//        GRVDetailsDTO postGRV = new GRVDetailsDTO();
//        postGRV.setPurchaseOrderId(999L);
//        GRV newGRV = new GRV();
//
//        // when
//        when(purchaseOrderService.getPurchaseOrder(postGRV.getPurchaseOrderId())).thenReturn(new PurchaseOrder());
//
//        // then
//        grvAddService.handlePurchaseOrder(postGRV, newGRV);
//
//        // Verify
//        assertNotNull(newGRV.getPurchaseOrder());
//        verify(purchaseOrderService).getPurchaseOrder(999L);
//    }
//
//
//    @Test
//    void testCoilNumberExistsInWarehouse() {
//
//        //given
//        Warehouse warehouse = new Warehouse();
//        warehouse.setId(1L);
//        String coilNumber = "12345";
//
//        //when
//        when(steelCoilService.getSteelCoilByCoilNumberInWarehouse(1L, coilNumber)).thenReturn(Optional.of(new SteelCoil()));
//
//        //then
//        assertTrue(grvAddService.coilNumberExistsInWarehouse(warehouse, coilNumber));
//
//        // Verify
//        verify(steelCoilService).getSteelCoilByCoilNumberInWarehouse(1L, coilNumber);
//    }
//
//    @Test
//    void testHandleGRVsConsumablesNotFound() {
//        //given
//        Consumable consumable = new Consumable();
//        consumable.setId(123L);
//        consumable.setName("Test Consumable");
//
//        ConsumablePostDTO consumablePostDTO = new ConsumablePostDTO();
//        consumablePostDTO.setConsumable(consumable);
//        consumablePostDTO.setQtyOrdered(10);
//        consumablePostDTO.setLandedPrice(new BigDecimal("100.00"));
//        List<ConsumablePostDTO> consumables = List.of(consumablePostDTO);
//
//        Warehouse warehouse = new Warehouse();
//        warehouse.setId(1L);
//
//        GRV newGRV = new GRV();
//
//        // when
//        when(consumablesService.getConsumableById(consumable.getId())).thenReturn(consumable);
//        when(consumableInWarehouseRepository.findByConsumableIdAndWarehouseId(consumable.getId(), warehouse.getId()))
//                .thenReturn(Optional.empty());
//
//        // then
//        assertThrows(ConsumableInWarehouseNotFoundException.class, () ->
//                grvAddService.handleGRVsConsumables(consumables, warehouse, newGRV));
//
//        // Verify
//        verify(consumablesService).getConsumableById(consumable.getId());
//        verify(consumableInWarehouseRepository).findByConsumableIdAndWarehouseId(consumable.getId(), warehouse.getId());
//    }
//
//
//    @Test
//    void testHandleGRVsConsumablesFound() {
//        // given
//        Consumable consumable = new Consumable();
//        consumable.setId(123L);
//
//        ConsumablePostDTO consumablePostDTO = new ConsumablePostDTO();
//        consumablePostDTO.setConsumable(consumable);
//        consumablePostDTO.setQtyOrdered(10);
//        consumablePostDTO.setLandedPrice(new BigDecimal("15.00"));
//
//        List<ConsumablePostDTO> consumables = List.of(consumablePostDTO);
//
//        Warehouse warehouse = new Warehouse();
//        warehouse.setId(1L);
//
//        GRV newGRV = new GRV();
//
//        ConsumablesInWarehouse existingEntry = new ConsumablesInWarehouse();
//        existingEntry.setConsumable(consumable);
//        existingEntry.setWarehouse(warehouse);
//        existingEntry.setQty(5);
//        existingEntry.setAvgLandedPrice(new BigDecimal("12.00"));
//
//        // when
//        when(consumableInWarehouseRepository.findByConsumableIdAndWarehouseId(consumable.getId(), warehouse.getId()))
//                .thenReturn(Optional.of(existingEntry));
//
//        when(consumablesService.getConsumableById(consumable.getId())).thenReturn(consumable);
//
//        when(calculateWeightedAvgPriceService.calculateNewWeightedAverage(
//                any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
//                .thenReturn(new BigDecimal("13.50"));
//
//        // then
//        grvAddService.handleGRVsConsumables(consumables, warehouse, newGRV);
//
//        // Verify
//        assertEquals(15, existingEntry.getNumberOfConsumables());
//        assertEquals(0, existingEntry.getAvgLandedPrice().compareTo(new BigDecimal("13.50")));
//        verify(consumableInWarehouseService).saveConsumableInWarehouse(existingEntry);
//        verify(logger).info("ConsumablesInWarehouse updated successfully.");
//    }
//
//
//
//
//
//
//    @Test
//    void testCreateGRVWithNoProducts()  {
//        // given
//        GRVDetailsDTO postGRV = new GRVDetailsDTO();
//        postGRV.setSteelCoils(Collections.emptyList());
//        postGRV.setConsumablesOnGrv(Collections.emptyList());
//        postGRV.setPurchaseOrderId(1L); // Assuming 1L is a dummy ID for testing
//
//        // when
//        when(purchaseOrderService.getPurchaseOrder(1L)).thenReturn(new PurchaseOrder());
//
//
//
//        // then
//      assertThrows(NoProductsFoundException.class, () -> {
//          if(postGRV.getSteelCoils().isEmpty() && postGRV.getConsumablesOnGrv().isEmpty()){
//              logger.info("No products to add. Returning...");
//              throw new NoProductsFoundException(409,"No products to add.");
//          }
//        verifyNoMoreInteractions(grvsRepository);
//    });
//
//            // verify
//            verify(logger).info("No products to add. Returning...");
//    }
//
//    @Test
//    void testCreateGRVWithSteelCoilsAndConsumables()  {
//        // given
//        Warehouse mockWarehouse = new Warehouse();
//        mockWarehouse.setId(1L);
//        mockWarehouse.setName("Test Warehouse");
//
//        Width width = new Width();
//        width.setWidth(new BigDecimal("925"));
//
//        SteelSpecification steelSpecification = new SteelSpecification();
//        steelSpecification.setWidth(width);
//
//        GRVDetailsDTO postGRV = getGrvDetailsDTO(mockWarehouse);
//
//        Supplier supplier = new Supplier();
//        supplier.setName("Test Supplier");
//        //when
//        when(weightConversionService.calculateConversionRate(any(BigDecimal.class), any(BigDecimal.class)))
//                .thenReturn(BigDecimal.valueOf(1.983));  // Ensure this method does not return null
//        when(suppliersService.getSupplier(1L)).thenReturn(supplier);
//        when(purchaseOrderService.getPurchaseOrder(1L)).thenReturn(new PurchaseOrder());
//        when(steelSpecificationService.handleSteelSpecification(any(SteelCoilPostDTO.class))).thenReturn(steelSpecification);
//        when(grvsRepository.save(any(GRV.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(steelCoilService.createNewSteelCoil(any(), any(), any(), any(), any(), any(), any(), any()))
//                .thenReturn(new SteelCoil());
//        when(consumableInWarehouseRepository.findByConsumableIdAndWarehouseId(anyLong(), anyLong()))
//                .thenReturn(Optional.of(new ConsumablesInWarehouse()));
//
//        // then
//        GRVDetailsDTO result = grvAddService.createGRV(postGRV);
//
//        // verify
//        assertNotNull(result);
//        verify(steelCoilService).createNewSteelCoil(any(), any(), any(), any(), any(), any(), any(), any());
//        verify(suppliersService).getSupplier(1L);
//        assertEquals("Test Supplier", supplier.getName());
//        verify(logger).info("GRV saved successfully.");
//
//
//    }
//
//    private static GRVDetailsDTO getGrvDetailsDTO(Warehouse mockWarehouse) {
//        SteelCoilPostDTO steelCoilPostDTO = new SteelCoilPostDTO();
//        Gauge gauge = new Gauge();
//        gauge.setGauge(new BigDecimal("0.25"));
//        steelCoilPostDTO.setGauge(gauge);
//        steelCoilPostDTO.setWeightInKgsOnArrival(BigDecimal.valueOf(1000));
//
//        GRVDetailsDTO postGRV = new GRVDetailsDTO();
//        postGRV.setSteelCoils(List.of(steelCoilPostDTO));
//        postGRV.setConsumablesOnGrv(List.of(new ConsumablePostDTO()));
//        postGRV.setPurchaseOrderId(1L);
//        postGRV.setSupplierId(1L);
//        postGRV.setWarehouse(mockWarehouse);
//        return postGRV;
//    }
//
//
//    @Test
//    void testHandleGRVsProductsNullConversionRate() {
//        // given
//        Warehouse mockWarehouse = new Warehouse();
//        mockWarehouse.setId(1L);
//
//        Gauge gauge = new Gauge();
//        gauge.setGauge(new BigDecimal("1.0"));
//        Width width = new Width();
//        width.setWidth(new BigDecimal("925"));
//
//        SteelSpecification steelSpecification = new SteelSpecification();
//        steelSpecification.setWidth(width); // Ensure width is set
//
//        SteelCoilPostDTO steelCoilPostDTO = new SteelCoilPostDTO();
//        steelCoilPostDTO.setGauge(gauge);
//        steelCoilPostDTO.setWidth(width);
//
//        GRV newGRV = new GRV();
//        Supplier supplier = new Supplier();
//        supplier.setName("Test Supplier");
//
//        List<SteelCoilPostDTO> steelCoils = List.of(steelCoilPostDTO);
//
//        //when
//        when(steelSpecificationService.handleSteelSpecification(any(SteelCoilPostDTO.class))).thenReturn(steelSpecification);
//        when(weightConversionService.calculateConversionRate(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(BigDecimal.ZERO);
//
//        // then
//        assertThrows(RateNotSavedException.class, () ->
//            grvAddService.handleGRVsProducts(steelCoils, newGRV, mockWarehouse, supplier),
//                "Expected RateNotSavedException to be thrown due to zero conversion rate");
//    }
//
//    @Test
//    void testCalculateConversionRate() {
//        // given
//        BigDecimal expectedWidth = new BigDecimal("925");
//        BigDecimal expectedGauge = new BigDecimal("1.0");
//        BigDecimal expectedConversionRate = new BigDecimal("10");  // Example expected rate
//
//        when(steelSpecificationService.handleSteelSpecification(any(SteelCoilPostDTO.class)))
//                .thenReturn(new SteelSpecification());
//        when(weightConversionService.calculateConversionRate(any(BigDecimal.class), any(BigDecimal.class)))
//                .thenReturn(expectedConversionRate);
//
//        // when
//        BigDecimal result = weightConversionService.calculateConversionRate(expectedWidth, expectedGauge);
//
//        // then
//        assertNotNull(result, "Result should not be null");
//        assertEquals(expectedConversionRate, result, "Conversion rate should match the expected value.");
//    }
//
//    @Test
//    public void testEmptyProductsThrowsException() {
//
//        // Given
//        postGRV = mock(GRVDetailsDTO.class);
//
//        // when
//        when(postGRV.getSteelCoils()).thenReturn(Collections.emptyList());
//        when(postGRV.getConsumablesOnGrv()).thenReturn(Collections.emptyList());
//
//        // then
//        Exception exception = assertThrows(NoProductsFoundException.class, () -> {
//            if(postGRV.getSteelCoils().isEmpty() && postGRV.getConsumablesOnGrv().isEmpty()){
//                logger.info("No products to add. Returning...");
//                throw new NoProductsFoundException(409,"No products to add.");
//            }
//        });
//
//        // verify
//        assertEquals("No products to add.", exception.getMessage());
//
//        verify(logger).info("No products to add. Returning...");
//    }
//
//    @Test
//    public void testDuplicateCoilNumberThrowsException() {
//        // given
//        Warehouse warehouse = new Warehouse();
//        warehouse.setId(12345L);
//        SteelCoilPostDTO coil1 = new SteelCoilPostDTO();
//        coil1.setCoilNumber("12345");
//        coil1.setWarehouse(warehouse);
//        List<SteelCoilPostDTO> products = List.of(coil1);
//
//        // when
//        when(steelCoilService.getSteelCoilByCoilNumberInWarehouse(12345L, "12345"))
//                .thenReturn(Optional.of(new SteelCoil()));
//
//
//        // then
//        assertThrows(SteelCoilNumberExists.class, () -> {
//            grvAddService.runCoilNumberDatabaseChecks(products, warehouse);
//        });
//        // verify
//        verify(logger).error("Found a steel coil with the same coil number in the same warehouse. Coil number: {}","12345");
//    }
//
//
//


}