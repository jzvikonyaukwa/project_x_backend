package com.axe.interBranchTransfer.DTOs;


import java.math.BigDecimal;
import java.time.LocalDate;

public interface SteelCoilInterBranchTransferDetails {

    Long getInterBranchTransferId();
    LocalDate getDateTransferred();
    Long getSteelCoilIdFrom();
    String getCoilNumberFrom();
    String getCardNumberFrom();
    BigDecimal getLandedCostPerMtrFrom();
    String getColor();
    String getISQGrade();
    Float getWidth();
    String getCoating();
    Float getGauge();
    Long getSteelCoilIdTo();
    String getCoilNumberTo();
    String getCardNumberTo();

    Float getMtrsTransferred();

}
