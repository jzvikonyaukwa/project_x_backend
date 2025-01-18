package com.axe.invoices.invoiceDTOs.services.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.SetColumnFilter;
import com.axe.common.agGrid.filter.FilterType;
import com.axe.common.agGrid.filter.TextColumnFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.axe.invoices.Invoice;

abstract class AbstractInvoiceRowProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractInvoiceRowProvider.class);

    protected Specification<Invoice> makeIdCriteria(final TextColumnFilter idFilter) {
        // ID filtering
        Specification<Invoice> filterConditions = Specification.where(null);

        if (idFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by idFilter: {}", idFilter);

        String objectID = idFilter.getFilter();
        logger.debug("Filtering by id: {}", objectID);

        if (objectID.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedObjectID = objectID.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped client ID: {}", escapedObjectID);

        if (FilterType.equals.getValue().equals(idFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .equal(root.get("id"), escapedObjectID);

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.contains.getValue().contains(idFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), "%" + escapedObjectID + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(idFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), escapedObjectID + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<Invoice> makeQuoteIdCriteria(final TextColumnFilter quoteIdFilter) {
        // Quote ID filtering
        Specification<Invoice> filterConditions = Specification.where(null);

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
        final String escapedQuoteId = quoteId.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped quote ID: {}", escapedQuoteId);

        if (FilterType.equals.getValue().equals(quoteIdFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("saleOrder").get("quote").get("id").as(String.class), escapedQuoteId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.contains.getValue().contains(quoteIdFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("saleOrder").get("quote").get("id").as(String.class), "%" + escapedQuoteId + "%",
                            '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(quoteIdFilter.getType())) {

            Specification<Invoice> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("saleOrder").get("quote").get("id").as(String.class), escapedQuoteId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<Invoice> makeIsPaidCriteria(final SetColumnFilter paidStatusFilter) {
        // Paid Status filtering
        Specification<Invoice> filterConditions = Specification.where(null);

        if (paidStatusFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by paidStatusFilter: {}", paidStatusFilter);

        final Set<String> distinctPaymentStatuses = paidStatusFilter.getValues().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        logger.debug("Filtering by status: {}", distinctPaymentStatuses);

        if (distinctPaymentStatuses.contains("yes") && distinctPaymentStatuses.contains("no")) {
            return filterConditions; // No need to filter if both "Yes" and "No" are present
        }

        Specification<Invoice> paidStatusPredicate = (root, query, cb) -> {
            if (distinctPaymentStatuses.contains("yes")) {
                return cb.isTrue(root.get("paid").as(Boolean.class));
            } else if (distinctPaymentStatuses.contains("no")) {
                return cb.isFalse(root.get("paid").as(Boolean.class));
            }
            return null;
        };

        return filterConditions.and(paidStatusPredicate);
    }

    protected Specification<Invoice> makeClientNameCriteria(final TextColumnFilter clientNameFilter) {
        // Client Name filtering
        Specification<Invoice> filterConditions = Specification.where(null);

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

            Specification<Invoice> clientNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("saleOrder")
                            .get("quote")
                            .get("project")
                            .get("client")
                            .get("name"), clientName.trim());

            filterConditions = filterConditions.and(clientNamePredicate);
        } else if (FilterType.contains.getValue().equals(clientNameFilter.getType())) {

            Specification<Invoice> clientNamePredicate = (root, query, cb) -> 
                cb                      .like(
                                root.get("saleOrder")
                                        .get("quote")
                                        .get("project")
                                        .get("client")
                                        .get("name"),
                                "%" + escapedClientName + "%", '!');

            filterConditions = filterConditions.and(clientNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<Invoice> makeProjectNameCriteria(final TextColumnFilter projectNameFilter) {
        // Project Name filtering
        Specification<Invoice> filterConditions = Specification.where(null);

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

            Specification<Invoice> projectNamePredicate = (root, query, cb) -> cb.equal(
                    cb.lower(root.join("project").get("name")), cb.lower(cb.literal(
                            projectName.trim())));

            filterConditions = filterConditions.and(projectNamePredicate);
        } else if (FilterType.contains.getValue().contains(projectNameFilter.getType())) {

            Specification<Invoice> projectNamePredicate = (root, query, cb) -> cb
                    .like(root.get("project").get("name"), "%" + escapedProjectName + "%", '!');

            filterConditions = filterConditions.and(projectNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<Invoice> makeDateIssuedCriteria(final DateColumnFilter dateInvoicedFilter) {
        // Date Invoiced filtering
        Specification<Invoice> filterConditions = Specification.where(null);

        if (dateInvoicedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateInvoicedCriteria: {}", dateInvoicedFilter);

        LocalDateTime dateInvoicedFrom = dateInvoicedFilter.getDateFrom();
        LocalDateTime dateInvoicedTo = dateInvoicedFilter.getDateTo();

        if (dateInvoicedFrom == null && dateInvoicedTo == null) {
            logger.debug("Both dateInvoicedFrom and dateInvoicedTo are null");

            Specification<Invoice> dateInvoicedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateInvoicedFilter.getType())) {
                case empty:
                case blank:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateInvoiced").as(LocalDate.class));
                    break;

                case notBlank:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateInvoiced").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateInvoicedFromFilterCondition); // Early return for clarity
        } else if (dateInvoicedFrom != null && dateInvoicedTo != null) {

            LocalDate dateInvoicedFromLocalDate = dateInvoicedFrom.toLocalDate();
            LocalDate dateInvoicedToLocalDate = dateInvoicedTo.toLocalDate();

            Specification<Invoice> dateInvoicedFilterCondition = Specification.where(null);
            if (dateInvoicedFromLocalDate.compareTo(dateInvoicedToLocalDate) == 0) {
                dateInvoicedFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateInvoiced").as(LocalDate.class), dateInvoicedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateInvoicedFrom.toLocalDate().isBefore(dateInvoicedTo.toLocalDate())
                        ? dateInvoicedFrom.toLocalDate()
                        : dateInvoicedTo.toLocalDate();
                final LocalDate filterEndDate = dateInvoicedFrom.toLocalDate().isAfter(dateInvoicedTo.toLocalDate())
                        ? dateInvoicedFrom.toLocalDate()
                        : dateInvoicedTo.toLocalDate();

                dateInvoicedFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateInvoiced").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateInvoicedFilterCondition);
        } else if (dateInvoicedFrom != null) {
            Specification<Invoice> dateInvoicedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateInvoicedFilter.getType())) {
                case equals:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateInvoiced").as(LocalDate.class),
                                    dateInvoicedFrom.toLocalDate());
                    break;
                case before:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateInvoiced").as(LocalDate.class), dateInvoicedFrom.toLocalDate());
                    break;
                case after:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateInvoiced").as(LocalDate.class),
                                    dateInvoicedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateInvoicedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }
}
