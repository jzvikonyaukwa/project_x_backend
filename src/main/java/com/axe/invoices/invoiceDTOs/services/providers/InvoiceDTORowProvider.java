package com.axe.invoices.invoiceDTOs.services.providers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.axe.common.agGrid.AgGridRowProvider;
import com.axe.common.agGrid.filter.ColumnFilter;
import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.SetColumnFilter;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.invoices.Invoice;
import com.axe.invoices.InvoiceRepository;
import com.axe.invoices.DTOs.InvoiceDTO;
import com.axe.saleOrder.SaleOrder;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class InvoiceDTORowProvider extends AbstractInvoiceRowProvider implements AgGridRowProvider<List<InvoiceDTO>> {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceDTORowProvider.class);

    private final InvoiceRepository repo;

    public InvoiceDTORowProvider(InvoiceRepository invoiceRepository) {
        this.repo = invoiceRepository;
    }

    @Override
    public ServerSideGetRowsResponse<List<InvoiceDTO>> getRows(final ServerSideGetRowsRequest request) {
        logger.debug("Getting all invoices for request: {}", request);
        try {
            Specification<Invoice> filterConditions = Specification.where(null);

            final Map<String, ColumnFilter> filterModel = request.getFilterModel();
            if (filterModel != null && !filterModel.isEmpty()) {
                logger.debug("Filter model: {}", filterModel);

                // ID filtering
                final TextColumnFilter objectIDFilter = (TextColumnFilter) request.getFilterModel().get("id");
                if (objectIDFilter != null) {
                    final Specification<Invoice> objectIDPredicate = makeIdCriteria(objectIDFilter);
                    filterConditions = filterConditions.and(objectIDPredicate);
                }

                // QuoteID filtering
                final TextColumnFilter quoteIdFilter = (TextColumnFilter) request.getFilterModel().get("quoteId");
                if (quoteIdFilter != null) {
                    final Specification<Invoice> quoteIdPredicate = makeQuoteIdCriteria(quoteIdFilter);
                    filterConditions = filterConditions.and(quoteIdPredicate);
                }

                // Client Name filtering
                final TextColumnFilter clientNameCriteria = (TextColumnFilter) request.getFilterModel()
                        .get("clientName");
                if (clientNameCriteria != null) {
                    final Specification<Invoice> clientNamePredicate = makeClientNameCriteria(clientNameCriteria);
                    filterConditions = filterConditions.and(clientNamePredicate);
                }
  
                // Date Invoiced filtering
                final DateColumnFilter dateInvoicedFilter = (DateColumnFilter) request.getFilterModel().get("dateInvoiced");
                if (dateInvoicedFilter != null) {
                    final Specification<Invoice> dateInvoicedPredicate = makeDateIssuedCriteria(dateInvoicedFilter);
                    filterConditions = filterConditions.and(dateInvoicedPredicate);
                }

                // Status filtering
                final SetColumnFilter isPaidCriteria = (SetColumnFilter) request.getFilterModel().get("paid");
                if (isPaidCriteria != null) {
                    final Specification<Invoice> isPaidPredicate = makeIsPaidCriteria(isPaidCriteria);
                    filterConditions = filterConditions.and(isPaidPredicate);
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
                request.getSortModel().forEach(sortDescriptor -> {
                    String sortField = sortDescriptor.getColId();

                    // Replace shorthand field names with their full paths
                    if ("clientName".equalsIgnoreCase(sortField)) {
                        sortField = "project.client.name";
                    }
                    if ("projectName".equalsIgnoreCase(sortField)) {
                        sortField = "project.name";
                    }
                    String sortDirection = sortDescriptor.getSort();
                    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                    sortOrder.add(new Sort.Order(direction, sortField));
                });
            }

            // Always add default sort order at the end
            sortOrder.add(new Sort.Order(Sort.Direction.DESC, "id"));

            Pageable pageRequest = PageRequest.of(pageNumber, request.getRowCount(), Sort.by(sortOrder));

            List<Invoice> matchingRecords = repo.findAll(filterConditions, pageRequest).toList();

            List<InvoiceDTO> converted = new ArrayList<>();
            for (Invoice invoice : matchingRecords) {
                SaleOrder saleOrder = invoice.getSaleOrder();
                InvoiceDTO invoiceDTO = new InvoiceDTO(invoice.getId(), invoice.getDateInvoiced(), invoice.getPaid(),
                saleOrder != null && saleOrder.getQuote() != null ? saleOrder.getQuote().getId(): null,
                saleOrder != null && saleOrder.getQuote() != null ? saleOrder.getQuote().getProject().getClient().getName(): null);
                converted.add(invoiceDTO);
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
