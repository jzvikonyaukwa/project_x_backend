package com.axe.wastage.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class AddWastageDTO {
    Long steelCoilId;
    Long productId;
    BigDecimal wastageInMeters;
    LocalDateTime date;
}
