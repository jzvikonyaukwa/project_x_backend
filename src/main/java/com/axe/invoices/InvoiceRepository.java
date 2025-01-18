package com.axe.invoices;

import com.axe.clients.Client;
import com.axe.invoices.DTOs.InvoiceResponseDTO;
import com.axe.quotePrice.QuotePrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> //
, JpaSpecificationExecutor<Invoice> {


    @Query("""
        SELECT
            c
        FROM
            Invoice i
        JOIN
            i.saleOrder so
        JOIN
            so.quote q
        JOIN
            q.project p
        JOIN
            p.client c
        WHERE
            i.id = :invoiceId
    """)
    Optional<Client> findClientByInvoiceId(@Param("invoiceId") Long invoiceId);


    @Query(value= """
            select 
            i.id,
            i.date_invoiced as dateInvoiced,
            i.paid,
            so.quote_id as quoteId,
            c.name as name
            from
            invoices i join sale_orders so on i.sale_order_id = so.id
            join quotes q on q.id =so.quote_id
            join projects p on p.id =q.project_id
            join clients c on c.id =p.client_id 
            order by i.id DESC;

""", nativeQuery = true)
    Page<InvoiceResponseDTO> findAllInvoices(Pageable pageable);
}
