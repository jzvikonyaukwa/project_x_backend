package com.axe.delivery_notes.providers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.axe.common.agGrid.AgGridRowProvider;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.common.agGrid.filter.NumberColumnFilter;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.common.agGrid.filter.ColumnFilter;
import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.delivery_notes.DeliveryNoteRepository;
import com.axe.delivery_notes.delivery_notesDTOs.DeliveryNoteGetAllDto;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class DeliveryNoteGetAllDtoRowProvider extends AbstractDeliveryNoteRowProvider
        implements AgGridRowProvider<List<DeliveryNoteGetAllDto>> {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryNoteGetAllDtoRowProvider.class);

    private final DeliveryNoteRepository repo;

    public DeliveryNoteGetAllDtoRowProvider(DeliveryNoteRepository deliveryNoteRepository) {
        this.repo = deliveryNoteRepository;
    }

    @Override
    public ServerSideGetRowsResponse<List<DeliveryNoteGetAllDto>> getRows(final ServerSideGetRowsRequest request) {
        logger.debug("Getting delivery notes for request: {}", request);
        try {
            Specification<DeliveryNote> filterConditions = Specification.where(null);

            final Map<String, ColumnFilter> filterModel = request.getFilterModel();
            if (filterModel != null && !filterModel.isEmpty()) {
                logger.debug("Filter model: {}", filterModel);

                // ID filtering
                final TextColumnFilter objectIDFilter = (TextColumnFilter) request.getFilterModel().get("id");
                if (objectIDFilter != null) {
                    final Specification<DeliveryNote> objectIDPredicate = makeIdCriteria(objectIDFilter);
                    filterConditions = filterConditions.and(objectIDPredicate);
                }

                // Client ID filtering
                final NumberColumnFilter clientIDFilter = (NumberColumnFilter) request.getFilterModel().get("clientId");
                if (clientIDFilter != null) {
                    final Specification<DeliveryNote> clientIDPredicate = makeClientIdCriteria(clientIDFilter);
                    filterConditions = filterConditions.and(clientIDPredicate);
                }

                // Date Issued filtering
                final DateColumnFilter dateCreatedFilter = (DateColumnFilter) request.getFilterModel().get("dateCreated");
                if (dateCreatedFilter != null) {
                    final Specification<DeliveryNote> dateCreatedPredicate = makeDateCreatedCriteria(dateCreatedFilter);
                    filterConditions = filterConditions.and(dateCreatedPredicate);
                }

                // Date Delivered filtering
                final DateColumnFilter dateDeliveredFilter = (DateColumnFilter) request.getFilterModel().get("dateDelivered");
                if (dateDeliveredFilter != null) {
                    final Specification<DeliveryNote> dateDeliveredPredicate = makeDateDeliveredCriteria(dateDeliveredFilter);
                    filterConditions = filterConditions.and(dateDeliveredPredicate);
                }

                // Delivery Address filtering
                final TextColumnFilter deliveryAddressCriteria = (TextColumnFilter) request.getFilterModel()
                        .get("deliveryAddress");
                if (deliveryAddressCriteria != null) {
                    final Specification<DeliveryNote> deliveryAddressPredicate = makeDeliveryAddressCriteria(deliveryAddressCriteria);
                    filterConditions = filterConditions.and(deliveryAddressPredicate);
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
                    String sortDirection = sortModel.getSort();
                    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC
                            : Sort.Direction.ASC;
                    sortOrder.add(new Sort.Order(direction, sortField));
                });
            }

            Pageable pageable = PageRequest.of(pageNumber, request.getRowCount(),
                    sortOrder.isEmpty() ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(sortOrder));
            List<DeliveryNote> matchingRecords = repo.findAll(filterConditions, pageable).getContent();

            List<DeliveryNoteGetAllDto> converted = new ArrayList<>();
            for (DeliveryNote deliveryNote : matchingRecords) {
                DeliveryNoteGetAllDto dto = new DeliveryNoteGetAllDto();
                dto.setId(deliveryNote.getId());
                dto.setDateCreated(deliveryNote.getDateCreated());
                dto.setDateDelivered(deliveryNote.getDateDelivered());
                dto.setStatus(deliveryNote.getStatus());
                dto.setDeliveryAddress(deliveryNote.getDeliveryAddress());
                converted.add(dto);
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
