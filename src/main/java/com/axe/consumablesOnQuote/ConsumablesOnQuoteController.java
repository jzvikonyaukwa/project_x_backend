package com.axe.consumablesOnQuote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumables-on-quote")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsumablesOnQuoteController {

    private final ConsumablesOnQuoteService consumablesOnQuoteService;
    private static final Logger logger = LoggerFactory.getLogger(ConsumablesOnQuoteController.class);
    public ConsumablesOnQuoteController(ConsumablesOnQuoteService consumablesOnQuoteService) {
        this.consumablesOnQuoteService = consumablesOnQuoteService;
    }

    @DeleteMapping("{id}")
    public void deleteConsumableOnQuote(@PathVariable Long id) {
        consumablesOnQuoteService.deleteConsumableOnQuote(id);
    }
}
