package com.axe.gauges;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gauges")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class GaugeController {

    private final GaugeService gaugeService;

    public GaugeController(GaugeService gaugeService) {
        this.gaugeService = gaugeService;
    }

    @GetMapping("")
    public List<Gauge> getAllGauges(){
        return gaugeService.getAllGauges();
    }
}

