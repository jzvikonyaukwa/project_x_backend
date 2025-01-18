package com.axe.steelCoils;

import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;


@ExtendWith(MockitoExtension.class)
class SteelCoilServiceTest {
    @InjectMocks
    SteelCoilService steelCoilService;
    @Mock
    SteelCoilRepository steelCoilRepository;
    @Mock
    private Page<SteelCoilDetailsDTO> steelCoilPage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


//    @Test
//    void getAvailableSteelCoilsForMachine() {
//        // given
//        BigDecimal width = new BigDecimal("1.0");
//
//        SteelCoilDetailsDTO coil1 = mock(SteelCoilDetailsDTO.class);
//        when(coil1.getEstMtrsRemaining()).thenReturn(new BigDecimal("100"));
//
//        SteelCoilDetailsDTO coil2 = mock(SteelCoilDetailsDTO.class);
//        when(coil2.getEstMtrsRemaining()).thenReturn(new BigDecimal("0"));
//
//        SteelCoilDetailsDTO coil3 = mock(SteelCoilDetailsDTO.class);
//        when(coil3.getEstMtrsRemaining()).thenReturn(new BigDecimal("50"));
//
//        List<SteelCoilDetailsDTO> availableCoils = Arrays.asList(coil1, coil2, coil3);
//
//        //when
//        when(steelCoilRepository.getAvailableSteelCoilsForMachine(width)).thenReturn(availableCoils);
//
//        List<SteelCoilDetailsDTO> result = steelCoilService.getAvailableSteelCoilsForMachine(width);
//        // Then
//        assertEquals(2, result.size());
//        assertTrue(result.contains(coil1));
//        assertFalse(result.contains(coil2));
//        assertTrue(result.contains(coil3));
//
//    }
//
//    @Test
//    void testGetFilteredAvailableSteelCoils() {
//        // given
//        Double gauge = 1.0;
//        String color = "blue";
//
//        SteelCoilDetailsDTO dto1 = mock(SteelCoilDetailsDTO.class);
//        SteelCoilDetailsDTO dto2 = mock(SteelCoilDetailsDTO.class);
//        List<SteelCoilDetailsDTO> expectedDTOs = Arrays.asList(dto1, dto2);
//
//        // Mock the behavior of getEstMtrsRemaining() the new update
//        when(dto1.getEstMtrsRemaining()).thenReturn(new BigDecimal("10.0"));
//        when(dto2.getEstMtrsRemaining()).thenReturn(new BigDecimal("20.0"));
//
//        // when
//        when(steelCoilRepository.getFilteredAvailableSteelCoils(BigDecimal.valueOf(182), gauge, color)).thenReturn(expectedDTOs);
//
//        List<SteelCoilDetailsDTO> actualDTOs = steelCoilService.getFilteredAvailableSteelCoils(BigDecimal.valueOf(182), gauge, color);
//
//        // Then
//        assertEquals(expectedDTOs, actualDTOs);
//    }
}