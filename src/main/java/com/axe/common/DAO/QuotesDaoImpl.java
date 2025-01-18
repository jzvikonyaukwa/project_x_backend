package com.axe.common.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.axe.clients.Client;
import com.axe.common.enums.QuoteStatus;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.product.Product;
import com.axe.projects.Project;
import com.axe.quotePrice.QuotePrice;
import com.axe.quoteRejectionReasons.QuoteRejectionReason;
import com.axe.quotes.Quote;
import com.axe.quotes.quotesDTO.QuotePostDTO;
import com.axe.quotes.quotesDTO.RejectionDetailsDTO;
import com.axe.utilities.ClientVisibleException;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;

@Lazy
@Repository
public class QuotesDaoImpl implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(QuotesDaoImpl.class);

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public QuotesDaoImpl(final EntityManager entityManager$, final TransactionTemplate transactionTemplate$) {
        entityManager = entityManager$;
        transactionTemplate = transactionTemplate$;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public @Nonnull Long countQuotesByStatusInRange(
            @Nonnull final LocalDate startDate, @Nonnull final LocalDate endDate, @Nonnull final QuoteStatus status)
            throws NestedRuntimeException {
        logger.debug("Counting quotes with status '{}' between {} and {}", status, startDate, endDate);
        try {

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            ParameterExpression<LocalDate> startDateParameter = cb.parameter(LocalDate.class, "startDate");
            ParameterExpression<LocalDate> endDateParameter = cb.parameter(LocalDate.class, "endDate");
            ParameterExpression<QuoteStatus> statusParameter = cb.parameter(QuoteStatus.class, "status");

            CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
            Root<Quote> root = criteriaQuery.from(Quote.class);

            Specification<Quote> filterConditions = Specification.where(null);

            filterConditions = filterConditions
                    .and((r, q, c) -> c.equal(r.<QuoteStatus>get("status"), statusParameter));

            if (startDate.equals(endDate)) {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.equal(r.<LocalDate>get("dateIssued"), startDateParameter));
            } else {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.between(r.<LocalDate>get("dateIssued"), startDateParameter,
                                endDateParameter));
            }

            Predicate conditions = filterConditions.toPredicate(root, criteriaQuery, cb);

            criteriaQuery.select(cb.count(root)).where(conditions);

            TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery) //
                    .setParameter(startDateParameter, startDate) //
                    .setParameter(statusParameter, status) //
            ;

            if (!startDate.equals(endDate)) {
                typedQuery.setParameter(endDateParameter, endDate);
            }

            return typedQuery.getSingleResult();
        } catch (final DataAccessException e) {
            logger.error("Data access error while counting quotes by status in range: {}",
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while counting quotes by status in range",
                    e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Error while counting quotes by status in range: {}", e.getLocalizedMessage());
            throw new RuntimeException("Error while counting quotes by status in range", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public @Nullable BigDecimal totalConsumablesOnQuotesByStatusInRange(
            @Nonnull final LocalDate startDate, @Nonnull final LocalDate endDate, @Nonnull final QuoteStatus status)
            throws NestedRuntimeException {
        logger.debug("Calculating total consumables on quotes with status '{}' between {} and {}", status, startDate,
                endDate);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            ParameterExpression<LocalDate> startDateParameter = cb.parameter(LocalDate.class, "startDate");
            ParameterExpression<LocalDate> endDateParameter = cb.parameter(LocalDate.class, "endDate");
            ParameterExpression<QuoteStatus> statusParameter = cb.parameter(QuoteStatus.class, "status");

            CriteriaQuery<BigDecimal> criteriaQuery = cb.createQuery(BigDecimal.class);
            Root<Quote> root = criteriaQuery.from(Quote.class);

            Specification<Quote> filterConditions = Specification.where(null);

            filterConditions = filterConditions
                    .and((r, q, c) -> c.equal(r.<QuoteStatus>get("status"), statusParameter));

            if (startDate.equals(endDate)) {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.equal(r.<LocalDate>get("dateIssued"), startDateParameter));
            } else {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.between(r.<LocalDate>get("dateIssued"), startDateParameter,
                                endDateParameter));
            }

            Predicate conditions = filterConditions.toPredicate(root, criteriaQuery, cb);

            Join<Quote, ConsumableOnQuote> coqJoin = root.join("consumablesOnQuote");

            criteriaQuery.select(
                    cb.sum(
                            cb.prod(
                                    coqJoin.<BigDecimal>get("qty"),
                                    coqJoin.<BigDecimal>get("sellPrice"))))
                    .where(conditions);

            TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(criteriaQuery) //
                    .setParameter(startDateParameter, startDate) //
                    .setParameter(statusParameter, status) //
            ;

            if (!startDate.equals(endDate)) {
                typedQuery.setParameter(endDateParameter, endDate);
            }

            return typedQuery.getSingleResult();
        } catch (final DataAccessException e) {
            logger.error("Data access error while calculating total consumables on quotes by status in range: {}",
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException(
                    "Data access error while calculating total consumables on quotes by status in range",
                    e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Error while calculating total consumables on quotes by status in range: {}",
                    e.getLocalizedMessage());
            throw new RuntimeException("Error while calculating total consumables on quotes by status in range", e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public @Nullable BigDecimal totalManufacturedProductsOnQuotesByStatusInRange(
            @Nonnull final LocalDate startDate, @Nonnull final LocalDate endDate, @Nonnull final QuoteStatus status)
            throws NestedRuntimeException {
        logger.debug("Calculating total manufactured products on quotes with status '{}' between {} and {}", status,
                startDate, endDate);
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            ParameterExpression<LocalDate> startDateParameter = cb.parameter(LocalDate.class, "startDate");
            ParameterExpression<LocalDate> endDateParameter = cb.parameter(LocalDate.class, "endDate");
            ParameterExpression<QuoteStatus> statusParameter = cb.parameter(QuoteStatus.class, "status");

            CriteriaQuery<BigDecimal> criteriaQuery = cb.createQuery(BigDecimal.class);
            Root<Product> root = criteriaQuery.from(Product.class);

            Specification<Product> filterConditions = Specification.where(null);

            filterConditions = filterConditions
                    .and((r, q, c) -> c.equal(r.<QuoteStatus>get("quote").get("status"), statusParameter));

            if (startDate.equals(endDate)) {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.equal(r.<LocalDate>get("quote").get("dateIssued"), startDateParameter));
            } else {
                filterConditions = filterConditions
                        .and((r, q, c) -> cb.between(r.<LocalDate>get("quote").get("dateIssued"), startDateParameter,
                                endDateParameter));
            }

            Predicate conditions = filterConditions.toPredicate(root, criteriaQuery, cb);

            Join<Product, Quote> quoteJoin = root.join("quote");

            criteriaQuery
                    .select(
                            cb.sum(
                                    cb.prod(
                                            root.get("totalLength"),
                                            root.get("sellPrice")
                                    )
                            )
                    )
                    .where(conditions);

            TypedQuery<BigDecimal> typedQuery = entityManager.createQuery(criteriaQuery) //
                    .setParameter(startDateParameter, startDate) //
                    .setParameter(statusParameter, status) //
            ;

            if (!startDate.equals(endDate)) {
                typedQuery.setParameter(endDateParameter, endDate);
            }

            return typedQuery.getSingleResult();
        } catch (final DataAccessException e) {
            logger.error(
                    "Data access error while calculating total manufactured products on quotes by status in range: {}",
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException(
                    "Data access error while calculating total manufactured products on quotes by status in range",
                    e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Error while calculating total manufactured products on quotes by status in range: {}",
                    e.getLocalizedMessage());
            throw new RuntimeException(
                    "Error while calculating total manufactured products on quotes by status in range", e);
        }
    }

    // this should be the ***ONLY*** method that creates a new quote
    @Transactional(propagation = Propagation.MANDATORY)
    public Quote createQuote(final QuotePostDTO quoteData) {
        logger.debug("Creating new quotation: {}", quoteData);
        try {
            Project project = entityManager.getReference(Project.class, quoteData.getProjectId());
            Client client = project.getClient();
            logger.debug("Found project: {} for client: {}", project.getName(), client.getName());

            QuotePrice quotePrice = entityManager.getReference(QuotePrice.class, quoteData.getQuotePrice().getId());

            Quote quote = new Quote();
            quote.setStatus(QuoteStatus.draft);
            quote.setPaid(false);

            quote.setProject(project);
            project.getQuotes().add(quote);

            quote.setDateIssued(quoteData.getDateIssued());
            quote.setDateLastModified(quoteData.getDateIssued());

            quote.setQuotePrice(quotePrice);
            quotePrice.getQuotes().add(quote);

            entityManager.persist(quote);

            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while creating new quote: {}",
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while creating new quote: ", e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Error while creating new quote: {}", e.getLocalizedMessage());
            throw new RuntimeException("Error while creating new quote: ", e);
        }
    }

    // this should be the ***ONLY*** method that gets a quote from the database
    @Transactional(propagation = Propagation.MANDATORY)
    public Quote fetchQuoteWithID(final Long quoteID) {
        logger.debug("Fetching quote with ID: {}", quoteID);
        try {
            Quote quote = entityManager.find(Quote.class, quoteID);
            if (quote == null) {
                throw new ClientVisibleException("Quote with ID: {} not found".formatted(quoteID));
            }
            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while fetching quote with ID {}: {}", quoteID,
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while fetching quote: ", e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Unknown error while fetching quote with ID {}: {}", quoteID, e.getLocalizedMessage());
            throw e;
        }
    }

    // this should be the ***ONLY*** method that saves a quote from the database
    @Transactional(propagation = Propagation.MANDATORY)
    public Quote saveQuote(final Quote quote) {
        logger.debug("Saving quote with ID: {}", quote.getId());

        entityManager.persist(quote);

        entityManager.flush();

        return quote;
    }

    // Summary Table of State Transitions:
    // Current State Possible Next States
    // draft pending_approval, rejected
    // pending_approval approved, rejected
    // approved accepted, rejected
    // accepted (final state)
    // rejected (final state)

    @Transactional(propagation = Propagation.MANDATORY)
    public Quote rejectQuote(Quote quote, RejectionDetailsDTO rejectionDetails) {
        logger.debug("Marking quote with ID: {} as rejected", quote.getId());
        try {
            logger.debug("Quote ID: {} has status: {}", quote.getId(), quote.getStatus());
            // check if the quote is not already in rejected status
            if (QuoteStatus.rejected == quote.getStatus()) {
                logger.error("Quote with ID: {} was already rejected", quote.getId());
                throw new ClientVisibleException("Quote '{}' was already rejected".formatted(quote.getId()));
            }

            final Long rejectionReasonID = rejectionDetails.getRejectionReason().getId();
            QuoteRejectionReason rejectionReason = entityManager.getReference(QuoteRejectionReason.class,
                    rejectionReasonID);
            if (rejectionReason == null) {
                throw new ClientVisibleException(
                        "Quote rejection reason with ID: {} not found".formatted(rejectionReasonID));
            }
            quote.setRejectedReason(rejectionReason);

            quote.setStatus(QuoteStatus.rejected);
            quote.setDateAccepted(null);
            quote.setDateRejected(rejectionDetails.getDate());
            quote.setDateLastModified(LocalDate.now());

            entityManager.flush();

            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while marking quote with ID {} as rejected: {}", quote.getId(),
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error while marking quote as rejected: ", e.getMostSpecificCause());
        } catch (final Exception e) {
            logger.error("Error while marking quote with ID {} as rejected: {}", quote.getId(),
                    e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Quote markAsAccepted(Quote quote) {
        logger.debug("Marking quote with ID: {} as accepted", quote.getId());
        try {
            logger.debug("Quote ID: {} has status: {}", quote.getId(), quote.getStatus());
            // check if the quote is in approved status
            if (QuoteStatus.accepted == quote.getStatus()) {
                logger.error("Quote with ID: {} was already accepted", quote.getId());
                throw new ClientVisibleException("Quote '{}' was already accepted".formatted(quote.getId()));
            }

            if (QuoteStatus.approved != quote.getStatus()) {
                logger.error("Quote with ID: {} is not approved", quote.getId());
                throw new ClientVisibleException("Quote '{}' is not approved".formatted(quote.getId()));
            }

            LocalDate currentTime = LocalDate.now();
            quote.setStatus(QuoteStatus.accepted);
            quote.setDateAccepted(currentTime);
            quote.setDateRejected(null);
            quote.setDateLastModified(currentTime);

            entityManager.flush();

            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while marking quote with ID {} as accepted: {}", quote.getId(),
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Data access error while marking quote as accepted",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("Error while marking quote with ID {} as accepted: {}", quote.getId(),
                    e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Quote markAsPendingApproval(final Quote quote) {
        logger.debug("Marking quote with ID: {} as pending approval", quote.getId());
        try {
            logger.debug("Quote ID: {} has status: {}", quote.getId(), quote.getStatus());
            // check if the quote is in draft status
            if (QuoteStatus.pending_approval == quote.getStatus()) {
                logger.error("Quote with ID: {} is already pending approval", quote.getId());
                throw new ClientVisibleException("Quote '{}' is already pending approval".formatted(quote.getId()));
            }

            if (QuoteStatus.draft != quote.getStatus()) {
                logger.error("Quote with ID: {} is not in draft status", quote.getId());
                throw new ClientVisibleException("Quote '{}' is not in draft status".formatted(quote.getId()));
            }

            quote.setStatus(QuoteStatus.pending_approval);
            quote.setDateAccepted(null);
            quote.setDateRejected(null);
            quote.setDateLastModified(LocalDate.now());

            entityManager.flush();

            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while marking quote with ID {} as pending approval: {}", quote.getId(),
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new NestedRuntimeException("Data access error while marking quote as pending approval",
                    e.getMostSpecificCause()) {
            };
        } catch (final Exception e) {
            logger.error("Error while marking quote with ID {} as pending approval: {}", quote.getId(),
                    e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Quote markAsApproved(Quote quote) {
        logger.debug("Marking quote with ID: {} as approved", quote.getId());
        try {
            logger.debug("Quote ID: {} has status: {}", quote.getId(), quote.getStatus());

            // check if the quote is already approved
            if (quote.getStatus() == QuoteStatus.approved) {
                logger.error("Quote with ID: {} was already approved", quote.getId());
                throw new ClientVisibleException("Quote '{}' was already approved".formatted(quote.getId()));
            }

            // Early return if quote is not in pending approval status
            if (quote.getStatus() != QuoteStatus.pending_approval) {
                logger.error("Quote with ID: {} is not pending approval", quote.getId());
                throw new ClientVisibleException("Quote '{}' is not pending approval".formatted(quote.getId()));
            }

            LocalDate currentTime = LocalDate.now();
            quote.setStatus(QuoteStatus.approved);
            quote.setDateAccepted(currentTime);
            quote.setDateRejected(null);
            quote.setDateLastModified(currentTime);

            entityManager.flush();

            return quote;
        } catch (final DataAccessException e) {
            logger.error("Data access error while marking quote with ID {} as approved: {}", quote.getId(),
                    e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException(
                    "Data access error while marking quote as approved: " + e.getLocalizedMessage());
        } catch (final Exception e) {
            logger.error("Error while marking quote with ID {} as approved: {}", quote.getId(),
                    e.getLocalizedMessage());
            throw e;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug("Initializing QuotesDaoImpl");
    }

    @Override
    public void destroy() throws Exception {
        logger.debug("Destroying QuotesDaoImpl");
    }
}
