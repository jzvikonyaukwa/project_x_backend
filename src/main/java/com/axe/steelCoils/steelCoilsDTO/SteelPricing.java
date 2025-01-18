package com.axe.steelCoils.steelCoilsDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SteelPricing {
    private GaugeAndPrice preferredPricing;
    private GaugeAndPrice optionalPricing;
}
