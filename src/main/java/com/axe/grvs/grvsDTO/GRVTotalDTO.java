package com.axe.grvs.grvsDTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GRVTotalDTO {
    private Long id;
    private LocalDate dateReceived;
    private Double grvTotal;
}
