package com.axe.quotes.services;

import com.axe.common.DAO.QuotesDaoImpl;
import com.axe.common.enums.QuoteStatus;
import com.axe.common.enums.SaleOrderStatus;
import com.axe.notifications.services.NotificationService;
import com.axe.quotes.Quote;
import com.axe.quotes.QuotesRepository;
import com.axe.quotes.quotesDTO.RejectionDetailsDTO;
import com.axe.saleOrder.SaleOrderService;
import com.axe.users.services.UsersService;
import com.axe.utilities.ClientVisibleException;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class QuoteStatusChangeService {
    private final static Logger logger = LoggerFactory.getLogger(QuoteStatusChangeService.class);

    private final NotificationService notificationService;
    private final QuotesDaoImpl quotesDao;
    private final QuotesRepository quotesRepository;
    private final SaleOrderService saleOrderService;

    public QuoteStatusChangeService(QuotesRepository quotesRepository, SaleOrderService saleOrderService
        , @Lazy QuotesDaoImpl quotesDao, UsersService usersService, NotificationService notificationService) {
        this.quotesRepository = quotesRepository;
        this.saleOrderService = saleOrderService;
        this.quotesDao = quotesDao;
        this.notificationService = notificationService;
    }

    @Transactional
    public Quote rejectQuote(String userEmail, Long quoteID, RejectionDetailsDTO rejectionDetails) {
        logger.debug("{} is rejecting quote with ID: {} - reason {}", userEmail, quoteID, rejectionDetails);
        try {
            // check if user has permission to reject quote
            Quote quote = quotesDao.fetchQuoteWithID(quoteID);

            // if we had accepted the quote, we need to cancel the sale order
            if (QuoteStatus.accepted.compareTo(quote.getStatus()) == 0) {
                if (quote.getSaleOrder() != null) {
                    quote.getSaleOrder().setStatus(SaleOrderStatus.cancelled);
                }
            }

            Quote updatedQuote = quotesDao.rejectQuote(quote, rejectionDetails);

            return quotesRepository.save(updatedQuote);
        } catch (DataAccessException e) {
            logger.error("Data access error while rejecting quote with ID {}: {}", quoteID,
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while rejecting quote: {}", e.getMostSpecificCause());
        } catch (ClientVisibleException e) {
            logger.error("Client visible error while rejecting quote with ID: {}", quoteID);
            throw e;
        } catch (Exception e) {
            logger.error("Unknown Error while rejecting quote with ID: {}", quoteID);
            throw new RuntimeException("Unknown Error while rejecting quote", e);
        }
    }

    @Transactional
    public Quote acceptQuote(String userEmail, Long quoteID) {
        logger.debug("{} is accepting quote with ID: {}", userEmail, quoteID);
        try {
            // check if user has permission to accept quote
            Quote quote = quotesDao.fetchQuoteWithID(quoteID);

            Quote updatedQuote = quotesDao.markAsAccepted(quote);

            // create sale order for the quote
            saleOrderService.createOrder(updatedQuote);

            return quotesRepository.save(updatedQuote);
        } catch (DataAccessException e) {
            logger.error("Data access error while accepting quote with ID: {}", quoteID);
            throw new RuntimeException("Data access error while accepting quote");
        } catch (ClientVisibleException e) {
            logger.error("Client visible error while accepting quote with ID: {}", quoteID);
            throw e;
        } catch (Exception e) {
            logger.error("Unknown Error while accepting quote with ID: {}", quoteID);
            throw new RuntimeException("Unknown Error while accepting quote");
        }
    }

    @Transactional
    public Quote approveQuote(String userEmail, Long quoteID) {
        logger.debug("{} is approving quote with ID: {}", userEmail, quoteID);
        try {
            // check if the user has the permission to approve the quote
            Quote quote = quotesDao.fetchQuoteWithID(quoteID);

            Quote updatedQuote = quotesDao.markAsApproved(quote);

            return quotesRepository.save(updatedQuote);
        } catch (DataAccessException e) {
            logger.error("Data access error while approving quote with ID: {}", quoteID);
            throw new RuntimeException("Data access error while approving quote");
        } catch (ClientVisibleException e) {
            logger.error("Client visible error while approving quote with ID: {}", quoteID);
            throw e;
        } catch (Exception e) {
            logger.error("Unknown Error while approving quote with ID: {}", quoteID);
            throw new RuntimeException("Unknown Error while approving quote");
        }
    }

    @Transactional
    public Quote requestQuoteApproval(String userEmail, Long quoteID) {
        logger.debug("{} is submitting quote with ID: {} for approval", userEmail, quoteID);
        try {
            Quote quote = quotesDao.fetchQuoteWithID(quoteID);

            Quote updatedQuote = quotesDao.markAsPendingApproval(quote);

            // send notification to all active super users
            String titleText = "Quote submitted for approval";
            String descriptionText = "Quote with ID: " + quoteID + " has been submitted for approval by " + userEmail;
            String linkUrl = "/quotes/" + quoteID;
            notificationService.sendNotificationToActiveSuperUsers(titleText, descriptionText, linkUrl,
                    "heroicons_mini:star");

            return quotesRepository.save(updatedQuote);
        } catch (DataAccessException e) {
            logger.error("Data access error while submitting quote with ID: {} for approval", quoteID);
            throw new RuntimeException("Data access error while submitting quote for approval");
        } catch (ClientVisibleException e) {
            logger.error("Client visible error while submitting quote with ID: {}", quoteID);
            throw e;
        } catch (Exception e) {
            logger.error("Unknown Error while submitting quote with ID: {} for approval", quoteID);
            throw new RuntimeException("Unknown Error while submitting quote for approval");
        }
    }
}
