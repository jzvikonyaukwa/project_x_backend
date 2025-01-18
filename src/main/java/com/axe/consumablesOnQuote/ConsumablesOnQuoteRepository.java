package com.axe.consumablesOnQuote;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConsumablesOnQuoteRepository extends JpaRepository<ConsumableOnQuote, Long> //
, JpaSpecificationExecutor<ConsumableOnQuote> {




    interface Specs {
        /**
         * Filter ConsumableOnQuote by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<ConsumableOnQuote> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter ConsumableOnQuote by quoteID.
         * 
         * @param quoteID The quoteID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<ConsumableOnQuote> withQuoteId(Number quoteID) {
            return (root, query, builder) -> builder.equal(root.get("quote").get("id"), quoteID);
        }
    }
}
