package com.axe.productTransactions;


import com.axe.productTransactions.dtos.ProductTransactionDetails;
import com.axe.productTransactions.dtos.TransactionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductTransactionsRepository extends JpaRepository<ProductTransaction, Long>, PagingAndSortingRepository<ProductTransaction, Long>,ProductTransactionsCustomRepository{

    @Query(value = """
            SELECT pt.id, pt.date, p.total_length, p.id as productId,
            w.mtrs_waste as mtrsWasted, soh.length as stockOnHandLength, wid.width
            FROM axe.product_transactions as pt
            LEFT JOIN axe.aggregated_products amp ON pt.id = amp.product_transaction_id
            LEFT JOIN axe.products as p ON p.id = amp.product_id
            LEFT JOIN axe.steel_coils as sc ON sc.id = pt.steel_coil_id
            LEFT JOIN axe.steel_specifications as ss ON ss.id = sc.steel_specification_id
            LEFT JOIN axe.widths as wid ON wid.id = ss.width_id
            LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
            LEFT JOIN axe.stock_on_hand as soh ON soh.product_transaction_id = pt.id
            WHERE wid.width = :width
    """ , nativeQuery = true)
    List<TransactionDTO> getProductTransactionsForThisWidth(int width);

    @Query(value = """
        SELECT pt.id, pt.date, amp.id as manufacturedProductId, p.length, p.id as productId,
        p.code as manufacturedProductCode, p.frame_name as frameName, 
        sc.id as steelCoilId, sc.coil_number as coilNumber,
        w.id as wastageId, w.mtrs_waste as mtrsWasted,
        soh.id as stockOnHandId, soh.length as stockOnHandLength
        FROM axe.product_transactions as pt
        LEFT JOIN axe.aggregated_products amp ON pt.id = amp.product_transaction_id
        LEFT JOIN axe.products as p ON p.id = amp.product_id
        LEFT JOIN axe.steel_coils as sc ON sc.id = pt.steel_coil_id
        LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
        LEFT JOIN axe.stock_on_hand as soh ON soh.product_transaction_id = pt.id
        """ , nativeQuery = true)
    List<ProductTransactionDetails> getAllProductTransactionDetails();

    @Query(value = """
        SELECT pt.id, pt.date, amp.id as manufacturedProductId, p.length, p.id as productId,
        p.code as manufacturedProductCode, p.frame_name as frameName, 
        sc.id as steelCoilId, sc.coil_number as coilNumber,
        w.id as wastageId, w.mtrs_waste as mtrsWasted,
        soh.id as stockOnHandId, soh.length as stockOnHandLength
        FROM axe.product_transactions as pt
        LEFT JOIN axe.aggregated_products amp ON pt.id = amp.product_transaction_id
        LEFT JOIN axe.products as p ON p.id = amp.product_id
        LEFT JOIN axe.steel_coils as sc ON sc.id = pt.steel_coil_id
        LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
        LEFT JOIN axe.stock_on_hand as soh ON soh.product_transaction_id = pt.id
        """ , nativeQuery = true)
    Page<ProductTransactionDetails> getAllProductTransactionDetailsByPagination(Pageable pageable);
}
