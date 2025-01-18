package com.axe.gauges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GaugeRepository extends JpaRepository<Gauge, Long> {

    List<Gauge> findAllByOrderByGaugeAsc();

    @Query(value = "SELECT * FROM axe.gauges g WHERE g.gauge = :gauge ", nativeQuery = true)
    Gauge findByGauge(Float gauge);
}
