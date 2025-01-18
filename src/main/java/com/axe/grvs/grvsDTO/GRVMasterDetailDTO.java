package com.axe.grvs.grvsDTO;
 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GRVMasterDetailDTO {
    private Long grvId;
    private String dateReceived;
    private String grvComments;
    private String supplierGrvCode;
    private String supplierName;
    private Long purchaseOrderId;
    @Builder.Default
    private List<GRVDetailDTO> details = new  ArrayList<>();
}
