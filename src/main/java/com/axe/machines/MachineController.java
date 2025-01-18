package com.axe.machines;

import com.axe.machines.machinesDTO.MachinesIdAndName;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping("all-machines")
    public List<Machine> getAllMachines(){
        return machineService.getAllMachines();
    }

    @GetMapping("machine/{machineId}")
    public Machine getMachine(@PathVariable Long machineId){
        return machineService.getMachine(machineId);
    }

    @GetMapping("all-machines-details")
    public List<MachinesIdAndName> getAllMachinesDetails(){
        return machineService.getAllMachinesDetails();
    }
}
