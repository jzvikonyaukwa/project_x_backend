package com.axe.product_type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductTypeRepository extends JpaRepository<ProductType,Long> {

    Optional<ProductType> findByName(String name);

}
