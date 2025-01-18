package com.axe.quotes.services;

import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.common.DAO.QuotesDaoImpl;
import com.axe.consumables.Consumable;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.consumablesOnQuote.ConsumablesOnQuoteService;
import com.axe.invoices.Invoice;
import com.axe.invoices.InvoiceRepository;
import com.axe.product.Product;
import com.axe.quotePrice.QuotePrice;
import com.axe.quotePrice.QuotePriceRepository;
import com.axe.quotes.Quote;
import com.axe.quotes.QuotesRepository;
import com.axe.quotes.quotesDTO.QuoteDetailsDTO;
import com.axe.quotes.quotesDTO.QuotePostDTO;
import com.axe.quotes.services.providers.QuoteDetailsRowProvider;
import com.axe.quotes.services.providers.QuoteRowProvider;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.RoundingMode;

@Service
public class QuotesService {
    private static final Logger logger = LoggerFactory.getLogger(QuotesService.class);

    private final QuotePriceRepository quotePriceRepository;
    private final QuotesRepository quotesRepository;
    private final ConsumablesOnQuoteService consumablesOnQuoteService;
    private final QuoteRowProvider quoteRowProvider;
    private final QuoteDetailsRowProvider quoteDetailsRowProvider;
    private final QuotesDaoImpl quotesDao;
    private final InvoiceRepository invoiceRepository;

    public QuotesService(QuotesRepository quotesRepository
            , QuotePriceRepository quotePriceRepository
            , InvoiceRepository invoiceRepository
            , ConsumablesOnQuoteService consumablesOnQuoteService
            , QuoteRowProvider quoteRowProvider
            , QuoteDetailsRowProvider quoteDetailsRowProvider
            , @Lazy QuotesDaoImpl quotesDao) {
        this.quotesRepository = quotesRepository;
        this.quotePriceRepository = quotePriceRepository;
        this.invoiceRepository = invoiceRepository;
        this.consumablesOnQuoteService = consumablesOnQuoteService;
        this.quoteRowProvider = quoteRowProvider;
        this.quoteDetailsRowProvider = quoteDetailsRowProvider;
        this.quotesDao = quotesDao;
    }

    public List<QuoteDetailsDTO> getAllQuotes() {
        return quotesRepository.getAllQuotesDetails();
    }

    @Transactional(readOnly = true)
    public ServerSideGetRowsResponse<List<QuoteDetailsDTO>> fetchQuoteDetailsDTOGridRows(ServerSideGetRowsRequest request) {
        logger.debug("Getting quotes details for request: {}", request);
        try {
            return quoteDetailsRowProvider.getRows(request);
        } catch (Exception e) {
            logger.error("Error in getting list quotes details: ", e);
            throw new RuntimeException("Error in getting list quotes details: ", e);
        }
    }

    @Transactional
    public Quote addQuote(final QuotePostDTO quoteData) {
        logger.debug("quotePostDTO: {}",  quoteData);
        try{
            return quotesDao.createQuote(quoteData);
        } catch (Exception e) {
            logger.error("Error in creating new quote: ", e);
            throw new RuntimeException("Error in creating new quote: ", e);
        }
    }

    public Quote saveQuote(Quote quote) {
        return quotesRepository.save(quote);
    }

    public Quote getQuote(Long id) {
        return quotesRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Quote updateQuoteSections(final Quote incomingQuote) {
        logger.info("In updateQuote() method. Quote: {}", incomingQuote);
        try {

            final Quote originalQuote = quotesRepository.findById(incomingQuote.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Quote not found"));

            logger.info("Original Quote fetched from DB: {}", originalQuote.getId());

            // Preserve the ID
            incomingQuote.setId(originalQuote.getId());
            // if (incomingQuote.getProject() == null) {
            // incomingQuote.setProject(originalQuote.getProject());
            // } else {
            // incomingQuote.setProject(getProject(incomingQuote.getProject().getId()));
            // }

            for (Product product : incomingQuote.getProducts()) {
//                if(product.getProductPrice() == null){
//                    product.setProductPrice(null);
//                } else {
//                    product.getProductPrice();
//                }
                product.setQuote(incomingQuote);
            }
            
            // check if quotePrice changed
            if (incomingQuote.getQuotePrice() != null) {
                QuotePrice updatedQuotePrice = incomingQuote.getQuotePrice();
                QuotePrice originalQuotePrice = originalQuote.getQuotePrice();
                logger.debug("originalQuotePrice is not null");
                if (updatedQuotePrice.compareTo(originalQuotePrice) != 0) {
                    logger.debug("updatedQuotePrice is different from originalQuotePrice");

                    // lookup the quote price
                    QuotePrice storedQuotePrice = quotePriceRepository.findById(updatedQuotePrice.getId())
                            .orElseThrow();

                    // update all consumables with the new quote price if they are not custom
                    incomingQuote.getConsumablesOnQuote().stream()
                            .filter(lineItem -> lineItem.getHasCustomMarkup() ==  false)
                            .forEach(lineItem -> lineItem.setSellPrice(
                                    storedQuotePrice.getMarkUp().multiply(lineItem.getCostPrice())
                                            .setScale(2, RoundingMode.HALF_UP)
                            ));
                } else {
                    logger.debug("updatedQuotePrice is the same as originalQuotePrice");
                }
            }
 
            handleTheConsumableUpdates(incomingQuote, originalQuote);

            // cutting lists and outsourced products are will never be edited through this function
            incomingQuote.setProducts(originalQuote.getProducts());
            incomingQuote.setQuotePrice(incomingQuote.getQuotePrice());

            return quotesRepository.save(incomingQuote);
        } catch (DataAccessException e) {
            logger.error("Database error updating quote: {}", e.getLocalizedMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unknown error updating quote: {}", e.getLocalizedMessage());
            throw e;
        } 
    }

    private void handleTheConsumableUpdates(Quote editedQuote, Quote originalQuote) {
        Map<Long, ConsumableOnQuote> consumablesMap = new HashMap<>();

        for (ConsumableOnQuote consumableOnOriginalQuote : originalQuote.getConsumablesOnQuote()) {
            logger.info("consumableOnOriginalQuote.getId() : " + consumableOnOriginalQuote.getId());
            consumablesMap.put(consumableOnOriginalQuote.getId(), consumableOnOriginalQuote);
        }
        logger.info("consumablesMap: " + consumablesMap.size());

        for (ConsumableOnQuote lineItem : editedQuote.getConsumablesOnQuote()) {
            Consumable detachedConsumable = lineItem.getConsumable();
            Long consumableOnQuoteId = lineItem.getId();
            Long invoiceId = lineItem.getInvoiceId();
            if(invoiceId != null){
                Invoice invoice = invoiceRepository.getReferenceById(invoiceId);
                lineItem.setInvoice(invoice);
            }

            if (consumablesMap.containsKey(consumableOnQuoteId)) {
                logger.info("consumablesMap contains this consumable ID");
                lineItem.setQty(Math.abs(lineItem.getQty()));
                lineItem.setConsumable(detachedConsumable);
                lineItem.setQuote(editedQuote);
                lineItem.setCostPrice(lineItem.getCostPrice());
                lineItem.setSellPrice(lineItem.getSellPrice());

                consumablesMap.remove(consumableOnQuoteId);

            } else {
                logger.info("consumablesMap does not contain this consumable ID");
                lineItem.setQuote(editedQuote);
                lineItem.setConsumable(detachedConsumable);
                // consumableOnQuote.setCostPrice(consumableOnQuote.getCostPrice());
            }
        }

        for (ConsumableOnQuote consumableOnOriginalQuote : consumablesMap.values()) {
            consumableOnOriginalQuote.setInvoice(null);
            editedQuote.getConsumablesOnQuote().remove(consumableOnOriginalQuote);
            consumablesOnQuoteService.deleteConsumableOnQuote(consumableOnOriginalQuote.getId());
        }
    }

    @Transactional(readOnly = true)
    public ServerSideGetRowsResponse<List<Quote>> fetchQuotesForAgGridRequest(
            final ServerSideGetRowsRequest request) {
        logger.debug("Getting all quotes for request: {}", request);
        try {
            return quoteRowProvider.getRows(request);
        } catch (Exception e) {
            logger.error("Error in getting all quotes: ", e);
            throw new RuntimeException("Error in getting all quotes: ", e);
        }
    }
}
