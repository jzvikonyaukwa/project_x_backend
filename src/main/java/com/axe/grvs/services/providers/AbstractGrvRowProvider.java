package com.axe.grvs.services.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.axe.common.agGrid.filter.DateColumnFilter; 
import com.axe.common.agGrid.filter.FilterType;
import com.axe.common.agGrid.filter.TextColumnFilter; 

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;
import com.axe.grvs.GRV; 
 

abstract class AbstractGrvRowProvider {
    private static final Logger logger = LoggerFactory.getLogger(AbstractGrvRowProvider.class);

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

    protected Specification<GRV> makeGrvIdCriteria(final TextColumnFilter grvIdFilter) {
        // GRV ID filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (grvIdFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by grvIdFilter: {}", grvIdFilter);

        String grvId = grvIdFilter.getFilter();
        logger.debug("Filtering by grvId: {}", grvId);

        if (grvId.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedGrvId =  this.escapeSpecialCharactersInString().apply(grvId);
        logger.debug("Filtering by escaped grv ID: {}", escapedGrvId);

        if (FilterType.equals.getValue().equals(grvIdFilter.getType())) {

            Specification<GRV> grvIdPredicate = (root, query, cb) -> cb
                    .equal(root.get("id"), grvId);

            filterConditions = filterConditions.and(grvIdPredicate);
        } else if (FilterType.contains.getValue().contains(grvIdFilter.getType())) {

            Specification<GRV> grvIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), "%" + escapedGrvId + "%", '!');

            filterConditions = filterConditions.and(grvIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(grvIdFilter.getType())) {

            Specification<GRV> grvIdPredicate = (root, query, cb) -> cb
                    .like(root.get("id").as(String.class), escapedGrvId + "%", '!');

            filterConditions = filterConditions.and(grvIdPredicate);
        }

        return filterConditions;
    }  

    protected Specification<GRV> makePOIdCriteria(final TextColumnFilter poIdFilter) {
        // GRV ID filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (poIdFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by poIdFilter: {}", poIdFilter);

        String poId = poIdFilter.getFilter();
        logger.debug("Filtering by poId: {}", poId);

        if (poId.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedGrvId =  this.escapeSpecialCharactersInString().apply(poId);
        logger.debug("Filtering by escaped PO ID: {}", escapedGrvId);

        if (FilterType.equals.getValue().equals(poIdFilter.getType())) {

            Specification<GRV> poIdPredicate = (root, query, cb) -> cb
                    .equal(root.get("purchaseOrder").get("id"), poId);

            filterConditions = filterConditions.and(poIdPredicate);
        } else if (FilterType.contains.getValue().contains(poIdFilter.getType())) {

            Specification<GRV> poIdPredicate = (root, query, cb) -> cb
                    .like(root.get("purchaseOrder").get("id").as(String.class), "%" + escapedGrvId + "%", '!');

            filterConditions = filterConditions.and(poIdPredicate);
        } else if (FilterType.startsWith.getValue().startsWith(poIdFilter.getType())) {

            Specification<GRV> poIdPredicate = (root, query, cb) -> cb
                    .like(root.get("purchaseOrder").get("id").as(String.class), escapedGrvId + "%", '!');

            filterConditions = filterConditions.and(poIdPredicate);
        }

        return filterConditions;
    }

    protected Specification<GRV> makeDateReceivedCriteria(final DateColumnFilter dateReceivedFilter) {
        // Date Issued filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (dateReceivedFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by dateReceivedCriteria: {}", dateReceivedFilter);

        LocalDateTime dateReceivedFrom = dateReceivedFilter.getDateFrom();
        LocalDateTime dateReceivedTo = dateReceivedFilter.getDateTo();

        if (dateReceivedFrom == null && dateReceivedTo == null) {
            logger.debug("Both dateReceivedFrom and dateReceivedTo are null");

            Specification<GRV> dateReceivedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateReceivedFilter.getType())) {
                case empty:
                case blank:
                    dateReceivedFromFilterCondition = (root, query, cb) -> cb
                            .isNull(root.get("dateReceived").as(LocalDate.class));
                    break;

                case notBlank:
                    dateReceivedFromFilterCondition = (root, query, cb) -> cb
                            .isNotNull(root.get("dateReceived").as(LocalDate.class));
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateReceivedFromFilterCondition); // Early return for clarity
        } else if (dateReceivedFrom != null && dateReceivedTo != null) {

            LocalDate dateReceivedFromLocalDate = dateReceivedFrom.toLocalDate();
            LocalDate dateReceivedToLocalDate = dateReceivedTo.toLocalDate();

            Specification<GRV> dateReceivedFromFilterCondition = Specification.where(null);
            if (dateReceivedFromLocalDate.compareTo(dateReceivedToLocalDate) == 0) {
                dateReceivedFromFilterCondition = (root, query, cb) -> cb
                        .equal(root.get("dateReceived").as(LocalDate.class), dateReceivedFromLocalDate);
            } else {
                final LocalDate filterStartDate = dateReceivedFromLocalDate.isBefore(dateReceivedToLocalDate)
                        ? dateReceivedFromLocalDate
                        : dateReceivedToLocalDate;
                final LocalDate filterEndDate = dateReceivedFromLocalDate.isAfter(dateReceivedToLocalDate)
                        ? dateReceivedFromLocalDate
                        : dateReceivedToLocalDate;

                dateReceivedFromFilterCondition = (root, query, cb) -> cb
                        .between(root.get("dateReceived").as(LocalDate.class),
                                filterStartDate, filterEndDate);
            }
            return filterConditions.and(dateReceivedFromFilterCondition);
        } else if (dateReceivedFrom != null) {
            Specification<GRV> dateReceivedFromFilterCondition = Specification.where(null);
            switch (FilterType.fromValue(dateReceivedFilter.getType())) {
                case equals:
                    dateReceivedFromFilterCondition = (root, query, cb) -> cb
                            .equal(root.get("dateReceived").as(LocalDate.class),
                                    dateReceivedFrom.toLocalDate());
                    break;
                case before:
                    dateReceivedFromFilterCondition = (root, query, cb) -> cb
                            .lessThan(root.get("dateReceived").as(LocalDate.class), dateReceivedFrom.toLocalDate());
                    break;
                case after:
                    dateReceivedFromFilterCondition = (root, query, cb) -> cb
                            .greaterThan(root.get("dateReceived").as(LocalDate.class),
                                    dateReceivedFrom.toLocalDate());
                    break;
                default:
                    break;
            }
            return filterConditions.and(dateReceivedFromFilterCondition); // Early return for clarity
        }

        return filterConditions;
    }

    protected Specification<GRV> makeSupplierNameCriteria(final TextColumnFilter supplierNameFilter) {
        // Supplier Name filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (supplierNameFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by supplierNameFilter: {}", supplierNameFilter);

        String supplierName = supplierNameFilter.getFilter();
        logger.debug("Filtering by supplier name: {}", supplierName);

        if (supplierName.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedSupplierNames = supplierName.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped supplier name: {}", escapedSupplierNames);

        if (FilterType.equals.getValue().equals(supplierNameFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("purchaseOrder")
                    .get("supplier")
                    .get("name"), supplierName.trim());

            filterConditions = filterConditions.and(supplierNamePredicate);
        } else if (FilterType.contains.getValue().contains(supplierNameFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .like(root.get("purchaseOrder")
                    .get("supplier")
                    .get("name"), "%" + escapedSupplierNames + "%", '!');

            filterConditions = filterConditions.and(supplierNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<GRV> makeCommentsCriteria(final TextColumnFilter commentsFilter) {
        // Comments filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (commentsFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by commentsFilter: {}", commentsFilter);

        String comments = commentsFilter.getFilter();
        logger.debug("Filtering by comments: {}", comments);

        if (comments.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedSupplierNames = comments.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped comments: {}", escapedSupplierNames);

        if (FilterType.equals.getValue().equals(commentsFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("comments"), comments.trim());

            filterConditions = filterConditions.and(supplierNamePredicate);
        } else if (FilterType.contains.getValue().contains(commentsFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .like(root.get("comments") , "%" + escapedSupplierNames + "%", '!');

            filterConditions = filterConditions.and(supplierNamePredicate);
        }

        return filterConditions;
    }

    protected Specification<GRV> makeSupplierGRVCodeCriteria(final TextColumnFilter supplierGRVCodeFilter) {
        // Comments filtering
        Specification<GRV> filterConditions = Specification.where(null);

        if (supplierGRVCodeFilter == null) {
            return filterConditions; // Early return for clarity
        }

        logger.debug("Filtering by supplierGRVCodeFilter: {}", supplierGRVCodeFilter);

        String supplierGRVCode = supplierGRVCodeFilter.getFilter();
        logger.debug("Filtering by supplierGRVCode: {}", supplierGRVCode);

        if (supplierGRVCode.isBlank()) {
            return filterConditions; // Early return for clarity
        }

        // Escape %, _, and the escape character '!'
        final String escapedSupplierNames = supplierGRVCode.trim().replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
        logger.debug("Filtering by escaped supplierGRVCode: {}", escapedSupplierNames);

        if (FilterType.equals.getValue().equals(supplierGRVCodeFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .equal(root.get("supplierGRVCode"), supplierGRVCode.trim());

            filterConditions = filterConditions.and(supplierNamePredicate);
        } else if (FilterType.contains.getValue().contains(supplierGRVCodeFilter.getType())) {

            Specification<GRV> supplierNamePredicate = (root, query, cb) -> cb
                    .like(root.get("supplierGRVCode") , "%" + escapedSupplierNames + "%", '!');

            filterConditions = filterConditions.and(supplierNamePredicate);
        }

        return filterConditions;
    }
    // protected Specification<GRV> makeWarehouseCriteria(final SetColumnFilter warehouseFilter) {
    //     // Status filtering
    //     Specification<GRV> filterConditions = Specification.where(null);

    //     if (warehouseFilter == null) {
    //         return filterConditions; // Early return for clarity
    //     }

    //     logger.debug("Filtering by statusCriteria: {}", warehouseFilter);

    //     final Set<String> warehouseValues = warehouseFilter.getValues().stream()
    //             // .map(QuoteStatus::fromValue)
    //             .sorted()
    //             .collect(Collectors.toSet()); // Changed to use collect toSet() method

    //     logger.debug("Filtering by warehouse: {}", warehouseValues);
    //     Specification<GRV> statusPredicate = Specification.where(null);
    //     if (warehouseValues.size() == 1) {
    //         statusPredicate = (root, query, cb) -> cb.equal(root.get("warehouse").get("name"), warehouseValues.toArray()[0]);
    //         return filterConditions.and(statusPredicate);
    //     } else if (warehouseValues.size() != 2) {

    //         statusPredicate = (root, query, cb) -> root.get("warehouse").get("name").in(warehouseValues);
    //         return filterConditions.and(statusPredicate);
    //     }

    //     return filterConditions;
    // }
}
