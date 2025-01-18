package com.axe.price_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    @Modifying
    @Query("DELETE FROM PriceHistory ph WHERE ph.productId = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
