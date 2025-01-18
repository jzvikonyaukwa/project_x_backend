package com.axe.finishes;

import com.axe.finishes.DTOs.FinishDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinishesRepository extends JpaRepository<Finish, Long> {

    @Query(value= """
            SELECT * FROM axe.finishes;\s
            """, nativeQuery = true)
    List<FinishDTO> getAllFinishes();
}
