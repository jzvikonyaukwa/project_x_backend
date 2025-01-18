package com.axe.saleOrder;

import com.axe.common.enums.SaleOrderStatus;

import com.axe.invoices.Invoice;

import com.axe.quotes.Quote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale_orders")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaleOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_created")
    private LocalDate dateIssued;

    @Column(name = "status")
    private SaleOrderStatus status;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @OneToOne()
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL)
    private List<Invoice> invoices = new ArrayList<>();

//    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference("saleOrder-deliveryNotes")
//    private List<DeliveryNote> deliveryNotes;

//    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonManagedReference("saleOrder-creditNotes")
//    private List<CreditNote> creditNotes;

}
