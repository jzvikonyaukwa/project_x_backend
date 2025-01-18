package com.axe.machineEvents;

import com.axe.machineEvents.DTOS.CoilIdInUse;
import com.axe.machineEvents.DTOS.MachineEventDTO;
import com.axe.machineEvents.DTOS.machinesEventsSummaryDTO.MachineTotalMetersCutDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/machine-events")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class MachineEventsController {

    private final MachineEventsService machineEventsService;

    public MachineEventsController(MachineEventsService machineEventsService) {
        this.machineEventsService = machineEventsService;
    }

    @GetMapping("/machine/{machineId}/all-events")
    public List<MachineEventDTO> getAllMachineEventsForMachineId(@PathVariable Long machineId){
        return machineEventsService.getAllMachineEventsForMachineId(machineId);
    }

    @GetMapping("/machine/{machineId}/coil-in-use")
    public CoilIdInUse getCoilIdInUseForMachine(@PathVariable Long machineId){
        return machineEventsService.getCoilIdInUseForMachine(machineId);
    }

    @GetMapping("machines-last-event/{machineId}")
    public MachineEvent getMachinesLastEvent(@PathVariable Long machineId){
        return machineEventsService.getMachinesLastEvent(machineId);
    }

    @PostMapping("load-coil/{machineId}/{steelCoilId}")
    public MachineEvent loadCoil(@PathVariable Long machineId, @PathVariable Long steelCoilId){
        return machineEventsService.loadCoil(machineId, steelCoilId);
    }

    @GetMapping("/total-meters-cut")
    public ResponseEntity<List<MachineTotalMetersCutDTO>> getTotalMetersCutByMachine(  @RequestParam(value = "startDate", required = false) String startDateStr,
                                                                                       @RequestParam(value = "endDate",required = false)String endDateStr) {
        LocalDateTime endDate = (endDateStr != null) ? LocalDateTime.parse(endDateStr) : LocalDateTime.now();
        LocalDateTime startDate = (startDateStr != null) ? LocalDateTime.parse(startDateStr) : endDate.minusDays(30);
        return ResponseEntity.ok(machineEventsService.findTotalMetersCutByMachine(startDate, endDate));
    }

}
