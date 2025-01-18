package com.axe.quotePrice;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuotePriceRepository extends JpaRepository<QuotePrice, Long> //
, JpaSpecificationExecutor<QuotePrice> {


    interface Specs {
        /**
         * Filter QuotePrice by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<QuotePrice> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter QuotePrice by preset status.
         * 
         * @param isPreset The preset status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<QuotePrice> isPreset(boolean isPreset) {
            // when dateEdited is null this is a preset
            return (root, query, builder) -> isPreset ? builder.isNull(root.get("dateEdited")) : builder.isNotNull(root.get("dateEdited"));
        }

        /**
         * Filter QuotePrice by preset status.
         * 
         * @return A Specification object representing the filter criteria.
         */
        static Specification<QuotePrice> isPreset() {
            // when dateEdited is null this is a preset
            return (root, query, builder) -> isPreset(true).toPredicate(root, query, builder);
        }

        /**
         * Filter QuotePrice by QuoteID.
         * 
         * @param id The QuoteID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<QuotePrice> withQuoteId(Number quoteID) {
            return (root, query, builder) -> builder.equal(root.get("quotes").get("id"), quoteID);
        }
    }
}
