package com.axe.steelCoils.steelCoilsDTO;

import java.time.LocalDateTime;

public interface SteelCoilInUseForMachineDTO {

    Long getSteelCoilId();
    String getCompanyCoilId();
    String getCardNumber();
    Float getMetersRemaining();
    String getSteelCoilStatus();
//    Long getSteelSpecificationId();
    String getFinish();
    String getColor();
    String getIsqGrade();
    Float getWidth();
    String getCoating();
    Float getGauge();
//    LocalDateTime getDateUsed();
}
