package com.axe.purchaseOrders.services;

import com.axe.consumables.DTOs.ConsumableOnPurchaseOrderPostDTO;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.purchaseOrders.DTOs.PurchaseOrderPostDTO;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.purchaseOrders.PurchaseOrderRepository;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.suppliers.Supplier;
import com.axe.suppliers.SuppliersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EditPurchaseOrder {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SuppliersService suppliersService;
    private final SteelSpecificationService steelSpecificationService;

    Logger logger = LoggerFactory.getLogger(EditPurchaseOrder.class);

    public EditPurchaseOrder(PurchaseOrderService purchaseOrderService,
                             PurchaseOrderRepository purchaseOrderRepository,
                             SuppliersService suppliersService,
                             SteelSpecificationService steelSpecificationService) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.suppliersService = suppliersService;
        this.steelSpecificationService = steelSpecificationService;
    }

    public PurchaseOrder updateEditedPurchaseOrder(PurchaseOrderPostDTO editedPurchaseOrder) {
        // Fetch original PurchaseOrder
        PurchaseOrder originalPurchaseOrder = purchaseOrderService.getPurchaseOrder(editedPurchaseOrder.getPurchaseOrderId());
        logger.info("Just fetched the original purchase order: {}", originalPurchaseOrder.getId());
        // Update supplier and metadata
        updateMetadata(originalPurchaseOrder, editedPurchaseOrder);

        logger.info("Just updated the metadata: {}", originalPurchaseOrder);
        // Update product purchases
        updateProductPurchases(originalPurchaseOrder, editedPurchaseOrder.getProductPurchases());
        logger.info("Just updated the product purchases: {}", originalPurchaseOrder);
        // Update consumables on purchase order
        updateConsumablePurchases(originalPurchaseOrder, editedPurchaseOrder.getConsumablesOnPurchaseOrder());
        logger.info("Just updated the consumables on purchase order: {}", originalPurchaseOrder);
        // Save and return updated purchase order
        return purchaseOrderRepository.save(originalPurchaseOrder);
    }

    private void updateMetadata(PurchaseOrder original, PurchaseOrderPostDTO edited) {
        Supplier supplier = suppliersService.getSupplierById(edited.getSupplierId());
        original.setSupplier(supplier);
        original.setDateIssued(edited.getDateIssued());
        original.setExpectedDeliveryDate(edited.getExpectedDeliveryDate());
        original.setNotes(edited.getComments());
        original.setPaid(edited.getPaid());
        original.setStatus(edited.getStatus());
    }

    private void updateProductPurchases(PurchaseOrder original, List<SteelCoilPostDTO> editedProducts) {
        // Map edited products by ID for quick lookup
        Map<Long, SteelCoilPostDTO> productMap = editedProducts.stream()
                .collect(Collectors.toMap(SteelCoilPostDTO::getProductOnPurchaseOrderId, Function.identity()));

        // Remove products not in the edited list
        original.getProductPurchases().removeIf(popo -> !productMap.containsKey(popo.getId()));
        logger.info("Just removed products not in the edited list: {}", original.getProductPurchases());
        // Update or add products in the purchase order
        for (SteelCoilPostDTO productDTO : editedProducts) {
            logger.info("productDTO.getLandedCostPerKg(): {}", productDTO.getLandedCostPerKg());
            ProductsOnPurchaseOrder popo = original.getProductPurchases().stream()
                    .filter(p -> p.getId().equals(productDTO.getProductOnPurchaseOrderId()))
                    .findFirst()
                    .orElseGet(() -> {
                        ProductsOnPurchaseOrder newPopo = new ProductsOnPurchaseOrder();
                        original.getProductPurchases().add(newPopo);
                        newPopo.setPurchaseOrder(original);
                        return newPopo;
                    });

            logger.info("Just updated the product: {}", popo);
            popo.setPurchaseCostPerKg(productDTO.getPurchaseCostPerKg());
            popo.setWeightOrdered(productDTO.getWeightOrdered());
            popo.setSteelSpecification(
                    steelSpecificationService.handleSteelSpecification(productDTO)
            );
        }
    }

    private void updateConsumablePurchases(PurchaseOrder original, List<ConsumableOnPurchaseOrderPostDTO> editedConsumables) {
        // Map edited consumables by ID for quick lookup
        Map<Long, ConsumableOnPurchaseOrderPostDTO> consumableMap = editedConsumables.stream()
                .collect(Collectors.toMap(ConsumableOnPurchaseOrderPostDTO::getConsumableOnPurchaseOrderId, Function.identity()));

        // Remove consumables not in the edited list
        original.getConsumablesOnPurchaseOrders().removeIf(copo -> !consumableMap.containsKey(copo.getId()));

        // Update or add consumables in the purchase order
        for (ConsumableOnPurchaseOrderPostDTO consumableDTO : editedConsumables) {
            ConsumablesOnPurchaseOrder copo = original.getConsumablesOnPurchaseOrders().stream()
                    .filter(c -> c.getId().equals(consumableDTO.getConsumableOnPurchaseOrderId()))
                    .findFirst()
                    .orElseGet(() -> {
                        ConsumablesOnPurchaseOrder newCopo = new ConsumablesOnPurchaseOrder();
                        original.getConsumablesOnPurchaseOrders().add(newCopo);
                        return newCopo;
                    });

            copo.setQty(consumableDTO.getQty());
            copo.setCostPerUnit(consumableDTO.getCostPerUnit());
        }
    }

}
