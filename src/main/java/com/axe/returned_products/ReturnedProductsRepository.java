package com.axe.returned_products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnedProductsRepository extends JpaRepository<ReturnedProducts, Long> {
//    List<ReturnedProducts> findByDeliveryNoteId(Long deliveryNoteId);
//    List<ReturnedProducts> findByCreditNoteId(Long creditNoteId);
}
