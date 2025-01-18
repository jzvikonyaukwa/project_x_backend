package com.axe.machineEvents.DTOS.machinesEventsSummaryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MachineTotalMetersCutDTO {
    private String machineName;

    private double totalMetersCut;

}
