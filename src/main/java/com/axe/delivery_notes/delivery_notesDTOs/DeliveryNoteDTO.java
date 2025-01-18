package com.axe.delivery_notes.delivery_notesDTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryNoteDTO {
    private Long id;
    private LocalDate dateCreated;
    private LocalDate dateDelivered;
    private String deliveryAddress;
    private Long projectId;
    private List<Map<Long,Long>> consumablesIdsQuantities;
    private List<Long> manufacturedProductsID;
}
