package com.axe.width;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface WidthRepository extends JpaRepository<Width, Long> {
    @Query(value = "SELECT * FROM axe.widths w WHERE w.width = :width ", nativeQuery = true)
    Width findByWidth(BigDecimal width);
}
