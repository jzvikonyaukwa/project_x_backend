package com.axe.consumable_category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConsumableCategoryRepository extends JpaRepository<ConsumableCategory, Long> {

    @Query("SELECT c FROM ConsumableCategory c WHERE c.name = ?1")
    ConsumableCategory findByName(String category);
}
