package com.axe.quotes;

import com.axe.common.enums.QuoteStatus;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.product.Product;
import com.axe.projects.Project;
import com.axe.quotePrice.QuotePrice;
import com.axe.quoteRejectionReasons.QuoteRejectionReason;
import com.axe.saleOrder.SaleOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "saleOrder"})
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_issued")
    private LocalDate dateIssued;

    @Column(name = "date_last_modified")
    private LocalDate dateLastModified;

    @Column(name = "date_accepted")
    private LocalDate dateAccepted;

    @Column(name = "date_rejected")
    private LocalDate dateRejected;

    @Column(name = "status")
    private QuoteStatus status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "paid")
    private Boolean paid;

    @ManyToOne()
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(mappedBy = "quote", cascade = CascadeType.ALL)
    private SaleOrder saleOrder;

    @ManyToOne()
    @JoinColumn(name = "quote_price_id")
    private QuotePrice quotePrice;

    @ManyToOne()
    @JoinColumn(name = "quote_rejection_reason_id")
    private QuoteRejectionReason rejectedReason;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumableOnQuote> consumablesOnQuote = new ArrayList<>();

}
