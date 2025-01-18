package com.axe.invoices;

import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.product.Product;
import com.axe.saleOrder.SaleOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_invoiced")
    private LocalDate dateInvoiced;

    @Column(name = "paid")
    private Boolean paid;

    @ManyToOne()
    @JoinColumn(name = "sale_order_id")
    @JsonIgnore
    private SaleOrder saleOrder;

    @OneToMany(mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<ConsumableOnQuote> consumablesOnQuote = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products = new ArrayList<>();

}
