package com.axe.prices;

import com.axe.colors.Color;
import com.axe.finishes.Finish;
import com.axe.gauges.Gauge;
import org.springframework.stereotype.Service;

@Service
public class PriceService {


    public float getPrice(Gauge gauge, Color color, Finish finish, float length){
        return  0.0f;
    }
}
