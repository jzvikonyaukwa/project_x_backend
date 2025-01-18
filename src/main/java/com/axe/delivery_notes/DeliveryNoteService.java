package com.axe.delivery_notes;

import com.axe.clients.Client;
import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;
import com.axe.consumables.Consumable;
import com.axe.consumables.ConsumablesService;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.consumablesOnQuote.ConsumablesOnQuoteService;
import com.axe.credit_note.exceptions.ResourceNotFoundException;

import com.axe.delivery_notes.delivery_notesDTOs.*;

import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.DeliveryNoteInventoryDto;
import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.DeliveryNoteResponse;
import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.InventoryResponse;
import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.ProductResponse;
import com.axe.delivery_notes.providers.DeliveryNoteGetAllDtoRowProvider;
import com.axe.inventories.Inventory;
import com.axe.inventories.InventoryService;
import com.axe.inventories.inventoryDTOs.InvertoryNewDto;
import com.axe.product.services.ProductService;
import com.axe.product_type.ProductType;
import com.axe.projects.Project;
import com.axe.projects.ProjectService;
import com.axe.projects.projectDTO.project_overviewDTO.ClientDTO;
import com.axe.projects.projectDTO.project_overviewDTO.ProjectDTO;
import com.axe.quotes.Quote;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

//import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class DeliveryNoteService {
    private static final Logger logger = LoggerFactory.getLogger(DeliveryNoteService.class);
    @PersistenceContext
    private EntityManager entityManager;

    private final DeliveryNoteRepository deliveryNoteRepository;
    private final InventoryService inventoryService;
    private final ProjectService projectService;
    private  final ProductService productService;
    private final  ConsumablesService consumablesService;
    private final ConsumablesOnQuoteService consumablesOnQuoteService;
    private final DeliveryNoteGetAllDtoRowProvider deliveryNoteGetAllDtoRowProvider;


    public DeliveryNoteService(DeliveryNoteRepository deliveryNoteRepository, InventoryService inventoryService, ProjectService projectService,
                               ProductService productService,
                               ConsumablesService consumablesService,
                               ConsumablesOnQuoteService consumablesOnQuoteService,
                               DeliveryNoteGetAllDtoRowProvider deliveryNoteGetAllDtoRowProvider
    ) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.inventoryService = inventoryService;
        this.projectService = projectService;
        this.productService =productService;
        this.consumablesService = consumablesService;
        this.consumablesOnQuoteService = consumablesOnQuoteService;
        this.deliveryNoteGetAllDtoRowProvider = deliveryNoteGetAllDtoRowProvider;

    }

    public List<DeliveryNoteGetAllDto> getAllDeliveryNotes() {
        logger.debug("Getting all delivery notes");

        long rowsFound = deliveryNoteRepository.count();
        Pageable pageable = PageRequest.of(0, (int) rowsFound, Sort.by(Sort.Direction.DESC, "id"));
        List<DeliveryNote> matchingRecords = deliveryNoteRepository.findAll(pageable).getContent();

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
        return converted;
    }

    public List<DeliveryNoteResponse> getDeliveryNotesByProjectId(Long projectId) {
        List<DeliveryNoteInventoryDto> results = deliveryNoteRepository.findByProjectId(projectId);
        Map<Long, DeliveryNoteResponse> deliveryNoteMap = new HashMap<>();

        for (DeliveryNoteInventoryDto result : results) {
            DeliveryNoteResponse deliveryNote = deliveryNoteMap.computeIfAbsent(result.getDeliveryNoteId(), id -> createDeliveryNoteResponse(result));
            InventoryResponse inventory = createInventoryResponse(result);

            if (result.getManufacturedId() != null) {
                inventory.setProduct(createProductResponse(result));
            }

            if (result.getConsumableId() != null) {
                inventory.setConsumable(createConsumableOnQuote(result));
            }

            deliveryNote.getInventories().add(inventory);
        }

        return new ArrayList<>(deliveryNoteMap.values());
    }

    private DeliveryNoteResponse createDeliveryNoteResponse(DeliveryNoteInventoryDto result) {
        DeliveryNoteResponse deliveryNote = new DeliveryNoteResponse();
        deliveryNote.setId(result.getDeliveryNoteId());
        deliveryNote.setDateCreated(result.getDateCreated());
        deliveryNote.setDateDelivered(result.getDateDelivered());
        deliveryNote.setDeliveryAddress(result.getDeliveryAddress());
        deliveryNote.setStatus(Status.fromValue(result.getStatus()));
        deliveryNote.setInventories(new ArrayList<>());
        return deliveryNote;
    }

    private InventoryResponse createInventoryResponse(DeliveryNoteInventoryDto result) {
        InventoryResponse inventory = new InventoryResponse();
        inventory.setId(result.getInventoryId());
        inventory.setDateIn(result.getInventoryDateIn());
        inventory.setDateOut(result.getInventoryDateOut());
        return inventory;
    }

    private ProductResponse createProductResponse(DeliveryNoteInventoryDto result) {
        ProductResponse product = new ProductResponse();
        product.setId(result.getInventoryId());
        product.setCode(result.getCode());
        product.setFrameName(result.getFrameName());
        product.setTotalLength(result.getTotalLength());
        product.setFrameType(result.getFrameType());
        product.setStatus(result.getStatus());
        product.setTotalQuantity(result.getManufacturedQuantity());
        product.setProductName(result.getProductName());
        ProductType productType = new ProductType();
        productType.setName(result.getProductName());
        product.setProductType(productType);
        return product;
    }

    private ConsumableOnQuote createConsumableOnQuote(DeliveryNoteInventoryDto result) {
        Consumable consumable = consumablesService.getConsumableById(result.getConsumableId());
        ConsumableOnQuote consumableOnQuote = new ConsumableOnQuote();
        consumableOnQuote.setId(result.getInventoryId());
        consumableOnQuote.setQty(result.getQuantity());
        consumableOnQuote.setSellPrice(result.getPrice());
        consumableOnQuote.setConsumable(consumable);
        return consumableOnQuote;
    }
    public DeliveryNoteNewDto getDeliveryNoteById(Long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + id));
        DeliveryNoteNewDto deliveryNoteDto = new DeliveryNoteNewDto();
        deliveryNoteDto.setId(deliveryNote.getId());
        deliveryNoteDto.setDateCreated(deliveryNote.getDateCreated());
        deliveryNoteDto.setDateDelivered(deliveryNote.getDateDelivered());
        deliveryNoteDto.setDeliveryAddress(deliveryNote.getDeliveryAddress());
        deliveryNoteDto.setStatus(deliveryNote.getStatus());
        ClientDTO clientDTO = new ClientDTO(deliveryNote.getProject().getClient().getId(),
                deliveryNote.getProject().getName());
        ProjectDTO projectDTO = new ProjectDTO(deliveryNote.getProject().getId(), deliveryNote.getProject().getName(),
                clientDTO);
        deliveryNoteDto.setProject(projectDTO);
        List<InvertoryNewDto> invertoryNewDtos = deliveryNote.getInventories().stream().map(inventory -> {
            InvertoryNewDto invertoryDto = new InvertoryNewDto();
            invertoryDto.setId(inventory.getId());
            invertoryDto.setDateIn(inventory.getDateIn());
            invertoryDto.setDateOut(inventory.getDateOut());
            invertoryDto.setConsumable(inventory.getConsumable());
            invertoryDto.setProduct(inventory.getProduct());
            invertoryDto.setReturnedProducts(inventory.getReturnedProducts());
            invertoryDto.setDeliveryNote(inventory.getDeliveryNote());

            if (inventory.getProduct() != null ) {
                invertoryDto.setColor(inventory.getProduct().getColor());
            }
            if (inventory.getProduct() != null ) {
                invertoryDto.setWidth(inventory.getProduct().getWidth());
            }
            if (inventory.getProduct() != null ) {
                invertoryDto.setGauge(inventory.getProduct().getGauge());
            }
//            if (inventory.getProduct() != null) {
//                invertoryDto.setProfile(inventory.getProduct().getProfile());
//            }
            return invertoryDto;
        }).toList();

        deliveryNoteDto.setInventories(invertoryNewDtos);
        return deliveryNoteDto;
    }

    @Transactional
    public DeliveryNote createDeliveryNote(DeliveryNoteDTO deliveryNoteDTO) {
        DeliveryNote newDeliveryNote = new DeliveryNote();

        newDeliveryNote.setDeliveryAddress(deliveryNoteDTO.getDeliveryAddress());
        newDeliveryNote.setStatus(Status.DELIVERED);

        Project project = projectService.getProjectById(deliveryNoteDTO.getProjectId());

        if (project == null) {
            throw new RuntimeException("Project not found for id :: " + deliveryNoteDTO.getProjectId());
        }

        logger.info("Found the project: {} ", project.getId());
        newDeliveryNote.setProject(project);

        List<Long> manufacturedProductIds = deliveryNoteDTO.getManufacturedProductsID();
        List<Long> consumableIds = deliveryNoteDTO.getConsumablesIdsQuantities()
                .stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toList());
        List<Map<Long,Long>> consumablesQuantities =deliveryNoteDTO.getConsumablesIdsQuantities();


        List<Inventory> inventories = inventoryService.getInventoriesByIds(manufacturedProductIds);
        inventories.addAll(inventoryService.getInventoriesByIds(consumableIds));

        Map<Long, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getId, Function.identity()));

        List<Inventory> managedMPInventories = manufacturedProductIds.stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryMap.get(inventoryId);
                    if (inventory == null) {
                        throw new RuntimeException("Inventory not found for id :: " + inventoryId);
                    }

//                    if (!"completed".equalsIgnoreCase(inventory.getManufacturedProduct().getStatus())) {
//                        throw new RuntimeException("Manufactured product is not completed yet :: " + inventoryId);
//                    }

                    inventory.setDateOut(LocalDate.now());
                    inventory.setDeliveryNote(newDeliveryNote);
                    return inventory;
                })
                .collect(Collectors.toList());
        List<Inventory> managedConsumableInventory = consumableIds.stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryMap.get(inventoryId);
                    if (inventory == null) {
                        throw new RuntimeException("Inventory not found for id :: " + inventoryId);
                    }

                    inventory.setDateOut(LocalDate.now());
                    inventory.setDeliveryNote(newDeliveryNote);
                    return inventory;
                })
                .collect(Collectors.toList());

        // Handle partial inventory
        if(deliveryNoteDTO.getConsumablesIdsQuantities() != null){
            HandlePartialInventory(deliveryNoteDTO.getConsumablesIdsQuantities());
        }
        managedMPInventories.addAll(managedConsumableInventory);

        newDeliveryNote.setInventories(managedMPInventories);

        if (newDeliveryNote.getDateCreated() == null) {
            newDeliveryNote.setDateCreated(LocalDate.now());
        }
        if (newDeliveryNote.getDateDelivered() == null) {
            newDeliveryNote.setDateDelivered(LocalDate.now());
        }
        return deliveryNoteRepository.save(newDeliveryNote);
    }

    // To Handle Partial Update
    private void HandlePartialInventory(List<Map<Long,Long>> consumablesQuantities){
        for (Map<Long, Long> consumableMap : consumablesQuantities) {
            for (Map.Entry<Long, Long> entry : consumableMap.entrySet()) {
                Long inventoryId = entry.getKey();
                Long quantityToSubtract = entry.getValue();

                Long consumableId= inventoryService.getInventoryById(inventoryId).map(inventory1 -> inventory1.getConsumable().getId()).get();
                ConsumableOnQuote consumableOnQuote = consumablesOnQuoteService
                        .getConsumableOnQuoteById(consumableId);
                // Subtract the quantity
                int remainingQuantity = consumableOnQuote.getQty()- quantityToSubtract.intValue();
                if (remainingQuantity < 0) {
                    throw new RuntimeException("Insufficient inventory for id :: " + consumableOnQuote);
                }
                if(remainingQuantity !=0) {
                    ConsumableOnQuote consumableOnQuoteNew = createConsumableOnQuotesRemainingQuantities(consumableOnQuote, remainingQuantity);
                    // Create new entry of remaining
                    createInventoryRemainingConsumableOnQuotes(consumableOnQuoteNew);
                }
                //leave the original updated
                consumableOnQuote.setQty(quantityToSubtract.intValue());


            }
        }
    }

    @Transactional
    public ConsumableOnQuote createConsumableOnQuotesRemainingQuantities( ConsumableOnQuote consumableOnQuote, Integer remainingQuantity) {
        ConsumableOnQuote newConsumableOnQuote =ConsumableOnQuote
                .builder()
                .qty(remainingQuantity)
                .sellPrice(consumableOnQuote.getSellPrice())
                .consumable(consumableOnQuote.getConsumable())
                .quote(consumableOnQuote.getQuote())
                .invoice(consumableOnQuote.getInvoice())
                .build();
        return consumablesOnQuoteService.saveConsumableOnQuote(newConsumableOnQuote);
    }

    public void createInventoryRemainingConsumableOnQuotes(ConsumableOnQuote consumableOnQuote){
        // Create an inventory of the new entry
        Inventory inventoryRemainingConsumableOnQuotes = Inventory.builder()
                .dateIn(LocalDate.now())
                .build();
                inventoryRemainingConsumableOnQuotes.setConsumable(consumableOnQuote);
        inventoryService.createInventory(inventoryRemainingConsumableOnQuotes);
    }


    public DeliveryNote updateDeliveryNoteStatus(Long id, String status) {
        DeliveryNote existingDeliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryNote not found with id " + id));

        existingDeliveryNote.setStatus(Status.valueOf(status));

        return deliveryNoteRepository.save(existingDeliveryNote);
    }

    public DeliveryNote updateDeliveryNote(Long id, DeliveryNote deliveryNoteDetails) {

        return deliveryNoteRepository.findById(id).map(existingDeliveryNote -> {
            existingDeliveryNote.setDateCreated(deliveryNoteDetails.getDateCreated());
            existingDeliveryNote.setDateDelivered(deliveryNoteDetails.getDateDelivered());
            existingDeliveryNote.setStatus(deliveryNoteDetails.getStatus());
            existingDeliveryNote.setInventories(deliveryNoteDetails.getInventories());
//            existingDeliveryNote.setConsumablesOnQuotes(deliveryNoteDetails.getConsumablesOnQuotes());
//            existingDeliveryNote.setReturnedProducts(deliveryNoteDetails.getReturnedProducts());
            return deliveryNoteRepository.save(existingDeliveryNote);
        }).orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + id));
    }

    public void deleteDeliveryNote(Long id) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + id));
        deliveryNoteRepository.delete(deliveryNote);
    }

    @Transactional
    public void removeItemsFromDeliveryNote(DeliveryNoteRemoveItemsDTO removeItemsDTO) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(removeItemsDTO.getDeliveryNoteId())
                .orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + removeItemsDTO.getDeliveryNoteId()));

        List<Inventory> removeMPInventories = removeItemsDTO.getRemoveManufacturedProductsID().stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryService.getInventoryById(inventoryId)
                            .orElseThrow(() -> new RuntimeException("Inventory not found for id :: " + inventoryId));

                    inventory.setDateOut(null);
                    inventory.setDeliveryNote(null);
                    return inventory;
                })
                .toList();

        List<Inventory> removeConsumableInventory = removeItemsDTO.getRemoveConsumablesOnQuoteID().stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryService.getInventoryById(inventoryId)
                            .orElseThrow(() -> new RuntimeException("Inventory not found for id :: " + inventoryId));

                    inventory.setDateOut(null);
                    inventory.setDeliveryNote(null);
                    return inventory;
                })
                .toList();

    }

    @Transactional
    public void addItemsToDeliveryNote(DeliveryNoteAddItemsDTO addItemsDTO) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(addItemsDTO.getDeliveryNoteId())
                .orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + addItemsDTO.getDeliveryNoteId()));

        List<Inventory> addMPInventories = addItemsDTO.getAddManufacturedProductsID().stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryService.getInventoryById(inventoryId)
                            .orElseThrow(() -> new RuntimeException("Inventory not found for id :: " + inventoryId));

                    inventory.setDateOut(LocalDate.now()); // Set the dateOut to current date or a specific date
                    inventory.setDeliveryNote(deliveryNote);
                    return inventory;
                })
                .toList();

        List<Inventory> addConsumableInventory = addItemsDTO.getAddConsumablesOnQuoteID().stream()
                .map(inventoryId -> {
                    Inventory inventory = inventoryService.getInventoryById(inventoryId)
                            .orElseThrow(() -> new RuntimeException("Inventory not found for id :: " + inventoryId));

                    inventory.setDateOut(LocalDate.now()); // Set the dateOut to current date or a specific date
                    inventory.setDeliveryNote(deliveryNote);
                    return inventory;
                })
                .toList();

        // Save the updated inventory items
        inventoryService.saveAll(addMPInventories);
        inventoryService.saveAll(addConsumableInventory);
    }


    public List<Inventory> getInventoriesByDeliveryNoteId(Long deliveryNoteId) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(deliveryNoteId)
        .orElseThrow(() -> new RuntimeException("DeliveryNote not found for this id :: " + deliveryNoteId));
        return deliveryNote.getInventories();
    }

//    public List<DeliveryNoteHasConsumablesOnQuote> getConsumablesOnQuotesByDeliveryNoteId(Long deliveryNoteId) {
//        DeliveryNote deliveryNote = getDeliveryNoteById(deliveryNoteId);
//        return deliveryNote.getConsumablesOnQuotes();
//    }
//
//    public List<ReturnedProducts> getReturnedProductsByDeliveryNoteId(Long deliveryNoteId) {
//        DeliveryNote deliveryNote = getDeliveryNoteById(deliveryNoteId);
//        return deliveryNote.getReturnedProducts();
//    }

    public Client getClientInfoByDeliveryNoteId(Long id) {
        Optional<DeliveryNote> deliveryNote = deliveryNoteRepository.findById(id);
        return deliveryNote.map(dn -> dn.getProject().getClient()).orElse(null);
    }

    public Quote getQuoteFromDeliveryNote(Long deliveryNoteId) {
        DeliveryNote deliveryNote = entityManager.find(DeliveryNote.class, deliveryNoteId);
        if (deliveryNote != null) {
            Project project = deliveryNote.getProject();
            if (project != null) {
                List<Quote> quotes = project.getQuotes();
                // Assuming you only expect one quote per project
                if (!quotes.isEmpty()) {
                    return quotes.get(0);
                }
            }
        }
        return null;
    }

    public boolean isEditable(Long deliveryNoteId) {
        DeliveryNote deliveryNote = deliveryNoteRepository.findById(deliveryNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery Note not found"));

        LocalDate now = LocalDate.now();
        LocalDate createdAt = deliveryNote.getDateCreated();

        // Calculate the difference in days
        long differenceInDays =  1;//ChronoUnit.DAYS.between(createdAt, now);

        return differenceInDays <= 1;
    }

    public List<DeliveryNoteItemDTO> getDeliveryNoteItemsForUpdate(@PathVariable Long deliveryNoteId) {
        return deliveryNoteRepository.getDeliveryNoteItemsForUpdate(deliveryNoteId);
    }

    public ProjectInformation getProjectInfoByDeliveryNoteId(Long id) {
        return (ProjectInformation) deliveryNoteRepository.getProjectInfoByDeliveryNoteId(id);
    }

    @Transactional(readOnly = true)
    public ServerSideGetRowsResponse<List<DeliveryNoteGetAllDto>> fetchDeliveryNoteGetAllDtoGridRows(
        ServerSideGetRowsRequest request) {
        logger.debug("Getting delivery notes for request: {}", request);
        try {
            return deliveryNoteGetAllDtoRowProvider.getRows(request);
        } catch (Exception e) {
            logger.error("Error in getting delivery notes: ", e);
            throw new RuntimeException("Error in getting delivery notes: ", e);
        }
    }
}
