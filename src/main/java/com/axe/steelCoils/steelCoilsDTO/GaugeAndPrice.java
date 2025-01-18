package com.axe.steelCoils.steelCoilsDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class GaugeAndPrice {
    private double gauge;
    private double price;

    public GaugeAndPrice(double gauge, double price) {
        this.gauge = gauge;
        this.price = price;
    }
}


