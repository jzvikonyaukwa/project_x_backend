package com.axe.grvs.services.providers;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.axe.colors.Color;
import com.axe.common.agGrid.AgGridRowProvider;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.consumables.Consumable;
import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.grvs.GRV;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.grvsDTO.GRVDetailDTO;
import com.axe.grvs.grvsDTO.GRVMasterDetailDTO;
import com.axe.common.agGrid.filter.ColumnFilter;
import com.axe.common.agGrid.filter.DateColumnFilter;
import com.axe.common.agGrid.filter.TextColumnFilter;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.warehouse.Warehouse;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class GrvMasterDetailRowProvider extends AbstractGrvRowProvider
        implements AgGridRowProvider<List<GRVMasterDetailDTO>> {
    private static final Logger logger = LoggerFactory.getLogger(GrvMasterDetailRowProvider.class);

    private final GRVsRepository repo;

    public GrvMasterDetailRowProvider(GRVsRepository grvsRepository) {
        this.repo = grvsRepository;
    }

    @Override
    public ServerSideGetRowsResponse<List<GRVMasterDetailDTO>> getRows(final ServerSideGetRowsRequest request) {
        logger.debug("Fetching data for request: {}", request);
        try {
            Specification<GRV> filterConditions = Specification.where(null);

            final Map<String, ColumnFilter> filterModel = request.getFilterModel();
            if (filterModel != null && !filterModel.isEmpty()) {
                logger.debug("Filter model: {}", filterModel);

                // GRV ID
                final TextColumnFilter grvIdFilter = (TextColumnFilter) request.getFilterModel().get("grvId");
                if (grvIdFilter != null) {
                    final Specification<GRV> quoteIdPredicate = makeGrvIdCriteria(grvIdFilter);
                    filterConditions = filterConditions.and(quoteIdPredicate);
                }

                // purchaseOrder ID
                final TextColumnFilter purchaseOrderIdFilter = (TextColumnFilter) request.getFilterModel()
                        .get("purchaseOrderId");
                if (purchaseOrderIdFilter != null) {
                    final Specification<GRV> quoteIdPredicate = makePOIdCriteria(purchaseOrderIdFilter);
                    filterConditions = filterConditions.and(quoteIdPredicate);
                }

                // Supplier Name
                final TextColumnFilter supplierNameFilter = (TextColumnFilter) request.getFilterModel()
                        .get("supplierName");
                if (supplierNameFilter != null) {
                    final Specification<GRV> supplierNamePredicate = makeSupplierNameCriteria(supplierNameFilter);
                    filterConditions = filterConditions.and(supplierNamePredicate);
                }

                // Comments
                final TextColumnFilter commentsFilter = (TextColumnFilter) request.getFilterModel()
                        .get("grvComments");
                if (commentsFilter != null) {
                    final Specification<GRV> commentsPredicate = makeCommentsCriteria(commentsFilter);
                    filterConditions = filterConditions.and(commentsPredicate);
                }

                // supplierGrvCode
                final TextColumnFilter supplierGRVCodeFilter = (TextColumnFilter) request.getFilterModel()
                        .get("supplierGrvCode");
                if (supplierGRVCodeFilter != null) {
                    final Specification<GRV> supplierGRVCodePredicate = makeSupplierGRVCodeCriteria(
                            supplierGRVCodeFilter);
                    filterConditions = filterConditions.and(supplierGRVCodePredicate);
                }

                // Date Received filtering
                final DateColumnFilter dateReceivedFilter = (DateColumnFilter) request.getFilterModel()
                        .get("dateReceived");
                if (dateReceivedFilter != null) {
                    final Specification<GRV> dateIssuedPredicate = makeDateReceivedCriteria(dateReceivedFilter);
                    filterConditions = filterConditions.and(dateIssuedPredicate);
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
                    if ("supplierName".equalsIgnoreCase(sortField)) {
                        sortField = "purchaseOrder.supplier.name";
                    }
                    if ("grvComments".equalsIgnoreCase(sortField)) {
                        sortField = "comments";
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

            List<GRV> matchingRecords = repo.findAll(filterConditions, pageRequest).toList();
            List<GRVMasterDetailDTO> converted = new ArrayList<>();
            for (GRV grv : matchingRecords) {

                GRVMasterDetailDTO master = GRVMasterDetailDTO.builder()
                        .grvId(grv.getId())
                        .dateReceived(grv.getDateReceived().toString())
                        .grvComments(grv.getComments())
                        .supplierGrvCode(grv.getSupplierGRVCode())
                        .build();

                List<GRVDetailDTO> details = new ArrayList<>();
                PurchaseOrder purchaseOrder = grv.getPurchaseOrder();
                if (purchaseOrder != null && purchaseOrder.getSupplier() != null) {
                    master.setSupplierName(purchaseOrder.getSupplier().getName());
                    master.setPurchaseOrderId(purchaseOrder.getId());
                }

                if (grv.getConsumablesOnGrv() != null && !grv.getConsumablesOnGrv().isEmpty()) {
                    for (ConsumablesOnGrv lineItem : grv.getConsumablesOnGrv()) {

                        Consumable consumable = lineItem.getConsumableInWarehouse().getConsumable();
                        Warehouse warehouse = lineItem.getConsumableInWarehouse().getWarehouse();

                        GRVDetailDTO grvDetail = GRVDetailDTO.builder().grvId(grv.getId())
                                .dateReceived(grv.getDateReceived())
                                .comments(grv.getComments())
                                .supplierGrvCode(grv.getSupplierGRVCode())
                                .consumableOnGrvId(grv.getId())
                                .consumableName(consumable.getName())
                                .serialNumber(consumable.getSerialNumber())
                                .uom(consumable.getUom())

                                .consumableCategoryId(consumable.getCategory().getId())

                                .avgLandedPrice(lineItem.getLandedPrice())
                                .qtyOrdered(lineItem.getQtyReceived())

                                .build();

                        if (warehouse != null) {
                            grvDetail.setConsumableWarehouseId(warehouse.getId());
                            grvDetail.setConsumableWarehouse(warehouse.getName());
                        }

                        if (purchaseOrder != null && purchaseOrder.getSupplier() != null) {
                            grvDetail.setPurchaseOrderId(purchaseOrder.getId());
                            grvDetail.setConsumableSupplierId(purchaseOrder.getSupplier().getId());
                            grvDetail.setConsumableSupplierName(purchaseOrder.getSupplier().getName());
                        }
                        details.add(grvDetail);
                    }
                }

                if (grv.getSteelCoils() != null && !grv.getSteelCoils().isEmpty()) {
                    for (SteelCoil lineItem : grv.getSteelCoils()) {

                        SteelSpecification steelSpecification = lineItem.getSteelSpecification();
                        Warehouse warehouse = lineItem.getWarehouse();
                        Color color = steelSpecification.getColor();

                        GRVDetailDTO grvDetail = GRVDetailDTO.builder().grvId(grv.getId())
                                .dateReceived(grv.getDateReceived())
                                .comments(grv.getComments())
                                .supplierGrvCode(grv.getSupplierGRVCode())
                                .isqGrade(steelSpecification.getISQGrade())
                                .landedCostPerMtr(lineItem.getLandedCostPerMtr())
                                .landedCostPerKg(lineItem.getLandedCostPerKg())
                                .estimatedMetersRemaining(lineItem.getEstimatedMetersRemaining())
                                .coating(steelSpecification.getCoating())
                                .estimatedMeterRunOnArrival(lineItem.getEstimatedMeterRunOnArrival())
                                .weightInKgsOnArrival(lineItem.getWeightInKgsOnArrival())
                                .estimatedMetersRemaining(lineItem.getEstimatedMetersRemaining())
                                .status(lineItem.getStatus())
                                .steelCoilId(lineItem.getId())
                                .cardNumber(lineItem.getCardNumber())
                                .coilNumber(lineItem.getCoilNumber())

                                .build();

                        if (steelSpecification.getGauge() != null) {
                            grvDetail.setGauge(steelSpecification.getGauge().getGauge());
                        }

                        if (steelSpecification.getWidth() != null) {
                            grvDetail.setWidth(steelSpecification.getWidth().getWidth());
                        }

                        if (color != null) {
                            grvDetail.setColor(color.getColor());
                            grvDetail.setFinish(color.getFinish().getName());
                        }
                        if (warehouse != null) {
                            grvDetail.setSteelCoilWarehouseId(warehouse.getId());
                            grvDetail.setSteelCoilWarehouse(warehouse.getName());
                        }

                        if (purchaseOrder != null && purchaseOrder.getSupplier() != null) {
                            grvDetail.setPurchaseOrderId(purchaseOrder.getId());
                            grvDetail.setSteelCoilSupplierId(purchaseOrder.getSupplier().getId());
                            grvDetail.setSteelCoilSupplierName(purchaseOrder.getSupplier().getName());
                        }
                        details.add(grvDetail);
                    }
                }
                master.setDetails(details);

                converted.add(master);
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
