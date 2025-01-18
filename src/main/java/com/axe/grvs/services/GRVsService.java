package com.axe.grvs.services;

import com.axe.colors.Color;
import com.axe.colors.ColorServices;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.consumableSource.SourceCountry;
import com.axe.consumableSource.SourceService;
import com.axe.consumable_category.ConsumableCategory;
import com.axe.consumable_category.ConsumableCategoryService;
import com.axe.consumables.Consumable;
import com.axe.consumables.DTOs.ConsumablePostDTO;
import com.axe.consumablesOnGrv.ConsumablesOnGrv;
import com.axe.gauges.Gauge;
import com.axe.gauges.GaugeService;
import com.axe.grvs.GRV;
import com.axe.grvs.GRVsRepository;
import com.axe.grvs.grvsDTO.*;
import com.axe.grvs.services.providers.GrvMasterDetailRowProvider;
import com.axe.steelCoils.SteelCoil;
import com.axe.utilities.ClientVisibleException;
import com.axe.warehouse.Warehouse;
import com.axe.warehouse.WarehouseService;
import com.axe.width.Width;
import com.axe.width.WidthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GRVsService {
    private final Logger logger = LoggerFactory.getLogger(GRVsService.class);

    private final GRVsRepository grvsRepository;
    private final ColorServices colorServices;
    private final GaugeService gaugeService;
    private final WidthService widthService;
    private final ConsumableCategoryService consumableCategoryService;
    private final SourceService sourceService;
    private final WarehouseService warehouseService;
    private final GrvMasterDetailRowProvider grvMasterDetailRowProvider;;

    public GRVsService(GRVsRepository grvsRepository, ColorServices colorServices,
            GaugeService gaugeService, WidthService widthService,
            ConsumableCategoryService consumableCategoryService,
            SourceService sourceService,
            WarehouseService warehouseService, GrvMasterDetailRowProvider grvMasterDetailRowProvider) {

        this.grvsRepository = grvsRepository;
        this.colorServices = colorServices;
        this.gaugeService = gaugeService;
        this.widthService = widthService;
        this.consumableCategoryService = consumableCategoryService;
        this.sourceService = sourceService;
        this.warehouseService = warehouseService;
        this.grvMasterDetailRowProvider = grvMasterDetailRowProvider;
    }

    public List<GRV> getAllGRVs() {
        return grvsRepository.getAllGRVs();
    }

    public GRV getGRV(Long id) {
        return grvsRepository.findById(id).orElse(null);
    }

    public List<GRV> getAllGRVsForSupplier(Long id) {
        return null;
        // return grvsRepository.getAllGRVsForSupplier(id);
    }

    public GRVDetailsDTO getGRVInformation(Long id) {
        List<GRVWithDetailsSQLQuery> grvInformation = grvsRepository.getGRVWithDetails(id);

        if (grvInformation.isEmpty()) {
            logger.info("Raw data is empty. Returning...");
            throw new ClientVisibleException("GRV data is empty");
        } else {
            return convertSqlResultToDtoList(grvInformation).get(0);
        }
    }

    public List<GRVDetailsDTO> convertSqlResultToDtoList(List<GRVWithDetailsSQLQuery> rawData) {

        if (rawData.isEmpty()) {
            logger.info("Raw data is empty. Returning...");
            return new ArrayList<>();
        }

        // Step 1: Grouping by GRV ID
        Map<Long, List<GRVWithDetailsSQLQuery>> groupedByGrvId = rawData.stream()
                .collect(Collectors.groupingBy(GRVWithDetailsSQLQuery::getGrvId));

        List<GRVDetailsDTO> result = new ArrayList<>();

        // Step 2: Processing each group
        groupedByGrvId.forEach((grvId, grvDetailsList) -> {
            GRVDetailsDTO grvDetailsDTO = new GRVDetailsDTO();
            GRVWithDetailsSQLQuery firstRow = grvDetailsList.get(0);

            grvDetailsDTO.setId(firstRow.getGrvId());
            grvDetailsDTO.setDateReceived(firstRow.getDateReceived());
            grvDetailsDTO.setComments(firstRow.getGrvComments());
            grvDetailsDTO.setSupplierGrvCode(firstRow.getSupplierGrvCode());

            setGrvSupplier(firstRow, grvDetailsDTO);
            setGrvWarehouse(firstRow, grvDetailsDTO);

            // Now passing the entire list related to the GRV
            handleProductsOnGRV(grvDetailsList, grvDetailsDTO);

            result.add(grvDetailsDTO);
        });

        return result;
    }

    private void setGrvWarehouse(GRVWithDetailsSQLQuery grvWithDetailsSQLQuery, GRVDetailsDTO grvDetailsDTO) {

        List<Warehouse> warehouses = warehouseService.getAllWarehouses();

        if (grvWithDetailsSQLQuery.getSteelCoilWarehouseId() != null) {
            for (Warehouse warehouse : warehouses) {
                if (warehouse.getId().equals(grvWithDetailsSQLQuery.getSteelCoilWarehouseId())) {
                    grvDetailsDTO.setWarehouse(warehouse);
                }
            }
        } else if (grvWithDetailsSQLQuery.getConsumableWarehouseId() != null) {
            for (Warehouse warehouse : warehouses) {
                if (warehouse.getId().equals(grvWithDetailsSQLQuery.getConsumableWarehouseId())) {
                    grvDetailsDTO.setWarehouse(warehouse);
                }
            }
        }
    }

    private void setGrvSupplier(GRVWithDetailsSQLQuery rawData, GRVDetailsDTO GRVDetailsDTO) {
        if (rawData.getSteelCoilSupplierId() != null) {
            GRVDetailsDTO.setSupplierId(rawData.getSteelCoilSupplierId());
        } else {
            GRVDetailsDTO.setSupplierId(rawData.getConsumableSupplierId());
        }
    }

    private void handleProductsOnGRV(List<GRVWithDetailsSQLQuery> rawData, GRVDetailsDTO GRVDetailsDTO) {

        List<SteelCoilPostDTO> steelCoilsList = new ArrayList<>();
        List<ConsumablePostDTO> consumablesList = new ArrayList<>();

        List<Color> colors = colorServices.getAllColors();
        List<Gauge> gauges = gaugeService.getAllGauges();
        List<Width> widths = widthService.getAllWidths();
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();

        List<ConsumableCategory> consumableCategories = consumableCategoryService.getAllConsumableCategories();
        List<SourceCountry> consumableSourceCountries = sourceService.getAllSources();

        for (GRVWithDetailsSQLQuery row : rawData) {
            logger.trace("gauge: " + row.getGauge());
            if (row.getSteelCoilId() != null) {
                SteelCoilPostDTO steelCoilDto = createSteelCoilFromRow(row, colors, gauges, widths, warehouses);
                steelCoilsList.add(steelCoilDto);
                GRVDetailsDTO.setWarehouse(steelCoilDto.getWarehouse());
            }
            if (row.getConsumableId() != null) {
                ConsumablePostDTO consumableDto = new ConsumablePostDTO();
                // consumableDto.setPurchaseOrderId(row.getPurchaseOrderId());
                consumableDto.setConsumableOnGrvId(row.getConsumableOnGrvId());
                consumableDto.setConsumableInWarehouseId(row.getConsumableInWarehouseId());
                consumableDto.setQtyOrdered(row.getQtyOrdered());
                consumableDto.setLandedPrice(row.getLandedPrice());
                setConsumableWarehouse(row, consumableDto, warehouses);
                consumableDto.setConsumable(
                        createConsumableFromRow(row, consumableCategories, consumableSourceCountries));
                consumablesList.add(consumableDto);
            }
        }

        GRVDetailsDTO.setSteelCoils(steelCoilsList);
        GRVDetailsDTO.setConsumablesOnGrv(consumablesList);
    }

    private void setConsumableWarehouse(GRVWithDetailsSQLQuery row, ConsumablePostDTO consumableDto,
            List<Warehouse> warehouses) {

        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName().equals(row.getConsumableWarehouse())) {
                consumableDto.setWarehouse(warehouse);
            }
        }
    }

    private Consumable createConsumableFromRow(GRVWithDetailsSQLQuery row,
            List<ConsumableCategory> consumableCategories,
            List<SourceCountry> consumableSourceCountries) {
        // create consumable from row
        Consumable consumable = new Consumable();
        consumable.setId(row.getConsumableId());
        consumable.setSerialNumber(row.getSerialNumber());
        consumable.setName(row.getConsumableName());
        consumable.setUom(row.getUom());

        logger.info("Consumable category id: " + row.getConsumableCategoryId());
        for (ConsumableCategory consumableCategory : consumableCategories) {
            if (consumableCategory.getId().equals(row.getConsumableCategoryId())) {
                logger.info("Consumable category found: ");
            }
        }

        for (SourceCountry sourceCountry : consumableSourceCountries) {
            if (sourceCountry.getId().equals(row.getConsumableCategoryId())) {
                consumable.setSourceCountry(sourceCountry);
            }
        }

        return consumable;
    }

    private SteelCoilPostDTO createSteelCoilFromRow(GRVWithDetailsSQLQuery row, List<Color> colors,
            List<Gauge> gauges, List<Width> widths, List<Warehouse> warehouses) {

        SteelCoilPostDTO steelCoilDto = new SteelCoilPostDTO();
        steelCoilDto.setSteelCoilId(row.getSteelCoilId());
        // steelCoilDto.setPurchaseOrderId(row.getPurchaseOrderId());
        steelCoilDto.setCoilNumber(row.getCoilNumber());
        steelCoilDto.setCardNumber(row.getCardNumber());
        steelCoilDto.setStatus(row.getStatus());
        steelCoilDto.setIsqGrade(row.getIsqGrade());
        steelCoilDto.setCoating(row.getCoating());
        steelCoilDto.setWeightInKgsOnArrival(row.getWeightInKgsOnArrival());
        steelCoilDto.setEstimatedMeterRunOnArrival(row.getEstimatedMeterRunOnArrival());
        steelCoilDto.setEstimatedMetersRemaining(row.getEstimatedMetersRemaining());
        steelCoilDto.setLandedCostPerMtr(row.getLandedCostPerMtr());

        for (Color color : colors) {
            if (color.getColor().equals(row.getColor())) {
                steelCoilDto.setColor(color);
            }
        }

        for (Gauge gauge : gauges) {
            if (gauge.getGauge().compareTo(row.getGauge()) == 0) {
                steelCoilDto.setGauge(gauge);
            }
        }

        // added a null check
        for (Width width : widths) {
            if (width.getWidth() != null && width.getWidth().equals(row.getWidth())) {
                steelCoilDto.setWidth(width);
            }
        }

        for (Warehouse warehouse : warehouses) {
            if (warehouse.getName().equals(row.getSteelCoilWarehouse())) {
                steelCoilDto.setWarehouse(warehouse);
            }
        }

        return steelCoilDto;
    }

    public List<GRVDetailsDTO> getAllGRVForPO(Long id) {

        logger.info("Getting all GRVs for PO: {}", id);
        List<GRVWithDetailsSQLQuery> grvInformation = grvsRepository.getAllGRVForPO(id);
        logger.info("GRV information: {}", grvInformation);
        return convertSqlResultToDtoList(grvInformation);
    }

    @Transactional(readOnly = true)
    public List<GRVTotalDTO> getLatestGRVTotals(int numberOfRecentGRVs) {
        logger.debug("Getting latest {} GRV totals", numberOfRecentGRVs);
        try {
            Pageable pageRequest = PageRequest.of(0, numberOfRecentGRVs, Sort.by(Sort.Direction.DESC, "id"));
            List<GRV> latestGRVs = grvsRepository.findAll(pageRequest).toList();
            logger.debug(" - GRVS found: {}", latestGRVs.size());

            // Calc total for each GRV, returning total value
            List<GRVTotalDTO> grvTotals = latestGRVs.stream().map(grv -> {
                logger.debug("   - grv - {}", grv.getId());

                BigDecimal totalForSteelCoils = BigDecimal.ZERO;
                BigDecimal totalForConsumables = BigDecimal.ZERO;

                List<ConsumablesOnGrv> consumables = grv.getConsumablesOnGrv();

                for (ConsumablesOnGrv consumable : consumables) {
                    BigDecimal totalForConsumable = consumable.getLandedPrice()
                            .multiply(new BigDecimal(consumable.getQtyReceived()));
                    totalForConsumables = totalForConsumables.add(totalForConsumable);
                }
                logger.debug("   - totalForConsumables - {}", totalForConsumables);

                List<SteelCoil> steelCoils = grv.getSteelCoils();

                for (SteelCoil coil : steelCoils) {
                    BigDecimal coilTotal = coil.getEstimatedMeterRunOnArrival().multiply(coil.getLandedCostPerMtr());
                    totalForSteelCoils = totalForSteelCoils.add(coilTotal);
                }
                logger.debug("   - totalForSteelCoils - {}", totalForSteelCoils);

                double roundedTotalForGRV = totalForSteelCoils.add(totalForConsumables).doubleValue();
                logger.debug("   - roundedTotalForGRV - {}", roundedTotalForGRV);

                return new GRVTotalDTO(grv.getId(), grv.getDateReceived(), roundedTotalForGRV);
            }).toList();

            return grvTotals;
        } catch (DataAccessException e) {
            logger.error(e.getMostSpecificCause().getLocalizedMessage());
            throw new RuntimeException("Data access error - Failed to get latest GRV totals", e);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            throw new RuntimeException("Unknown error - Failed to get latest GRV totals", e);
        }
    }

    @Transactional(readOnly = true)
    public ServerSideGetRowsResponse<List<GRVMasterDetailDTO>> fetchGRVMasterDetailGridRows(
            ServerSideGetRowsRequest request) {
        logger.debug("Getting grvs details for request: {}", request);
        try {
            return grvMasterDetailRowProvider.getRows(request);
        } catch (Exception e) {
            logger.error("Error in getting list grvs details: ", e);
            throw new RuntimeException("Error in getting list grvs details: ", e);
        }
    }
}
