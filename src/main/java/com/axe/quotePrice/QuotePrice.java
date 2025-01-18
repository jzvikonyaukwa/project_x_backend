package com.axe.quotePrice;

import com.axe.quotes.Quote;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quote_price")
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quotes"})
public class QuotePrice implements Comparable<QuotePrice> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type")
    private String priceType;

    @Column(name = "mark_up")
    private BigDecimal markUp;

    @Column(name = "date_edited")
    private LocalDate dateEdited;

//    @OneToOne(mappedBy = "quotePrice", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private Quote quote;

    @OneToMany(mappedBy = "quotePrice", cascade = CascadeType.ALL)
    private List<Quote> quotes = new ArrayList<>();

    @PreUpdate
    public void preUpdate() {
        // prevent upating preset price types
        if ("custom".compareToIgnoreCase(priceType) == 0) {
            dateEdited = LocalDate.now();
        } else {
            throw new IllegalArgumentException("Price type must be custom");
        }
    }

    @Override
    public int compareTo(QuotePrice other) {
        int priceTypeComparison = this.priceType.compareToIgnoreCase(other.priceType);
        if (priceTypeComparison != 0) {
            return priceTypeComparison;
        }
        return this.markUp.compareTo(other.markUp);
    }
}
