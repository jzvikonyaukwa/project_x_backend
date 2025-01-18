package com.axe.machineEvents;

import com.axe.machineEvents.DTOS.CoilIdInUse;
import com.axe.machineEvents.DTOS.MachineEventDTO;
import com.axe.machines.Machine;
import com.axe.machines.MachineService;
import com.axe.machineEvents.DTOS.machinesEventsSummaryDTO.MachineTotalMetersCutDTO;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MachineEventsService {

    private final MachineEventsRepository machineEventsRepository;
    private final MachineService machineService;
    private final SteelCoilService steelCoilService;
    Logger logger = LoggerFactory.getLogger(MachineEventsService.class);
    private static final BigDecimal TOLERANCE_PERCENTAGE = new BigDecimal("0.03"); // 3%


    public MachineEventsService(MachineEventsRepository machineEventsRepository,
                                MachineService machineService, SteelCoilService steelCoilService) {
        this.machineEventsRepository = machineEventsRepository;
        this.machineService = machineService;
        this.steelCoilService = steelCoilService;
    }

    public List<MachineEventDTO> getAllMachineEventsForMachineId(Long machineId) {
        return machineEventsRepository.getAllMachineEventsForMachineId(machineId);
    }

    public MachineEvent getMachinesLastEvent(Long machineId) {
        MachineEvent machineEvent =machineEventsRepository.getMachinesLastEvent(machineId);
        logger.info("machineEvent: {}", machineEvent.getLoadedTime());
        checkIfCoilIsFinished(machineEvent.getSteelCoil());
        return machineEvent;
    }

    private void checkIfCoilIsFinished(SteelCoil steelCoil) {
        if (steelCoil.getEstimatedMetersRemaining().equals(BigDecimal.ZERO)) {
            steelCoil.setStatus("finished");
            steelCoilService.save(steelCoil);
        }
    }

    public MachineEvent loadCoil(Long machineId, Long steelCoilId) {



        Machine machine = machineService.getMachine(machineId);
        if(machine == null){
            logger.error("Machine not found");
            throw new RuntimeException("Machine not Found");
        }

        SteelCoil steelCoilToBeLoaded = steelCoilService.getSteelCoil(steelCoilId);
        if(steelCoilToBeLoaded == null){
            logger.error("Steel coil not found");
            throw new IllegalArgumentException("Steel coil not found");
        }
        else steelCoilToBeLoaded.setStatus("in-use");
        steelCoilService.save(steelCoilToBeLoaded);

        logger.info("steelCoilToBeLoaded: {}", steelCoilToBeLoaded);

       boolean steelCoilExist = searchExistingInUseSteelCoil(
                steelCoilToBeLoaded.getSteelSpecification().getGauge().getGauge(),
                steelCoilToBeLoaded.getSteelSpecification().getColor().getColor(),
                steelCoilToBeLoaded.getSteelSpecification().getCoating()
       );

        logger.info("steelCoilToBeLoaded to be loaded: {} on machine: {}", steelCoilToBeLoaded.getCoilNumber(), machine.getId());

        MachineEvent machineEvent = getMachinesLastEvent(machineId);

        logger.info("machineEvent: {}", machineEvent);

        LocalDateTime now = LocalDateTime.now();

        if(machineEvent != null){

            // checking that the machine is 0 and the gauge is the same before change
            checkLoadingFactors(machineEvent, steelCoilToBeLoaded,steelCoilExist);

            machineEvent.setUnloadedTime(now);
            machineEventsRepository.save(machineEvent);
        }

        steelCoilToBeLoaded.setStatus("in-use");
        steelCoilService.save(steelCoilToBeLoaded);

        machineEvent =  MachineEvent
                .builder()
                .machine(machine)
                .steelCoil(steelCoilToBeLoaded)
                .loadedTime(now)
                .totalMetersCut(0.0F)
                .cutsMade(0)
                .build();


        logger.info("New machineEvent: {}", machineEvent);

        return machineEventsRepository.save(machineEvent);
    }

    public void checkLoadingFactors(MachineEvent machineEvent, SteelCoil steelCoilToBeLoaded,boolean steelCoilExist) {
        BigDecimal gaugeMachineEvent = machineEvent.getSteelCoil().getSteelSpecification().getGauge().getGauge();
        BigDecimal gaugeSteelCoilToBeLoaded = steelCoilToBeLoaded.getSteelSpecification().getGauge().getGauge();

        String colorMachineEvent = machineEvent.getSteelCoil().getSteelSpecification().getColor().getColor();
        String colorSteelCoilToBeLoaded = steelCoilToBeLoaded.getSteelSpecification().getColor().getColor();

        String coatingMachineEvent = machineEvent.getSteelCoil().getSteelSpecification().getCoating();
        String coatingSteelCoilToBeLoaded = steelCoilToBeLoaded.getSteelSpecification().getCoating();

        BigDecimal estimatedMetersRemaining = machineEvent.getSteelCoil().getEstimatedMetersRemaining();

         if(steelCoilExist && (steelCoilToBeLoaded.getStatus().equals("in-stock"))){
           logger.warn("There exists a steel coil currently in use that matches the same specifications as the one being utilized.");
         throw new RuntimeException("There exists a steel coil currently in use that matches the same specifications as the one being utilized.");
         }
         if (isWithinTolerance(gaugeMachineEvent, gaugeSteelCoilToBeLoaded)
                && colorMachineEvent.equals(colorSteelCoilToBeLoaded)
                && coatingMachineEvent.equals(coatingSteelCoilToBeLoaded)
                && !(estimatedMetersRemaining.compareTo(BigDecimal.valueOf(0.00)) == 0)) {

            logger.warn("ERROR1: Make the current Loaded Machine 0, by adding to Missing metres or Wastage");
            throw new RuntimeException("Make the current Loaded Machine 0, by adding to Missing metres or Wastage");
         }
    }

    private boolean isWithinTolerance(BigDecimal value1, BigDecimal value2) {
        BigDecimal difference = value1.subtract(value2).abs();
        BigDecimal tolerance = value1.multiply(TOLERANCE_PERCENTAGE);
        return difference.compareTo(tolerance) <= 0;
    }

    private boolean searchExistingInUseSteelCoil(BigDecimal gauge, String color, String coating) {
        return steelCoilService.searchForSteelCoilUsingGaugeColorAndCoating(gauge, color, coating);
    }


    public CoilIdInUse getCoilIdInUseForMachine(Long machineId) {
        return machineEventsRepository.getCoilIdInUseForMachine(machineId);
    }

    public  List<MachineTotalMetersCutDTO> findTotalMetersCutByMachine(LocalDateTime startDate, LocalDateTime endDate) {
        return machineEventsRepository.getTotalMetersCutByMachine(startDate, endDate);
    }

    public void updateTotalMetersCut(Long steelCoilLd, BigDecimal metersCut) {
       // Update Meters Cut
        List<MachineEvent> machineEvents = machineEventsRepository.findMachineEventsBySteelCoilId(steelCoilLd);
        if (!machineEvents.isEmpty()) {
            MachineEvent latestMachineEvent = machineEvents.get(0); // The first one is the latest due to DESC ordering
            latestMachineEvent.setTotalMetersCut(latestMachineEvent.getTotalMetersCut() + metersCut.floatValue());
            latestMachineEvent.setCutsMade(latestMachineEvent.getCutsMade() + 1);
            machineEventsRepository.save(latestMachineEvent);
        } else {
            // Handle the case where no machine event is found
            throw new EntityNotFoundException("No MachineEvent found for coilId: " + steelCoilLd);
        }
    }
}
