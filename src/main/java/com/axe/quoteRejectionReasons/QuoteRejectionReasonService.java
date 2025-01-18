package com.axe.quoteRejectionReasons;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteRejectionReasonService {

    private final QuoteRejectionReasonRepository quoteRejectionReasonRepository;

    public QuoteRejectionReasonService(QuoteRejectionReasonRepository quoteRejectionReasonRepository) {
        this.quoteRejectionReasonRepository = quoteRejectionReasonRepository;
    }

    public List<QuoteRejectionReason> getAllQuoteRejectionReasons() {
        return quoteRejectionReasonRepository.findAll();
    }

    public QuoteRejectionReason saveNewQuoteRejectionReason(QuoteRejectionReason quoteRejectionReason) {
        return quoteRejectionReasonRepository.save(quoteRejectionReason);
    }
}
