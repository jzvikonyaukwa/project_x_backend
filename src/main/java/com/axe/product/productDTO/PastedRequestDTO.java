package com.axe.product.productDTO;

import com.axe.colors.Color;
import com.axe.gauges.Gauge;
import com.axe.profile.Profile;
import com.axe.width.Width;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PastedRequestDTO {
    List<PastedAggregatedRequestDTO> aggregatedProducts;
    Long invoiceId;

    String finish;
    String notes;
    String status;

    LocalDateTime dateWorkBegan;
    LocalDateTime dateWorkCompleted;
    LocalDateTime lastWorkedOn;
    LocalDate targetDate;

    String priority;
    Gauge gauge;
    Color color;
    Width width;
    Profile profile;
    Boolean canInvoice;

}
