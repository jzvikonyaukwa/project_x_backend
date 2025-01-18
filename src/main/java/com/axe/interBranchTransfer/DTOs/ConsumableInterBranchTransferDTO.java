package com.axe.interBranchTransfer.DTOs;



import java.time.LocalDateTime;


public record ConsumableInterBranchTransferDTO ( Long consumableInWarehouseFromId,
        Integer qty,
        LocalDateTime date){

}
