package com.axe.price_history;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
@Table(name = "price_history")
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "changed_at")
    private LocalDateTime changedAt;
    @Column(name = "old_price")
    private BigDecimal oldPrice;
    @Column(name = "new_price")
    private BigDecimal newPrice;

}
