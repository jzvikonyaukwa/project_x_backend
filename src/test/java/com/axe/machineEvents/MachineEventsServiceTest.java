package com.axe.machineEvents;

import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.machineEvents.DTOS.MachineEventDTO;
import com.axe.machines.Machine;
import com.axe.machines.MachineService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.steelSpecifications.SteelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MachineEventsServiceTest {
//
//    @Mock
//    private MachineEventsRepository machineEventsRepository;
//
//    @Mock
//    private MachineService machineService;
//
//    @Mock
//    private SteelCoilService steelCoilService;
//
//    @InjectMocks
//    private MachineEventsService machineEventsService;
//
//    @Captor
//    private ArgumentCaptor<SteelCoil> steelCoilCaptor;
//
//    private Machine machine;
//    private SteelCoil steelCoil;
//    private MachineEvent machineEvent;

//    @BeforeEach
//    void setUp() {
//        machine = new Machine();
//        machine.setId(1L);
//
//        SteelSpecification steelSpecification = new SteelSpecification();
//        Gauge gauge = new Gauge();
//        gauge.setGauge(new BigDecimal("0.5"));
//        steelSpecification.setGauge(gauge);
//        Color color = new Color();
//        color.setColor("color");
//        steelSpecification.setColor(color);
//        steelSpecification.setCoating("coating");
//
//        steelCoil = new SteelCoil();
//        steelCoil.setId(1L);
//        steelCoil.setStatus("in-stock");
//        steelCoil.setEstimatedMetersRemaining(BigDecimal.ZERO);
//        steelCoil.setSteelSpecification(steelSpecification);
//
//        machineEvent = new MachineEvent();
//        machineEvent.setMachine(machine);
//        machineEvent.setSteelCoil(steelCoil);
//        machineEvent.setLoadedTime(LocalDateTime.now());
//    }
//
//    @Test
//    void testGetAllMachineEventsForMachineId() {
//        Long machineId = 1L;
//        MachineEventDTO mockEvent = mock(MachineEventDTO.class);
//        List<MachineEventDTO> mockEvents = Collections.singletonList(mockEvent);
//        when(machineEventsRepository.getAllMachineEventsForMachineId(machineId)).thenReturn(mockEvents);
//
//        List<MachineEventDTO> events = machineEventsService.getAllMachineEventsForMachineId(machineId);
//
//        assertNotNull(events);
//        assertEquals(1, events.size());
//        verify(machineEventsRepository, times(1)).getAllMachineEventsForMachineId(machineId);
//    }
//
//    @Test
//    void testGetMachinesLastEvent() {
//        Long machineId = 1L;
//        when(machineEventsRepository.getMachinesLastEvent(machineId)).thenReturn(machineEvent);
//
//        MachineEvent result = machineEventsService.getMachinesLastEvent(machineId);
//
//        assertNotNull(result);
//        assertEquals(machine, result.getMachine());
//        verify(machineEventsRepository, times(1)).getMachinesLastEvent(machineId);
//    }
//
//    @Test
//    void testLoadCoil() {
//        Long machineId = 1L;
//        Long steelCoilId = 1L;
//
//        when(machineService.getMachine(machineId)).thenReturn(machine);
//        when(steelCoilService.getSteelCoil(steelCoilId)).thenReturn(steelCoil);
//        when(machineEventsRepository.getMachinesLastEvent(machineId)).thenReturn(machineEvent);
//        when(machineEventsRepository.save(any(MachineEvent.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        MachineEvent result = machineEventsService.loadCoil(machineId, steelCoilId);
//
//        assertNotNull(result);
//        assertEquals(machine, result.getMachine());
//        assertEquals(steelCoil, result.getSteelCoil());
//        verify(machineService, times(1)).getMachine(machineId);
//        verify(steelCoilService, times(1)).getSteelCoil(steelCoilId);
//        verify(machineEventsRepository, times(2)).save(any(MachineEvent.class));
//
//    }
//
//
//
//    @Test
//    void testCheckLoadingFactors() {
//        SteelSpecification steelSpecification = new SteelSpecification();
//        Gauge gauge = new Gauge();
//        gauge.setGauge(BigDecimal.valueOf(0.5));
//        steelSpecification.setGauge(gauge);
//        Color color = new Color();
//        color.setColor("color");
//        steelSpecification.setColor(color);
//        steelSpecification.setCoating("coating");
//
//        SteelCoil steelCoilToBeLoaded = new SteelCoil();
//        steelCoilToBeLoaded.setSteelSpecification(steelSpecification);
//        steelCoilToBeLoaded.setStatus("in-stock");
//
//        MachineEvent newMachineEvent = new MachineEvent();
//        newMachineEvent.setSteelCoil(steelCoil);
//        newMachineEvent.setMachine(machine);
//
//
//
//        assertDoesNotThrow(() -> machineEventsService.checkLoadingFactors(newMachineEvent, steelCoilToBeLoaded, false));
//    }

}
