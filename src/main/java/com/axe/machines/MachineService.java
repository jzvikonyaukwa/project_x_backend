package com.axe.machines;

import com.axe.machines.machinesDTO.MachinesIdAndName;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MachineService {

    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    public Machine getMachine(Long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new RuntimeException("Machine not found"));
    }

    public List<MachinesIdAndName> getAllMachinesDetails() {
        return machineRepository.findAllMachinesDetails();

    }
}
