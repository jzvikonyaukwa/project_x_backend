package com.axe.consumablesOnQuote;

import com.axe.delivery_notes.DeliveryNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsumablesOnQuoteService {
    private static final Logger log = LoggerFactory.getLogger(DeliveryNoteService.class);

    private final ConsumablesOnQuoteRepository consumablesOnQuoteRepository;

    public ConsumablesOnQuoteService(ConsumablesOnQuoteRepository consumablesOnQuoteRepository) {
        this.consumablesOnQuoteRepository = consumablesOnQuoteRepository;
    }

    public ConsumableOnQuote saveConsumableOnQuote(ConsumableOnQuote consumableOnQuote) {
        return consumablesOnQuoteRepository.save(consumableOnQuote);
    }

    public ConsumableOnQuote getConsumableOnQuoteById(Long id) {
        return consumablesOnQuoteRepository.findById(id).orElseThrow();
    }

    public void deleteConsumableOnQuote(Long id) {
        consumablesOnQuoteRepository.deleteById(id);
    }
}
