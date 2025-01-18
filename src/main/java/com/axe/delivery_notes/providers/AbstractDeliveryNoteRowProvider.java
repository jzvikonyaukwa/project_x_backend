package com.axe.delivery_notes.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.FilterType;
import com.axe.common.agGrid.filter.NumberColumnFilter;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.delivery_notes.DeliveryNote;
import java.time.LocalDate;
import java.time.LocalDateTime; 

// import static com.axe.invoices.InvoiceRepository.Specs.withClientId;

abstract class AbstractDeliveryNoteRowProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDeliveryNoteRowProvider.class);

    protected Specification<DeliveryNote> makeIdCriteria(final TextColumnFilter idFilter) {
        // ID filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

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
        final String escapedId = objectID.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped client ID: {}", escapedId);

        if (FilterType.equals.getValue().equals(idFilter.getType())) {

            Specification<DeliveryNote> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), escapedId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.contains.getValue().contains(idFilter.getType())) {

            Specification<DeliveryNote> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), "%" + escapedId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(idFilter.getType())) {

            Specification<DeliveryNote> quoteIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), escapedId + "%", '!');

            filterConditions = filterConditions.and(quoteIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeClientIdCriteria(final NumberColumnFilter clientIdFilter) {
        // Client ID filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

        if (clientIdFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by clientIdFilter: {}", clientIdFilter);

        Long clientId = ((Integer) clientIdFilter.getFilter()).longValue();
        logger.debug("Filtering by clientId: {}", clientId);

        if (FilterType.equals.getValue().equals(clientIdFilter.getType())) {

            Specification<DeliveryNote> clientIdPredicate = (root, query, cb) -> cb
                    .equal(root.get("project").get("client").get("id"), clientId);

            filterConditions = filterConditions.and(clientIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeDateCreatedCriteria(final DateColumnFilter dateCreatedFilter) {
        // Date Invoiced filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

        if (dateCreatedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateCreatedCriteria: {}", dateCreatedFilter);

        LocalDateTime dateCreatedFrom = dateCreatedFilter.getDateFrom();
        LocalDateTime dateCreatedTo = dateCreatedFilter.getDateTo();

        if (dateCreatedFrom == null && dateCreatedTo == null) {
            logger.debug("Both dateCreatedFrom and dateCreatedTo are null");

            Specification<DeliveryNote> dateCreatedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateCreatedFilter.getType())) {
                case empty:
                case blank:
                    dateCreatedFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateCreated").as(LocalDate.class));
                    break;

                case notBlank:
                    dateCreatedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateCreated").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateCreatedFromFilterCondition); // Early return for clarity
        } else if (dateCreatedFrom != null && dateCreatedTo != null) {

            LocalDate dateCreatedFromLocalDate = dateCreatedFrom.toLocalDate();
            LocalDate dateCreatedToLocalDate = dateCreatedTo.toLocalDate();

            Specification<DeliveryNote> dateCreatedFilterCondition = Specification.where(null);
            if (dateCreatedFromLocalDate.compareTo(dateCreatedToLocalDate) == 0) {
                dateCreatedFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateCreated").as(LocalDate.class), dateCreatedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateCreatedFrom.toLocalDate().isBefore(dateCreatedTo.toLocalDate())
                        ? dateCreatedFrom.toLocalDate()
                        : dateCreatedTo.toLocalDate();
                final LocalDate filterEndDate = dateCreatedFrom.toLocalDate().isAfter(dateCreatedTo.toLocalDate())
                        ? dateCreatedFrom.toLocalDate()
                        : dateCreatedTo.toLocalDate();

                dateCreatedFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateCreated").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateCreatedFilterCondition);
        } else if (dateCreatedFrom != null) {
            Specification<DeliveryNote> dateCreatedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateCreatedFilter.getType())) {
                case equals:
                    dateCreatedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateCreated").as(LocalDate.class),
                                    dateCreatedFrom.toLocalDate());
                    break;
                case before:
                    dateCreatedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateCreated").as(LocalDate.class), dateCreatedFrom.toLocalDate());
                    break;
                case after:
                    dateCreatedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateCreated").as(LocalDate.class),
                                    dateCreatedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateCreatedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeDateDeliveredCriteria(final DateColumnFilter dateDeliveredFilter) {
        // Date Delivered filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

        if (dateDeliveredFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateDeliveredFilter: {}", dateDeliveredFilter);

        LocalDateTime dateDeliveredFrom = dateDeliveredFilter.getDateFrom();
        LocalDateTime dateDeliveredTo = dateDeliveredFilter.getDateTo();

        if (dateDeliveredFrom == null && dateDeliveredTo == null) {
            logger.debug("Both dateDeliveredFrom and dateDeliveredTo are null");

            Specification<DeliveryNote> dateDeliveredFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateDeliveredFilter.getType())) {
                case empty:
                case blank:
                    dateDeliveredFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateDelivered").as(LocalDate.class));
                    break;

                case notBlank:
                    dateDeliveredFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateDelivered").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateDeliveredFromFilterCondition); // Early return for clarity
        } else if (dateDeliveredFrom != null && dateDeliveredTo != null) {

            LocalDate dateDeliveredFromLocalDate = dateDeliveredFrom.toLocalDate();
            LocalDate dateDeliveredToLocalDate = dateDeliveredTo.toLocalDate();

            Specification<DeliveryNote> dateDeliveredFilterCondition = Specification.where(null);
            if (dateDeliveredFromLocalDate.compareTo(dateDeliveredToLocalDate) == 0) {
                dateDeliveredFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateDelivered").as(LocalDate.class), dateDeliveredFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateDeliveredFrom.toLocalDate().isBefore(dateDeliveredTo.toLocalDate())
                        ? dateDeliveredFrom.toLocalDate()
                        : dateDeliveredTo.toLocalDate();
                final LocalDate filterEndDate = dateDeliveredFrom.toLocalDate().isAfter(dateDeliveredTo.toLocalDate())
                        ? dateDeliveredFrom.toLocalDate()
                        : dateDeliveredTo.toLocalDate();

                dateDeliveredFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateDelivered").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateDeliveredFilterCondition);
        } else if (dateDeliveredFrom != null) {
            Specification<DeliveryNote> dateDeliveredFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateDeliveredFilter.getType())) {
                case equals:
                    dateDeliveredFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateDelivered").as(LocalDate.class),
                                    dateDeliveredFrom.toLocalDate());
                    break;
                case before:
                    dateDeliveredFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateDelivered").as(LocalDate.class), dateDeliveredFrom.toLocalDate());
                    break;
                case after:
                    dateDeliveredFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateDelivered").as(LocalDate.class),
                                    dateDeliveredFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateDeliveredFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeDeliveryAddressCriteria(final TextColumnFilter deliveryAddressFilter) {
        // Delivery Address filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

        if (deliveryAddressFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by deliveryAddressFilter: {}", deliveryAddressFilter);

        String deliveryAddress = deliveryAddressFilter.getFilter();
        logger.debug("Filtering by delivery address: {}", deliveryAddress);

        if (deliveryAddress.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedDeliveryAddress = deliveryAddress.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped delivery address: {}", escapedDeliveryAddress);

        if (FilterType.equals.getValue().equals(deliveryAddressFilter.getType())) {

            Specification<DeliveryNote> deliveryAddressPredicate = (root, query, cb) -> cb
                    .equal(root.get("deliveryAddress"), deliveryAddress.trim());

            filterConditions = filterConditions.and(deliveryAddressPredicate);
        } else if (FilterType.contains.getValue().contains(deliveryAddressFilter.getType())) {

            Specification<DeliveryNote> deliveryAddressPredicate = (root, query, cb) -> cb
                    .like(root.get("deliveryAddress"), "%" + escapedDeliveryAddress + "%", '!');

            filterConditions = filterConditions.and(deliveryAddressPredicate);
        }

        return filterConditions;
    }

    /**
    protected Specification<DeliveryNote> makeIsPaidCriteria(final SetColumnFilter paidStatusFilter) {
        // Paid Status filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

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

        Specification<DeliveryNote> paidStatusPredicate = (root, query, cb) -> {
            if (distinctPaymentStatuses.contains("yes")) {
                return cb.isTrue(root.get("paid").as(Boolean.class));
            } else if (distinctPaymentStatuses.contains("no")) {
                return cb.isFalse(root.get("paid").as(Boolean.class));
            }
            return null;
        };

        return filterConditions.and(paidStatusPredicate);
    }

    protected Specification<DeliveryNote> makeClientNameCriteria(final TextColumnFilter clientNameFilter) {
        // Client Name filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

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

            Specification<DeliveryNote> clientNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("saleOrder")
                    .get("quote")
                    .get("project")
                    .get("client")
                    .get("name"), clientName.trim());

            filterConditions = filterConditions.and(clientNamePredicate);
        } else if (FilterType.contains.getValue().equals(clientNameFilter.getType())) {

            Specification<DeliveryNote> clientNamePredicate = (root, query, cb) -> cb
                    .like(root.get("saleOrder")
                    .get("quote")
                    .get("project")
                    .get("client")
                    .get("name"), "%" + escapedClientName + "%", '!');

            filterConditions = filterConditions.and(clientNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeProjectNameCriteria(final TextColumnFilter projectNameFilter) {
        // Project Name filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

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

            Specification<DeliveryNote> projectNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("project").get("name"), projectName.trim());

            filterConditions = filterConditions.and(projectNamePredicate);
        } else if (FilterType.contains.getValue().contains(projectNameFilter.getType())) {

            Specification<DeliveryNote> projectNamePredicate = (root, query, cb) -> cb
                    .like(root.get("project").get("name"), "%" + escapedProjectName + "%", '!');

            filterConditions = filterConditions.and(projectNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<DeliveryNote> makeDateIssuedCriteria(final DateColumnFilter dateInvoicedFilter) {
        // Date Invoiced filtering
        Specification<DeliveryNote> filterConditions = Specification.where(null);

        if (dateInvoicedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateInvoicedCriteria: {}", dateInvoicedFilter);

        LocalDateTime dateInvoicedFrom = dateInvoicedFilter.getDateFrom();
        LocalDateTime dateInvoicedTo = dateInvoicedFilter.getDateTo();

        if (dateInvoicedFrom == null && dateInvoicedTo == null) {
            logger.debug("Both dateInvoicedFrom and dateInvoicedTo are null");

            Specification<DeliveryNote> dateInvoicedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateInvoicedFilter.getType())) {
                case empty:
                case blank:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateCreated").as(LocalDate.class));
                    break;

                case notBlank:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateCreated").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateInvoicedFromFilterCondition); // Early return for clarity
        } else if (dateInvoicedFrom != null && dateInvoicedTo != null) {

            LocalDate dateInvoicedFromLocalDate = dateInvoicedFrom.toLocalDate();
            LocalDate dateInvoicedToLocalDate = dateInvoicedTo.toLocalDate();

            Specification<DeliveryNote> dateInvoicedFilterCondition = Specification.where(null);
            if (dateInvoicedFromLocalDate.compareTo(dateInvoicedToLocalDate) == 0) {
                dateInvoicedFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateCreated").as(LocalDate.class), dateInvoicedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateInvoicedFrom.toLocalDate().isBefore(dateInvoicedTo.toLocalDate())
                        ? dateInvoicedFrom.toLocalDate()
                        : dateInvoicedTo.toLocalDate();
                final LocalDate filterEndDate = dateInvoicedFrom.toLocalDate().isAfter(dateInvoicedTo.toLocalDate())
                        ? dateInvoicedFrom.toLocalDate()
                        : dateInvoicedTo.toLocalDate();

                dateInvoicedFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateCreated").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateInvoicedFilterCondition);
        } else if (dateInvoicedFrom != null) {
            Specification<DeliveryNote> dateInvoicedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateInvoicedFilter.getType())) {
                case equals:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateCreated").as(LocalDate.class),
                                    dateInvoicedFrom.toLocalDate());
                    break;
                case before:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateCreated").as(LocalDate.class), dateInvoicedFrom.toLocalDate());
                    break;
                case after:
                    dateInvoicedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateCreated").as(LocalDate.class),
                                    dateInvoicedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateInvoicedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }
**/
}
