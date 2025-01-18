package com.axe.purchaseOrders.services;

import com.axe.consumables.Consumable;
import com.axe.consumables.ConsumablesService;
import com.axe.consumables.DTOs.ConsumableOnPurchaseOrderPostDTO;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.purchaseOrders.DTOs.PurchaseOrderPostDTO;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.purchaseOrders.PurchaseOrderRepository;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.suppliers.Supplier;
import com.axe.suppliers.SuppliersService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderAddService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SuppliersService suppliersService;
    private final SteelSpecificationService steelSpecificationService;
    private final ConsumablesService consumablesService;
    Logger logger = LoggerFactory.getLogger(PurchaseOrderAddService.class);

    public PurchaseOrderAddService(PurchaseOrderRepository purchaseOrderRepository,
                                   SuppliersService suppliersService,
                                   SteelSpecificationService steelSpecificationService,
                                   ConsumablesService consumablesService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.suppliersService = suppliersService;
        this.steelSpecificationService = steelSpecificationService;
        this.consumablesService = consumablesService;
    }

    @Transactional
    public PurchaseOrder addPurchaseOrder(PurchaseOrderPostDTO purchaseOrderPostDTO) {

        Supplier supplier = suppliersService.getSupplierById(purchaseOrderPostDTO.getSupplierId());

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setDateIssued(purchaseOrderPostDTO.getDateIssued());
        purchaseOrder.setExpectedDeliveryDate(purchaseOrderPostDTO.getExpectedDeliveryDate());
        purchaseOrder.setNotes(purchaseOrderPostDTO.getComments());
        purchaseOrder.setStatus(purchaseOrderPostDTO.getStatus());
        purchaseOrder.setPaid(purchaseOrderPostDTO.getPaid());
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        logger.info("Just saved the purchaseOrder: " + purchaseOrder);

        if(purchaseOrderPostDTO.getProductPurchases() != null && !purchaseOrderPostDTO.getProductPurchases().isEmpty()){
            handleSteelCoilsOnPurchaseOrder(purchaseOrderPostDTO, purchaseOrder);
            logger.info("Just saved the steel on the purchaseOrder: {}", purchaseOrder);
        }

        if(purchaseOrderPostDTO.getConsumablesOnPurchaseOrder() != null && !purchaseOrderPostDTO.getConsumablesOnPurchaseOrder().isEmpty()){
            handleConsumablesOnPurchaseOrder(purchaseOrderPostDTO, purchaseOrder);
            logger.info("Just saved the consumables on the purchaseOrder: " + purchaseOrder);
        }

        return saveNewPurchaseOrder(purchaseOrder);
    }

    private void handleSteelCoilsOnPurchaseOrder(PurchaseOrderPostDTO purchaseOrderPostDTO, PurchaseOrder purchaseOrder) {
        for (SteelCoilPostDTO productsOnOrderDTO : purchaseOrderPostDTO.getProductPurchases()) {
            SteelSpecification steelSpecification
                    = steelSpecificationService.handleSteelSpecification(productsOnOrderDTO);
            logger.info("Steel Specification: {}", steelSpecification);
            logger.info("Steel Specification ID: {}", steelSpecification.getId());
            ProductsOnPurchaseOrder productsOnPurchaseOrder = new ProductsOnPurchaseOrder();
            logger.info("productsOnOrderDTO purchase cost per kg: {}", productsOnOrderDTO.getLandedCostPerKg());
            productsOnPurchaseOrder.setPurchaseOrder(purchaseOrder);
            productsOnPurchaseOrder.setPurchaseCostPerKg(productsOnOrderDTO.getPurchaseCostPerKg());
            productsOnPurchaseOrder.setWeightOrdered(productsOnOrderDTO.getWeightOrdered());
            productsOnPurchaseOrder.setSteelSpecification(steelSpecification);
            productsOnPurchaseOrder.setStatus("pending");

            purchaseOrder.getProductPurchases().add(productsOnPurchaseOrder);
            steelSpecification.getProductsOnPurchaseOrders().add(productsOnPurchaseOrder);
            steelSpecification = steelSpecificationService.saveNewProductType(steelSpecification);
            logger.info("Saved new ss: " +  steelSpecification);
        }
    }

    private void handleConsumablesOnPurchaseOrder(PurchaseOrderPostDTO purchaseOrderPostDTO, PurchaseOrder purchaseOrder) {

        for (ConsumableOnPurchaseOrderPostDTO consumableOnPurchaseOrderPostDTO : purchaseOrderPostDTO.getConsumablesOnPurchaseOrder()) {
            if(consumableOnPurchaseOrderPostDTO.getConsumableId() == null){
                throw new RuntimeException("Consumable ID is null");
            }

            Consumable consumable = consumablesService.getConsumableById(consumableOnPurchaseOrderPostDTO.getConsumableId());
            ConsumablesOnPurchaseOrder consumablesOnPurchaseOrder = new ConsumablesOnPurchaseOrder();
            consumablesOnPurchaseOrder.setPurchaseOrder(purchaseOrder);
            consumablesOnPurchaseOrder.setConsumable(consumable);
            consumablesOnPurchaseOrder.setStatus("pending");
            consumablesOnPurchaseOrder.setQty(consumableOnPurchaseOrderPostDTO.getQty());
            consumablesOnPurchaseOrder.setCostPerUnit(consumableOnPurchaseOrderPostDTO.getCostPerUnit());

            purchaseOrder.getConsumablesOnPurchaseOrders().add(consumablesOnPurchaseOrder);
        }
    }

    private PurchaseOrder saveNewPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }
}
