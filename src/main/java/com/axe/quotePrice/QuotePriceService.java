package com.axe.quotePrice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuotePriceService {
    private final static Logger logger = LoggerFactory.getLogger(QuotePriceService.class);

    private final QuotePriceRepository quotePriceRepository;
    public QuotePriceService(QuotePriceRepository quotePriceRepository) {
        this.quotePriceRepository = quotePriceRepository;
    }

    public QuotePrice saveQuotePrice(QuotePrice quotePrice) {
        return quotePriceRepository.save(quotePrice);
    }

    @Transactional(readOnly = true)
    public QuotePrice getQuotePriceById(Long id) {
        return quotePriceRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<QuotePrice> getAllQuotePrices() {
        logger.debug("Getting all quote prices");


        return quotePriceRepository.findAll();
    }




}
