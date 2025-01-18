package com.axe.missing_metres;

import com.axe.missing_metres.models.MissingMetersRequest;
import com.axe.productTransactions.ProductTransaction;
import com.axe.productTransactions.ProductTransactionsService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MissingMetresServiceTest {

    @InjectMocks
    private SteelCoilService steelCoilService;

    @Mock
    private MissingMetresRepository missingMetresRepository;

    @Mock
    private ProductTransactionsService productTransactionsService;

    @InjectMocks
    private MissingMetresService missingMetresService;

    private MissingMetersRequest missingMetersRequest;
    private SteelCoil steelCoil;
    private ProductTransaction productTransaction;

//    @BeforeEach
//    void setUp() {
//        missingMetersRequest = new MissingMetersRequest(1L, BigDecimal.valueOf(100), "Test Reason", LocalDateTime.now());
//
//        steelCoil = new SteelCoil();
//        steelCoil.setId(1L);
//
//        MissingMetres missingMetres = new MissingMetres();
//        missingMetres.setMtrsMissing(BigDecimal.valueOf(100));
//        missingMetres.setReason("Test Reason");
//        missingMetres.setStatus(Status.PENDING);
//        missingMetres.setLoggedAt(missingMetersRequest.loggedAt());
//
//        productTransaction = new ProductTransaction();
//        productTransaction.setId(1L);
//    }
//
//    @Test
//    @Transactional
//    void logMissingMeters_success() {
//        when(steelCoilService.getSteelCoil(missingMetersRequest.steelCoilId())).thenReturn(steelCoil);
//        when(productTransactionsService.createProductTransaction(eq(steelCoil.getId()), eq(missingMetersRequest.loggedAt()), any(MissingMetres.class)))
//                .thenReturn(productTransaction);
//
//        SteelCoil result = missingMetresService.logMissingMeters(missingMetersRequest);
//
//        assertEquals(steelCoil, result);
//        verify(missingMetresRepository, times(1)).save(any(MissingMetres.class));
//    }
//
//    @Test
//    @Transactional
//    void logMissingMeters_nullRequest_throwsException() {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> missingMetresService.logMissingMeters(null));
//        assertEquals("MissingMetresRequest cannot be null", exception.getMessage());
//    }
}
