package com.axe.consumablesOnQuote;

import com.axe.consumables.Consumable;
import com.axe.inventories.Inventory;
import com.axe.invoices.Invoice;
import com.axe.quotes.Quote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consumables_on_quote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"quote", "invoice", "inventoryConsumables"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quote", "invoice", "inventoryConsumables"})
public class ConsumableOnQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "unit_price")
    private BigDecimal costPrice;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @Column(name = "invoice_id", insertable = false, updatable = false)
    private Long invoiceId;

    @ManyToOne()
    @JoinColumn(name = "consumables_id")
    private Consumable consumable;

    @ManyToOne()
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @Column(name = "has_custom_markup")
    private Boolean hasCustomMarkup;

    @ManyToOne()
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @Builder.Default
    @OneToMany(mappedBy = "consumable", cascade = CascadeType.ALL)
    private List<Inventory> inventoryConsumables = new ArrayList<>();

}
