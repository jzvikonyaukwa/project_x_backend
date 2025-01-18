package com.axe.missing_metres;



import com.axe.productTransactions.ProductTransaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "missing_metres")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MissingMetres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mtrs_missing")
    private BigDecimal mtrsMissing;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_transaction_id")
    @JsonIgnore
    private ProductTransaction productTransaction;

    @Column(name = "reason")
    private String reason;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @Column(name = "status")
    private Status status;
}
