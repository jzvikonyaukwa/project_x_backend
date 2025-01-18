package com.axe.credit_note;

import com.axe.projects.Project;
import com.axe.quotes.Quote;
import com.axe.returned_products.ReturnedProducts;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "credit_notes")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class CreditNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonBackReference("project-creditNotes")
    private Project project;

    @OneToMany(mappedBy = "creditNote", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("creditNote-returnedProducts")
    private List<ReturnedProducts> returnedProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_order_quote_id")
    @JsonBackReference("quote-creditNotes")
    private Quote quote;
}
