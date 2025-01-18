package com.axe.quotes.services.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.NumberColumnFilter;
import com.axe.common.agGrid.filter.SetColumnFilter;
import com.axe.common.agGrid.filter.FilterType;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.quotes.Quote;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.axe.common.enums.QuoteStatus;
import static com.axe.quotes.QuotesRepository.Specs.withClientId;

abstract class AbstractQuoteRowProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractQuoteRowProvider.class);

    private Function<String,String> escapeSpecialCharactersInString() {
        return inputString -> inputString.trim()
                     .replace("!", "!!")
                     .replace("%", "!%")
                     .replace("_", "!_");
    }

    private Function<String,String> startsWith() {
        return inputString -> inputString.trim()
                     .replace("!", "!!")
                     .replace("%", "!%")
                     .replace("_", "!_");
    }

    // private Function<String,String> escapeSpecialCharactersInString() {
    //     return inputString -> inputString.trim()
    //                  .replace("!", "!!")
    //                  .replace("%", "!%")
    //                  .replace("_", "!_");
    // }

    protected Specification<Quote> makeQuoteIdCriteria(final TextColumnFilter quoteIdFilter) {
        // Quote ID filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (quoteIdFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by quoteIdFilter: {}", quoteIdFilter);

        String quoteId = quoteIdFilter.getFilter();
        logger.debug("Filtering by quoteId: {}", quoteId);

        if (quoteId.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedQuoteId =  this.escapeSpecialCharactersInString().apply(quoteId);
        logger.debug("Filtering by escaped client ID: {}", escapedQuoteId);

        if (FilterType.equals.getValue().equals(quoteIdFilter.getType())) {

            Specification<Quote> quoteIdPredicate = (root, query, cb) -> cb
                    .equal(root.get("id"), quoteId);

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.contains.getValue().contains(quoteIdFilter.getType())) {

            Specification<Quote> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), "%" + escapedQuoteId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(quoteIdFilter.getType())) {

            Specification<Quote> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), escapedQuoteId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<Quote> makeStatusCriteria(final SetColumnFilter statusFilter) {
        // Status filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (statusFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by statusCriteria: {}", statusFilter);

        final Set<QuoteStatus> statusValues = statusFilter.getValues().stream()
                .map(QuoteStatus::fromValue)
                .sorted()
                .collect(Collectors.toSet()); // Changed to use collect toSet() method

        logger.debug("Filtering by status: {}", statusValues);
        Specification<Quote> statusPredicate = Specification.where(null);
        if (statusValues.size() == 1) {
            statusPredicate = (root, query, cb) -> cb.equal(root.get("status"), statusValues.toArray()[0]);
            return filterConditions.and(statusPredicate);
        } else if (statusValues.size() != QuoteStatus.values().length) {

            statusPredicate = (root, query, cb) -> root.get("status").in(statusValues);
            return filterConditions.and(statusPredicate);
        }

        return filterConditions;
    }

    protected Specification<Quote> makeClientIdCriteria(final NumberColumnFilter clientIdFilter) {
        // Client ID filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (clientIdFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by clientIdFilter: {}", clientIdFilter);

        Long clientId = ((Integer) clientIdFilter.getFilter()).longValue();
        logger.debug("Filtering by clientId: {}", clientId);

        if (FilterType.equals.getValue().equals(clientIdFilter.getType())) {
            filterConditions = filterConditions.and(withClientId(clientId));
        }

        return filterConditions;
    }

    protected Specification<Quote> makeClientNameCriteria(final TextColumnFilter clientNameFilter) {
        // Client Name filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (clientNameFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by clientNameFilter: {}", clientNameFilter);

        String clientName = clientNameFilter.getFilter();
        logger.debug("Filtering by client name: {}", clientName);

        if (clientName.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedClientName = clientName.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped client name: {}", escapedClientName);

        if (FilterType.equals.getValue().equals(clientNameFilter.getType())) {

            Specification<Quote> clientNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("project").get("client").get("name"), clientName.trim());

            filterConditions = filterConditions.and(clientNamePredicate);
        } else if (FilterType.contains.getValue().equals(clientNameFilter.getType())) {

            Specification<Quote> clientNamePredicate = (root, query, cb) -> cb
                    .like(root.get("project").get("client").get("name"), "%" + escapedClientName + "%", '!');

            filterConditions = filterConditions.and(clientNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<Quote> makeProjectNameCriteria(final TextColumnFilter projectNameFilter) {
        // Project Name filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (projectNameFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by projecttNameFilter: {}", projectNameFilter);

        String projectName = projectNameFilter.getFilter();
        logger.debug("Filtering by project name: {}", projectName);

        if (projectName.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedProjectName = projectName.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped project name: {}", escapedProjectName);

        if (FilterType.equals.getValue().equals(projectNameFilter.getType())) {

            Specification<Quote> projectNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("project").get("name"), projectName.trim());

            filterConditions = filterConditions.and(projectNamePredicate);
        } else if (FilterType.contains.getValue().contains(projectNameFilter.getType())) {

            Specification<Quote> projectNamePredicate = (root, query, cb) -> cb
                    .like(root.get("project").get("name"), "%" + escapedProjectName + "%", '!');

            filterConditions = filterConditions.and(projectNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<Quote> makeDateIssuedCriteria(final DateColumnFilter dateIssuedFilter) {
        // Date Issued filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (dateIssuedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateIssuedCriteria: {}", dateIssuedFilter);

        LocalDateTime dateIssuedFrom = dateIssuedFilter.getDateFrom();
        LocalDateTime dateIssuedTo = dateIssuedFilter.getDateTo();

        if (dateIssuedFrom == null && dateIssuedTo == null) {
            logger.debug("Both dateIssuedFrom and dateIssuedTo are null");

            Specification<Quote> dateIssuedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateIssuedFilter.getType())) {
                case empty:
                case blank:
                    dateIssuedFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateIssued").as(LocalDate.class));
                    break;

                case notBlank:
                    dateIssuedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateIssued").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateIssuedFromFilterCondition); // Early return for clarity
        } else if (dateIssuedFrom != null && dateIssuedTo != null) {

            LocalDate dateIssuedFromLocalDate = dateIssuedFrom.toLocalDate();
            LocalDate dateIssuedToLocalDate = dateIssuedTo.toLocalDate();

            Specification<Quote> dateIssuedFromFilterCondition = Specification.where(null);
            if (dateIssuedFromLocalDate.compareTo(dateIssuedToLocalDate) == 0) {
                dateIssuedFromFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateIssued").as(LocalDate.class), dateIssuedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateIssuedFromLocalDate.isBefore(dateIssuedToLocalDate)
                        ? dateIssuedFromLocalDate
                        : dateIssuedToLocalDate;
                final LocalDate filterEndDate = dateIssuedFromLocalDate.isAfter(dateIssuedToLocalDate)
                        ? dateIssuedFromLocalDate
                        : dateIssuedToLocalDate;

                dateIssuedFromFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateIssued").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateIssuedFromFilterCondition);
        } else if (dateIssuedFrom != null) {
            Specification<Quote> dateIssuedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateIssuedFilter.getType())) {
                case equals:
                    dateIssuedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateIssued").as(LocalDate.class),
                                    dateIssuedFrom.toLocalDate());
                    break;
                case before:
                    dateIssuedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateIssued").as(LocalDate.class), dateIssuedFrom.toLocalDate());
                    break;
                case after:
                    dateIssuedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateIssued").as(LocalDate.class),
                                    dateIssuedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateIssuedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }

    protected Specification<Quote> makeDateAcceptedCriteria(final DateColumnFilter dateAcceptedFilter) {
        // Date Accepted filtering
        Specification<Quote> filterConditions = Specification.where(null);

        if (dateAcceptedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateAcceptedCriteria: {}", dateAcceptedFilter);

        LocalDateTime dateAcceptedFrom = dateAcceptedFilter.getDateFrom();
        LocalDateTime dateAcceptedTo = dateAcceptedFilter.getDateTo();

        if (dateAcceptedFrom == null && dateAcceptedTo == null) {
            logger.debug("Both dateAcceptedFrom and dateAcceptedTo are null", dateAcceptedFilter.getType());
            Specification<Quote> dateAcceptedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateAcceptedFilter.getType())) {
                case empty:
                case blank:
                    logger.debug(" IS NULL{}", dateAcceptedFilter.getType());
                    dateAcceptedFromFilterCondition = (root, query, cb) -> cb.isNull(root.get("dateAccepted"));

                    break;

                case notBlank:
                    logger.debug(" IS NOT NULL {}", dateAcceptedFilter.getType());
                    dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateAccepted").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateAcceptedFromFilterCondition);
        } else if (dateAcceptedFrom != null && dateAcceptedTo != null) {

            LocalDate dateAcceptedFromLocalDate = dateAcceptedFrom.toLocalDate();
            LocalDate dateAcceptedToLocalDate = dateAcceptedTo.toLocalDate();

            Specification<Quote> dateAcceptedFromFilterCondition = Specification.where(null);

            if (dateAcceptedFromLocalDate.compareTo(dateAcceptedToLocalDate) == 0) {
                dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateAccepted").as(LocalDate.class), dateAcceptedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateAcceptedFromLocalDate.isBefore(dateAcceptedToLocalDate)
                        ? dateAcceptedFromLocalDate
                        : dateAcceptedToLocalDate;
                final LocalDate filterEndDate = dateAcceptedFromLocalDate.isAfter(dateAcceptedToLocalDate)
                        ? dateAcceptedFromLocalDate
                        : dateAcceptedToLocalDate;

                dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateAccepted").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateAcceptedFromFilterCondition);
        } else if (dateAcceptedFrom != null) {
            Specification<Quote> dateAcceptedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateAcceptedFilter.getType())) {
                case equals:
                    dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateAccepted").as(LocalDate.class),
                                    dateAcceptedFrom.toLocalDate());
                    break;
                case before:
                    dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateAccepted").as(LocalDate.class), dateAcceptedFrom.toLocalDate());
                    break;
                case after:
                    dateAcceptedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateAccepted").as(LocalDate.class),
                                    dateAcceptedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateAcceptedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }
}
