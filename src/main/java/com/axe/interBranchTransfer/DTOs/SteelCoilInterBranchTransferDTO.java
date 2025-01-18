package com.axe.interBranchTransfer.DTOs;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public  record SteelCoilInterBranchTransferDTO(  Long steelCoilIdFrom,
BigDecimal metres,
LocalDateTime date){
  
}
