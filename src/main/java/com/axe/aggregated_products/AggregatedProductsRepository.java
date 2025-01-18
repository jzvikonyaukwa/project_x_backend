package com.axe.aggregated_products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AggregatedProductsRepository extends JpaRepository<AggregatedProduct, Long> {

    @Query(value = """
            SELECT amp.product_transaction_id
              FROM axe.aggregated_products as amp
              WHERE amp.id = :id ;
            """, nativeQuery = true)
    Long getProductTransactionId(Long id);

//    @Query("SELECT mp.cuttingList FROM AggregatedManufacturedProduct mp WHERE mp.id = :productId")
//    Optional<CuttingList> findCuttingListByProductId(@Param("productId") Long productId);
}
