package com.axe.quotes;

import com.axe.common.enums.QuoteStatus;
import com.axe.quotes.quotesDTO.QuoteDetailsDTO;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteStatusCount;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface QuotesRepository extends JpaRepository<Quote, Long> //
, JpaSpecificationExecutor<Quote> {


    @Query("SELECT q FROM Quote q ORDER BY q.dateLastModified DESC")
    List<Quote> getAllQuotes();

    @Query("""
    SELECT
        q.id as id, c.name as clientName, q.dateIssued as dateIssued, p.name as projectName,\s
        q.dateLastModified as dateLastModified, q.dateAccepted as dateAccepted,\s
        q.status as status, q.notes as notes, c.id as clientId\s
    FROM
        Quote q
    JOIN
        q.project p
    JOIN
        p.client c
    ORDER BY
        q.dateLastModified
    DESC""")
    List<QuoteDetailsDTO> getAllQuotesDetails();

    @Query("SELECT " +
            "SUM(CASE WHEN q.status = 'accepted' THEN 1 ELSE 0 END) AS acceptedCount, " +
            "SUM(CASE WHEN q.status = 'rejected' THEN 1 ELSE 0 END) AS rejectedCount, " +
            "SUM(CASE WHEN q.status = 'draft' THEN 1 ELSE 0 END) AS draftCount, " +
            "SUM(CASE WHEN q.status = 'pending approval' THEN 1 ELSE 0 END) AS pendingApprovalCount " +
            "FROM Quote q " +
            "WHERE q.dateIssued BETWEEN :startDate AND :endDate")
    QuoteStatusCount getQuoteStatusCountByDateRange(@Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);


    // @Query("SELECT new com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuotesPerDayDTO(DAYNAME(q.dateIssued), COUNT(q.id), MIN(q.dateIssued)) " +
    //         "FROM Quote q " +
    //         "WHERE q.dateIssued BETWEEN :startDate AND :endDate " +
    //         "GROUP BY DAYNAME(q.dateIssued), DAYOFWEEK(q.dateIssued) " +
    //         "ORDER BY DAYOFWEEK(q.dateIssued)")
    // List<QuotesPerDayDTO> findQuotesCountPerDay(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // List<Quote> findAllByDateIssuedBetween(LocalDate startDate, LocalDate endDate);

    // @Query("""
    //     SELECT 
    //         coq.quotePrice
    //     FROM 
    //         ConsumableOnQuote coq
    //     JOIN 
    //         coq.quote q
    //         WHERE q.id = :quoteID
    //         GROUP BY coq.quotePrice
    //     """)
    // List<QuotePrice> fetchQuoteQuotePrices(Long quoteID);





    interface Specs {
        /**
         * Filter Quote by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Quote> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter Quote by status.
         * 
         * @param status The status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Quote> withStatus(QuoteStatus status) {
            return (root, query, builder) -> builder.equal(root.get("status"),  status);
        }
        
        /**
         * Filter Quote by clientID.
         * 
         * @param id The clientID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<Quote> withClientId(Number clientID) {
            return (root, query, builder) -> builder.equal(root.get("project").get("client").get("id"), clientID);
        }
    }
}
