package com.axe.machineEvents.DTOS;

import java.time.LocalDateTime;

public interface MachineEventDTO {
    Long getId();
    Long getMachineId();
    Long getSteelCoilId();
    Integer getCutsMade();
    Float getTotalMetersCut();
    LocalDateTime getLoadedTime();
    LocalDateTime getUnloadedTime();
}
