package com.axe.missing_metres.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public record MissingMetersRequest(
    Long steelCoilId,
    BigDecimal missingMeters,
    String reason,
    LocalDateTime loggedAt
){
}
