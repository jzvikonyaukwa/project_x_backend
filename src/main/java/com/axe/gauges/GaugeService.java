package com.axe.gauges;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GaugeService {

    private final GaugeRepository gaugeRepository;

    public GaugeService(GaugeRepository gaugeRepository) {
        this.gaugeRepository = gaugeRepository;
    }

    public List<Gauge> getAllGauges(){
        return gaugeRepository.findAllByOrderByGaugeAsc();
    }

    public Gauge getGaugeById(Long id) {
        return gaugeRepository.findById(id).orElseThrow();
    }

    public Gauge getGaugeByGauge(Float gauge) {
        return gaugeRepository.findByGauge(gauge);
    }
}
