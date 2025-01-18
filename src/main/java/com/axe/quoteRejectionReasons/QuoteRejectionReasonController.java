package com.axe.quoteRejectionReasons;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quote-rejection-reasons")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class QuoteRejectionReasonController {

    private final QuoteRejectionReasonService quoteRejectionReasonService;

    public QuoteRejectionReasonController(QuoteRejectionReasonService quoteRejectionReasonService) {
        this.quoteRejectionReasonService = quoteRejectionReasonService;
    }

    @GetMapping("all-quote-rejection-reasons")
    public List<QuoteRejectionReason> getAllQuoteRejectionReasons(){
        return quoteRejectionReasonService.getAllQuoteRejectionReasons();
    }

    @PostMapping("save-quote-rejection-reason")
    public QuoteRejectionReason saveNewQuoteRejectionReason(@RequestBody QuoteRejectionReason quoteRejectionReason){
        return quoteRejectionReasonService.saveNewQuoteRejectionReason(quoteRejectionReason);
    }
}
