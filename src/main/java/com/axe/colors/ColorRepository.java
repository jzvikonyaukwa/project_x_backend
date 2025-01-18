package com.axe.colors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {

    @Query(value= """
            SELECT * FROM axe.colors as c
            WHERE c.color = :color ;""", nativeQuery = true)
    Optional<Color> findByColor(String color);
}
