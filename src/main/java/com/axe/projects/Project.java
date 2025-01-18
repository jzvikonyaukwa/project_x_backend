package com.axe.projects;

import com.axe.clients.Client;
import com.axe.credit_note.CreditNote;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.quotes.Quote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quotes", "deliveryNotes", "creditNotes"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    @ToString.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-deliveryNotes")
    private List<DeliveryNote> deliveryNotes = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-creditNotes")
    private List<CreditNote> creditNotes = new ArrayList<>();
}
