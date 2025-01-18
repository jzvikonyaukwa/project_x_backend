package com.axe.delivery_notes;

import com.axe.delivery_notes_consumables_on_quote.DeliveryNoteHasConsumablesOnQuote;
import com.axe.inventories.Inventory;
import com.axe.projects.Project;
import com.axe.quotes.Quote;
import com.axe.returned_products.ReturnedProducts;
import com.axe.saleOrder.SaleOrder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@Table(name = "delivery_notes")
@Entity
@ToString

public class DeliveryNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @Column(name = "date_delivered")
    private LocalDate dateDelivered;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference("project-deliveryNotes")
    private Project project;

    @OneToMany(mappedBy = "deliveryNote", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("deliveryNote-inventories")
    private List<Inventory> inventories = new ArrayList<>();

}
