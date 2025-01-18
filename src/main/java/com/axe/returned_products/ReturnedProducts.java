package com.axe.returned_products;

import com.axe.credit_note.CreditNote;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.inventories.Inventory;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Table(name = "returned_products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class ReturnedProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reason")
    private String reason;

    @Column(name = "returned_date")
    private LocalDate returnedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "returnedProducts", fetch = FetchType.EAGER)
    @JsonManagedReference("returnedProduct-inventories")
    private List<Inventory> inventories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_note_id")
    @JsonBackReference("deliveryNote-returnedProducts")
    private DeliveryNote deliveryNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_note_id")
    @JsonBackReference("creditNote-returnedProducts")
    private CreditNote creditNote;
}
