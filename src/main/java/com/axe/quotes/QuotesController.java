package com.axe.quotes;

import com.auth0.jwt.JWT;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.quotePrice.QuotePrice;
import com.axe.quotes.quotesDTO.*;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteStatusCount;
import com.axe.quotes.quotesDTO.qoutesSummaryDTO.QuoteSummaryDTO;
import com.axe.quotes.services.QuoteStatusChangeService;
import com.axe.quotes.services.QuoteSummaryService;
import com.axe.quotes.services.QuotesService;
import com.axe.utilities.ClientVisibleException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@Tag(name = "Quotes", description = "Quotation specific APIs")
@RequestMapping("/api/quotes")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class QuotesController {
    private final static Logger logger = LoggerFactory.getLogger(QuotesController.class);

    private final QuotesService quotesService;
    private final QuoteStatusChangeService quoteStatusChangeService;
    private final QuoteSummaryService quoteSummaryService;

    public QuotesController(QuotesService quotesService,
                            QuoteStatusChangeService quoteStatusChangeService,
                            QuoteSummaryService quoteSummaryService
                            ) {
        this.quotesService = quotesService;
        this.quoteStatusChangeService = quoteStatusChangeService;
        this.quoteSummaryService = quoteSummaryService;
    }

    @PostMapping("get-rows")
    @Operation(summary = "Get quotes details based on ag-grid request")
    public ServerSideGetRowsResponse<List<QuoteDetailsDTO>> processAgGridQuoteRequest(
            @RequestBody ServerSideGetRowsRequest request) {
        logger.info("Getting quotes for AG Grid request: {}", request);
        try {
            return quotesService.fetchQuoteDetailsDTOGridRows(request);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error fetching quotes for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching quotes.");
        }
    }

    @PostMapping("all-quotes")
    @Operation(summary = "Get quotes based on ag-grid request")
    public ServerSideGetRowsResponse<List<Quote>> getAllQuotesForClient(
            @RequestBody ServerSideGetRowsRequest request) {
        logger.info("Getting quotes for request: {}", request);
        try {
            return quotesService.fetchQuotesForAgGridRequest(request);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Error fetching quotes for AG Grid request: {}", e.getLocalizedMessage());
            throw new RuntimeException("Server encountered an error fetching quotes.");
        }
    }

    @PostMapping("add-quote")
    public Quote addQuote(@RequestBody QuotePostDTO quoteData){
        try {
        return quotesService.addQuote(quoteData);
    } catch (ClientVisibleException e) {
        throw new RuntimeException(e.getLocalizedMessage());
    } catch (Exception e) {
        throw new RuntimeException("Server encountered an error while submitting quote for approval");
    }
    }

    @GetMapping("get-quote/{id}")
    public Quote getQuote(@PathVariable Long id) {
        try {
            return quotesService.getQuote(id);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while fetching quote");
        }
    }

    // @GetMapping("{quoteID}/get-quote-prices")
    // @Operation(summary = "Get a list of all quote prices used on a quote (will always include preset quote prices)")
    // public List<QuotePrice> getQuoteQuotePrices(
    //         @Parameter(description = "ID of the quotation", example = "87654321") //
    //         @PathVariable Long quoteID) {
    //     logger.info("Fetching quote prices used on quoteID: {}", quoteID);
    //     try {
    //         return quotesService.getQuoteQuotePrices(quoteID);
    //     } catch (ClientVisibleException e) {
    //         throw new RuntimeException(e.getLocalizedMessage());
    //     } catch (Exception e) {
    //         throw new RuntimeException(
    //                 "Server encountered an error while fetching quote prices used on quoteID: " + quoteID);
    //     }
    // }

    @PatchMapping("update-quote")
    public Quote updateQuoteSections(@RequestBody Quote quote){
        logger.info("Updating quote sections: {}", quote.getId());
        try{
            return quotesService.updateQuoteSections(quote);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while updating quote sections");
        }
    }

    @PatchMapping("{quoteID}/submit-for-approval")
    @Operation(summary = "Submit quote for approval")
    public Quote submitForApproval(@RequestHeader(AUTHORIZATION) String header, @PathVariable Long quoteID) {
        logger.info("Submitting quote for approval: {}", quoteID);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return quoteStatusChangeService.requestQuoteApproval(userEmail, quoteID);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while submitting quote for approval");
        }
    }

    @PatchMapping("{quoteID}/approve")
    @Operation(summary = "Approve quote")
    public Quote approveQuote(@RequestHeader(AUTHORIZATION) String header, @PathVariable Long quoteID) {
        logger.info("Approving quote: {}", quoteID);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return quoteStatusChangeService.approveQuote(userEmail, quoteID);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while approving quote");
        }
    }

    @PatchMapping("{quoteID}/reject")
    @Operation(summary = "Reject quote")
    public Quote rejectQuote(@RequestHeader(AUTHORIZATION) String header, @PathVariable Long quoteID, @RequestBody RejectionDetailsDTO rejectionDetails) {
        logger.info("Rejecting quote: {}", quoteID);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return quoteStatusChangeService.rejectQuote(userEmail, quoteID, rejectionDetails);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while rejecting quote");
        }
    }

    @PatchMapping("{quoteID}/accept")
    @Operation(summary = "Accept quote")
    public Quote acceptQuote(@RequestHeader(AUTHORIZATION) String header, @PathVariable Long quoteID) {
        logger.info("Accepting quote: {}", quoteID);
        try {
            String userEmail = extractEmailFromAuthorizationHeader(header);
            return quoteStatusChangeService.acceptQuote(userEmail, quoteID);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while accepting quote");
        }
    }

    @GetMapping("/summary")
    public QuoteSummaryDTO getQuoteSummary(@RequestParam(value = "startDate", required = false) String startDateStr,
                                           @RequestParam(value = "endDate", required = false) String endDateStr) {
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);
        return quoteSummaryService.getQuoteSummary(startDate, endDate);
    }
    @GetMapping("/status-count")
    public QuoteStatusCount getQuoteStatusCount(
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate",required = false)String endDateStr) {
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);
        return quoteSummaryService.getQuoteStatusCountByDateRange(startDate, endDate);
    }

    @GetMapping("/per-day")
    @Operation(summary = "Dashboard - Get quotes count per day")
    @Transactional(readOnly = true)
    public List<com.axe.quotes.QuoteSummaryDTO> getQuotesCountPerDay(@RequestParam(value = "startDate", required = false) String startDateStr,
                                                      @RequestParam(value = "endDate",required = false)String endDateStr) {
        logger.info("Getting quotes count per day - startDate: {}, endDate: {}", startDateStr, endDateStr);
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        return quoteSummaryService.getQuotesCountPerDay(startDate, endDate);
    }

    @GetMapping("/latest")
    public List<QuoteDetailsDTO> getLatestQuotes() {
        return quoteSummaryService.getLatestQuotes();
    }

    @GetMapping("/totals")
    @Operation(summary = "Dashboard - Get quotes total value per day")
    @Transactional(readOnly = true)
    public ResponseEntity<List<com.axe.quotes.QuoteSummaryDTO>> getQuoteTotalsByDateRange(@RequestParam(value = "startDate", required = false) String startDateStr,
                                                                                 @RequestParam(value = "endDate",required = false)String endDateStr) {
        logger.info("Getting quote totals by date range - startDate: {}, endDate: {}", startDateStr, endDateStr);
        LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr) : LocalDate.now();
        LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr) : endDate.minusDays(30);

        return ResponseEntity.ok(quoteSummaryService.getQuoteTotalsByDateRange(startDate, endDate));
    }

    private String extractEmailFromAuthorizationHeader(final String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header");
        }
        String token = header.substring(7);
        return JWT.decode(token).getClaim("email").asString();
    }
}
