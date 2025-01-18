package com.axe.inventories;

import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.product.Product;
import com.axe.returned_products.ReturnedProducts;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Setter
@Getter
@Table(name = "customer_inventory")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_in")
    private LocalDate dateIn;

    @Column(name = "date_out")
    private LocalDate dateOut;

    @ManyToOne()
    @JoinColumn(name = "consumables_on_quote_id")
    private ConsumableOnQuote consumable;

    @OneToOne(mappedBy = "inventory", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private Product product;

    @ManyToOne
    @JoinColumn(name = "returned_product_id")
    @JsonBackReference("returnedProduct-inventories")
    private ReturnedProducts returnedProducts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_note_id")
    @JsonBackReference("deliveryNote-inventories")
    private DeliveryNote deliveryNote;

}
