package com.axe.machineEvents;

import com.axe.machineEvents.DTOS.CoilIdInUse;
import com.axe.machineEvents.DTOS.MachineEventDTO;
import com.axe.machineEvents.DTOS.machinesEventsSummaryDTO.MachineTotalMetersCutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MachineEventsRepository extends JpaRepository<MachineEvent, Long> {
    @Query(value = """
            SELECT id,machine_id as machineId, steel_coil_id as steelCoilId, cuts_made as cutsMade,\s
            total_meters_cut as totalMetersCut, loaded_time as loadedTime, unloaded_time as unloadedTime
            FROM axe.machine_events\s
            WHERE machine_id = :machineId ;""", nativeQuery = true)
    List<MachineEventDTO> getAllMachineEventsForMachineId(Long machineId);

    @Query(value ="SELECT * FROM axe.machine_events WHERE machine_id = :machineId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    MachineEvent getMachinesLastEvent(@Param("machineId") Long machineId);



    @Query(value = """
            SELECT *\s
               FROM axe.machine_events\s
               WHERE machine_id = :machineId ORDER BY id DESC LIMIT 1 \s""", nativeQuery = true)
    CoilIdInUse getCoilIdInUseForMachine(Long machineId);

    @Query("SELECT new com.axe.machineEvents.DTOS.machinesEventsSummaryDTO.MachineTotalMetersCutDTO(m.name, COALESCE(SUM(me.totalMetersCut), 0.0)) " +
            "FROM Machine m LEFT JOIN m.machineEvents me " +
            "ON me.loadedTime BETWEEN :startDate AND :endDate " +
            "GROUP BY m.name")
    List<MachineTotalMetersCutDTO> getTotalMetersCutByMachine(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT me FROM MachineEvent me WHERE me.steelCoil.id = :steelCoilLd ORDER BY me.loadedTime DESC")
    List<MachineEvent> findMachineEventsBySteelCoilId(@Param("steelCoilLd") Long steelCoilLd);







}
