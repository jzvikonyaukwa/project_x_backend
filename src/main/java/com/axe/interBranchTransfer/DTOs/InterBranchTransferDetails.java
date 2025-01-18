package com.axe.interBranchTransfer.DTOs;

import java.time.LocalDate;

public interface InterBranchTransferDetails {
    Long getInterBranchTransferId();
    LocalDate getDateTransferred();
    Long getSteelCoilIdFrom();
    String getCoilNumberFrom();
    String getCardNumberFrom();
    Long getSteelCoilIdTo();
    String getCoilNumberTo();
    String getCardNumberTo();
    Float getMtrsTransferred();

}
