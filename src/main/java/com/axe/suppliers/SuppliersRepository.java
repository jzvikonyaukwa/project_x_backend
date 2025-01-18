package com.axe.suppliers;

import com.axe.suppliers.SupplierDTOs.SupplierDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SuppliersRepository extends JpaRepository<Supplier, Long> {


    @Query(value= """
            SELECT supplier.id as supplierId, supplier.name as supplierName, phone as supplierPhone, 
            email as supplierEmail, street as supplierStreet, suburb as supplierSuburb, city as supplierCity, 
            country as supplierCountry
           FROM axe.suppliers as supplier
           LEFT JOIN axe.supplier_phones as phone ON phone.supplier_id = supplier.id AND phone.label = 'main'
           LEFT JOIN axe.supplier_emails as email ON email.supplier_id = supplier.id AND email.label = 'main'
           LEFT JOIN axe.supplier_addresses as address ON address.supplier_id = supplier.id AND address.label = 'main'
           ORDER BY supplier.name
           """,nativeQuery = true)
    List<SupplierDetailsDTO> getAllSuppliersWithDetails();
}
