package com.axe.machines;

import com.axe.machines.machinesDTO.MachinesIdAndName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    @Query(value = "SELECT id, name FROM axe.machines", nativeQuery = true)
    List<MachinesIdAndName> findAllMachinesDetails();
}
