package com.axe.delivery_notes_consumables_on_quote;

import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.delivery_notes.DeliveryNote;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "delivery_note_has_consumables_on_quote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryNoteHasConsumablesOnQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "delivery_note_id", nullable = false)
    @JsonBackReference("deliveryNote-consumablesOnQuotes")
    private DeliveryNote deliveryNote;

//    @ManyToOne
//    @JoinColumn(name = "consumables_on_quote_id", nullable = false)
//    private ConsumableOnQuote consumableOnQuote;
}
