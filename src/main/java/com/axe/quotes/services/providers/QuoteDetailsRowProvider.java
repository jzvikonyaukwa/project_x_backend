package com.axe.quotes.services.providers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.axe.common.agGrid.AgGridRowProvider;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.quotes.quotesDTO.QuoteDetailsDTO;

import com.axe.clients.Client;
import com.axe.common.agGrid.filter.ColumnFilter;
import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.NumberColumnFilter;
import com.axe.common.agGrid.filter.SetColumnFilter;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.projects.Project;
import com.axe.quotes.Quote;
import com.axe.quotes.QuotesRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class QuoteDetailsRowProvider extends AbstractQuoteRowProvider
        implements AgGridRowProvider<List<QuoteDetailsDTO>> {
    private static final Logger logger = LoggerFactory.getLogger(QuoteDetailsRowProvider.class);

    private final QuotesRepository repo;

    public QuoteDetailsRowProvider(QuotesRepository quotesRepository) {
        this.repo = quotesRepository;
    }

    @Override
    public ServerSideGetRowsResponse<List<QuoteDetailsDTO>> getRows(final ServerSideGetRowsRequest request) {
        logger.debug("Fetching data for request: {}", request);
        try {
            Specification<Quote> filterConditions = Specification.where(null);

            final Map<String, ColumnFilter> filterModel = request.getFilterModel();
            if (filterModel != null && !filterModel.isEmpty()) {
                logger.debug("Filter model: {}", filterModel);
                // Client ID filtering
                final NumberColumnFilter clientIdFilter = (NumberColumnFilter) request.getFilterModel().get("clientId");
                if (clientIdFilter != null) {
                    final Specification<Quote> clientIdPredicate = makeClientIdCriteria(clientIdFilter);
                    filterConditions = filterConditions.and(clientIdPredicate);
                }

                // Client Name filtering
                final TextColumnFilter clientNameCriteria = (TextColumnFilter) request.getFilterModel()
                        .get("clientName");
                if (clientNameCriteria != null) {
                    final Specification<Quote> clientNamePredicate = makeClientNameCriteria(clientNameCriteria);
                    filterConditions = filterConditions.and(clientNamePredicate);
                }

                // Quote ID filtering
                final TextColumnFilter quoteIdFilter = (TextColumnFilter) request.getFilterModel().get("id");
                if (quoteIdFilter != null) {
                    final Specification<Quote> quoteIdPredicate = makeQuoteIdCriteria(quoteIdFilter);
                    filterConditions = filterConditions.and(quoteIdPredicate);
                }

                // Date Issued filtering
                final DateColumnFilter dateIssuedFilter = (DateColumnFilter) request.getFilterModel().get("dateIssued");
                if (dateIssuedFilter != null) {
                    final Specification<Quote> dateIssuedPredicate = makeDateIssuedCriteria(dateIssuedFilter);
                    filterConditions = filterConditions.and(dateIssuedPredicate);
                }

                // Date Accepted filtering
                final DateColumnFilter dateAcceptedCriteria = (DateColumnFilter) request.getFilterModel()
                        .get("dateAccepted");
                if (dateAcceptedCriteria != null) {
                    final Specification<Quote> dateAcceptedPredicate = makeDateAcceptedCriteria(dateAcceptedCriteria);
                    filterConditions = filterConditions.and(dateAcceptedPredicate);
                }

                // Project Name filtering
                final TextColumnFilter projectNameCriteria = (TextColumnFilter) request.getFilterModel()
                        .get("projectName");
                if (projectNameCriteria != null) {
                    final Specification<Quote> projectNamePredicate = makeProjectNameCriteria(projectNameCriteria);
                    filterConditions = filterConditions.and(projectNamePredicate);
                }

                // Status filtering
                final SetColumnFilter statusCriteria = (SetColumnFilter) request.getFilterModel().get("status");
                if (statusCriteria != null) {
                    final Specification<Quote> statusPredicate = makeStatusCriteria(statusCriteria);
                    filterConditions = filterConditions.and(statusPredicate);
                }
            }

            long rowsFound = repo.count(filterConditions);
            logger.debug("Rows found: {}", rowsFound);
            if (rowsFound == 0) {
                return new ServerSideGetRowsResponse<>(Collections.emptyList(), 0, null);
            }

            int pageNumber = request.getStartRow() / request.getRowCount();

            List<Sort.Order> sortOrder = new ArrayList<>();
            if (request.getSortModel() != null) {
                // Add sorting
                request.getSortModel().forEach(sortModel -> {
                    String sortField = sortModel.getColId();
                    if ("clientName".equalsIgnoreCase(sortField)) {
                        sortField = "project.client.name";
                    }
                    if ("projectName".equalsIgnoreCase(sortField)) {
                        sortField = "project.name";
                    }
                    String sortDirection = sortModel.getSort();
                    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                    sortOrder.add(new Sort.Order(direction, sortField));
                });
            }

            // Always add default sort order at the end
            sortOrder.add(new Sort.Order(Sort.Direction.DESC, "id"));
            Pageable pageRequest = PageRequest.of(pageNumber, request.getRowCount(), Sort.by(sortOrder));

            List<Quote> matchingRecords = repo.findAll(filterConditions, pageRequest).toList();
            List<QuoteDetailsDTO> converted = new ArrayList<>();
            for (Quote quote : matchingRecords) {
                Project project = quote.getProject();
                Client client = (project != null) ? project.getClient() : null;
                QuoteDetailsDTO item = new QuoteDetailsDTO(quote.getId(), client == null ? "" : client.getName(),
                       project != null ? project.getName() : "", quote.getDateIssued(),
                        quote.getDateLastModified(), quote.getDateAccepted(), quote.getStatus().toString(),
                        quote.getNotes(), client == null ? 0L : client.getId());
                converted.add(item);
            }

            return new ServerSideGetRowsResponse<>(converted, (int) rowsFound, null);
        } catch (DataAccessException e) {
            logger.error("Data access error: {}", e.getMostSpecificCause().getLocalizedMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getLocalizedMessage());
        }

        return new ServerSideGetRowsResponse<>(Collections.emptyList(), 0, null);
    }
}
