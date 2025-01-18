package com.axe.consumableSource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SourceRepository extends JpaRepository<SourceCountry, Long> {
    @Query("SELECT s FROM SourceCountry s WHERE s.country = ?1")
    SourceCountry findByName(String sourceCountry);
}
