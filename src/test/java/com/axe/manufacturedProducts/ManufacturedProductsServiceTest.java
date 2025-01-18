package com.axe.manufacturedProducts;

import com.axe.inventories.InventoryService;
import com.axe.machineEvents.MachineEventsService;
import com.axe.productTransactions.ProductTransaction;
import com.axe.productTransactions.ProductTransactionsService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManufacturedProductsServiceTest {

//    @Mock
//    private ManufacturedProductsRepository manufacturedProductsRepository;
//
//    @Mock
//    private ProductTransactionsService productTransactionsService;
//
//
//    @InjectMocks
//    private ManufacturedProductsService manufacturedProductsService;
//
//    @Mock
//    InventoryService inventoryService;
//
//    @Mock
//    SteelCoilService steelCoilService;
//
//    @Mock
//    MachineEventsService machineEventsService;
//
//
//    private ManufacturedProduct manufacturedProduct;
//    private ProductPostDTO productPostDTO;
//    private ProductTransaction productTransaction;
//
//    @BeforeEach
//    void setUp() {
//        CuttingList cuttingList = new CuttingList();
//        manufacturedProduct = new ManufacturedProduct();
//        manufacturedProduct.setId(1L);
//        manufacturedProduct.setCuttingList(cuttingList);
//        manufacturedProduct.setStatus("incomplete");
//        cuttingList.setStatus("completed");
//
//        productPostDTO = new ProductPostDTO(1L, 1L, LocalDateTime.now().toString());
//        productTransaction = new ProductTransaction();
//    }

    @Test
    @Transactional
    void testProductManufactured_Success() {
        // Ensure manufacturedProduct is a mock
//        ManufacturedProduct manufacturedProduct = mock(ManufacturedProduct.class);
//
//
//        // Mocking the CuttingList and Manufactured product status
//        CuttingList cuttingList = mock(CuttingList.class);
//        when(cuttingList.getDateWorkBegan()).thenReturn(LocalDateTime.now());
//        when(manufacturedProduct.getStatus()).thenReturn("completed");
//
//
//        // Ensure manufacturedProduct.getCuttingList() returns the mock CuttingList
//        when(manufacturedProduct.getCuttingList()).thenReturn(cuttingList);
//
//        // Mocking  dependencies
//        when(manufacturedProductsRepository.findById(anyLong())).thenReturn(Optional.of(manufacturedProduct));
////        when(manufacturedProductsRepository.getProductTransactionId(anyLong())).thenReturn(null);
//        when(productTransactionsService.save(any(ProductTransaction.class))).thenReturn(productTransaction);
//        when(manufacturedProductsRepository.save(any(ManufacturedProduct.class))).thenReturn(manufacturedProduct);
//
//        // Mocking the new dependencies
//        SteelCoil steelCoil = new SteelCoil();
//        steelCoil.setId(1L);
//        when(steelCoilService.getByCoilId(anyLong())).thenReturn(steelCoil);
//        doNothing().when(machineEventsService).updateTotalMetersCut(anyLong(), any(BigDecimal.class));
//
//        // Ensure product.getTotalProductlength() returns a non-null value
//        when(manufacturedProduct.getTotalLength()).thenReturn(BigDecimal.valueOf(100));


//        ManufacturedProduct result = manufacturedProductsService.productManufactured(productPostDTO);

        // Assertions
//        assertNotNull(result);
//        assertEquals("completed", result.getStatus());

        // Verifications
//        verify(manufacturedProductsRepository).findById(anyLong());
//        verify(manufacturedProductsRepository).getProductTransactionId(anyLong());
//        verify(productTransactionsService).save(any(ProductTransaction.class));
//        verify(manufacturedProductsRepository, times(1)).save(any(ManufacturedProduct.class));
//
//        // New verifications
//        verify(steelCoilService).getByCoilId(anyLong());
//        verify(machineEventsService).updateTotalMetersCut(anyLong(), any(BigDecimal.class));
    }






    @Test
    @Transactional
    void testProductManufactured_ProductAlreadyManufactured() {
//        when(manufacturedProductsRepository.findById(anyLong())).thenReturn(Optional.of(manufacturedProduct));
////        when(manufacturedProductsRepository.getProductTransactionId(anyLong())).thenReturn(1L);
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> manufacturedProductsService.productManufactured(productPostDTO));
//
////        assertEquals("Product already manufactured", exception.getMessage());
//        verify(manufacturedProductsRepository).findById(anyLong());
////        verify(manufacturedProductsRepository).getProductTransactionId(anyLong());
//        verify(manufacturedProductsRepository, never()).save(any(ManufacturedProduct.class));
//        verify(productTransactionsService, never()).save(any(ProductTransaction.class));
    }
}
