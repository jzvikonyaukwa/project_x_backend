package com.axe.quotePrice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quote-prices")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class QuotePriceController {

    private final QuotePriceService quotePriceService;

    public QuotePriceController(QuotePriceService quotePriceService) {
        this.quotePriceService = quotePriceService;
    }

    @GetMapping()
    public List<QuotePrice> getAllQuotePrices() {
        return quotePriceService.getAllQuotePrices();
    }

    @PostMapping()
    public QuotePrice addQuotePrice(@RequestBody QuotePrice quotePrice){
        return quotePriceService.saveQuotePrice(quotePrice);
    }

    @GetMapping("get-quote/{id}")
    public QuotePrice getQuote(@PathVariable Long id){
        return quotePriceService.getQuotePriceById(id);
    }

    @PutMapping()
    public QuotePrice updateQuotePrice(@RequestBody QuotePrice quotePrice){
        return quotePriceService.saveQuotePrice(quotePrice);
    }
}
