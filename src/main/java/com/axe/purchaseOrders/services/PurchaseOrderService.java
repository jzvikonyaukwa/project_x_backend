package com.axe.purchaseOrders.services;

import com.axe.consumables.DTOs.ConsumableOnPurchaseOrderPostDTO;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrderService;
import com.axe.grvs.GRV;
import com.axe.grvs.services.GRVsService;
import com.axe.grvs.grvsDTO.SteelCoilPostDTO;
import com.axe.purchaseOrders.DTOs.*;
import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.purchaseOrders.PurchaseOrderRepository;
import com.axe.steelSpecifications.SteelSpecification;
import com.axe.steelSpecifications.SteelSpecificationService;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrder;
import com.axe.productsOnPurchaseOrder.ProductsOnPurchaseOrderService;
import com.axe.suppliers.Supplier;
import com.axe.suppliers.SuppliersService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SuppliersService suppliersService;
    private final SteelSpecificationService steelSpecificationService;
    private final ProductsOnPurchaseOrderService productsOnPurchaseOrderService;

    private final ConsumablesOnPurchaseOrderService consumablesOnPurchaseOrderService;
    private final GRVsService grvsService;
    Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                SuppliersService suppliersService,
                                SteelSpecificationService steelSpecificationService,
                                ProductsOnPurchaseOrderService productsOnPurchaseOrderService,
                                ConsumablesOnPurchaseOrderService consumablesOnPurchaseOrderService,
                                GRVsService grvsService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.suppliersService = suppliersService;
        this.steelSpecificationService = steelSpecificationService;
        this.productsOnPurchaseOrderService = productsOnPurchaseOrderService;
        this.consumablesOnPurchaseOrderService = consumablesOnPurchaseOrderService;
        this.grvsService = grvsService;
    }

    public PurchaseOrdersPaginationResponseDTO getAllPurchaseOrders(int page, int size,String filters) {

        if(size == 0){
            size = 1;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateIssued"));
        Specification<PurchaseOrder> spec = PurchaseOrderSpecification.getFilterSpecification(filters);

        Page<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll(spec,pageable);
        return new PurchaseOrdersPaginationResponseDTO(purchaseOrders.getContent(), purchaseOrders.getTotalElements());
    }

    public List<PurchaseOrder> getAllPurchaseOrdersOnOrder() {
        return purchaseOrderRepository.findAllByStatusOrdered();
    }

    public List<PurchaseOrder> getAllSuppliersPurchaseOrdersOnOrder(Long supplierId) {
        return purchaseOrderRepository.getAllSuppliersPurchaseOrdersOnOrder(supplierId);
    }

    public List<ProductsOnPurchaseOrder> updateProductsOnOrder(List<ProductDelivered> productsDelivered) {

        logger.info("GRV ID: " + productsDelivered.get(0).getGrvId());
        GRV grv = grvsService.getGRV(productsDelivered.get(0).getGrvId());

        List<ProductsOnPurchaseOrder> purchaseOrderProductsUpdated = new ArrayList<>();

        for(ProductDelivered productDelivered : productsDelivered){

            if(productDelivered.getProductOnPurchaseOrderId() != null){
                logger.info("Product on purchase order ID: {}", productDelivered.getProductOnPurchaseOrderId());

                ProductsOnPurchaseOrder prod
                        = productsOnPurchaseOrderService.getProductOnPurchaseOrderById(
                        productDelivered.getProductOnPurchaseOrderId());

                prod.setStatus("delivered");

                purchaseOrderProductsUpdated.add(productsOnPurchaseOrderService.updateProductOnPurchaseOrder(prod));

                PurchaseOrder purchaseOrder = getPurchaseOrder(productsDelivered.get(0).getPurchaseOrderId());

                if(purchaseOrder.getProductPurchases().stream().allMatch(p -> p.getStatus().equals("delivered"))){
                    purchaseOrder.setStatus("delivered");
                    purchaseOrderRepository.save(purchaseOrder);
                }
            } else if (productDelivered.getConsumableOnPurchaseOrderId() != null){

                ConsumablesOnPurchaseOrder consumableOnPurchaseOrder
                        = consumablesOnPurchaseOrderService.getConsumablesOnPurchaseOrderById(
                        productDelivered.getConsumableOnPurchaseOrderId());

                consumableOnPurchaseOrder.setGrv(grv);
                consumableOnPurchaseOrder.setStatus("delivered");
                PurchaseOrder purchaseOrder = getPurchaseOrder(productsDelivered.get(0).getPurchaseOrderId());

                if(purchaseOrder.getProductPurchases().stream().allMatch(p -> p.getStatus().equals("delivered"))){
                    purchaseOrder.setStatus("delivered");
                    purchaseOrderRepository.save(purchaseOrder);
                }
                consumablesOnPurchaseOrderService.saveNewConsumablesOnPurchaseOrder(consumableOnPurchaseOrder);
            } else {
                logger.info("Product on purchase order ID is null");
            }

        }

        return purchaseOrderProductsUpdated;
    }

    public List<StockOnOrderDetails> getStockOnOrder() {
        return purchaseOrderRepository.getStockOnOrder();
    }

    public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
        logger.info("Purchase order ID: {}", purchaseOrderId);
        return purchaseOrderRepository.findById(purchaseOrderId).orElseThrow(() -> new RuntimeException("Purchase order not found"));
    }

    @Transactional
    public PurchaseOrder updateEditedPurchaseOrder(PurchaseOrderPostDTO editedPurchaseOrder) {

        PurchaseOrder originalPurchaseOrder = getPurchaseOrder(editedPurchaseOrder.getPurchaseOrderId());

        Supplier supplier = suppliersService.getSupplierById(editedPurchaseOrder.getSupplierId());
        originalPurchaseOrder.setSupplier(supplier);
        originalPurchaseOrder.setDateIssued(editedPurchaseOrder.getDateIssued());
        originalPurchaseOrder.setExpectedDeliveryDate(editedPurchaseOrder.getExpectedDeliveryDate());
        originalPurchaseOrder.setNotes(editedPurchaseOrder.getComments());
        originalPurchaseOrder.setPaid(editedPurchaseOrder.getPaid());
        originalPurchaseOrder.setStatus(editedPurchaseOrder.getStatus());
        for(SteelCoilPostDTO productsOnPurchaseOrderDTO : editedPurchaseOrder.getProductPurchases()){

            SteelSpecification steelSpecification = steelSpecificationService.handleSteelSpecification(productsOnPurchaseOrderDTO);

            for (ProductsOnPurchaseOrder productsOnPurchaseOrder : originalPurchaseOrder.getProductPurchases()) {
                // TODO: SORT OUT
                if(productsOnPurchaseOrderDTO.getProductOnPurchaseOrderId().equals(productsOnPurchaseOrder.getId())){
                    productsOnPurchaseOrder.setPurchaseCostPerKg(productsOnPurchaseOrderDTO.getLandedCostPerKg());
                    productsOnPurchaseOrder.setWeightOrdered(productsOnPurchaseOrderDTO.getWeightOrdered());
                    productsOnPurchaseOrder.setSteelSpecification(steelSpecification);
                }
            }
        }

        boolean found;
        Iterator<ProductsOnPurchaseOrder> iterator = originalPurchaseOrder.getProductPurchases().iterator();

        while (iterator.hasNext()) {
            ProductsOnPurchaseOrder popo = iterator.next();
            found = false;

            for (SteelCoilPostDTO productsOnPurchaseOrderDTO : editedPurchaseOrder.getProductPurchases()) {
                if (popo.getId().equals(productsOnPurchaseOrderDTO.getProductOnPurchaseOrderId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                iterator.remove();
            }
        }

        boolean found2 = false;

        Iterator<ConsumablesOnPurchaseOrder> iterator2 = originalPurchaseOrder.getConsumablesOnPurchaseOrders().iterator();

        while (iterator2.hasNext()) {
            ConsumablesOnPurchaseOrder consumablesOnPurchaseOrder1 = iterator2.next();

            for (ConsumableOnPurchaseOrderPostDTO copo : editedPurchaseOrder.getConsumablesOnPurchaseOrder()) {
                if (copo.getConsumableOnPurchaseOrderId().equals(consumablesOnPurchaseOrder1.getId())) {
                    logger.info("Consumable on purchase order ID: " + consumablesOnPurchaseOrder1.getId() + "FOUND");
                    found2 = true;
                    consumablesOnPurchaseOrder1.setQty(copo.getQty());
                    consumablesOnPurchaseOrder1.setCostPerUnit(copo.getCostPerUnit());
                    break;
                }
            }

            if (!found2) {
                iterator2.remove();
            }
        }

        return purchaseOrderRepository.save(originalPurchaseOrder);
    }

//    public void deletePurchaseOrder(Long purchaseOrderId) {
//        purchaseOrderRepository.deleteById(purchaseOrderId);
//    }

    public PurchaseOrder cancelPurchaseOrder(Long poId) {

        if(poId == null){
            throw new RuntimeException("Purchase order ID is null");
        }

        PurchaseOrder originalPurchaseOrder = getPurchaseOrder(poId);

        for(ProductsOnPurchaseOrder productsOnPurchaseOrder : originalPurchaseOrder.getProductPurchases()){
            productsOnPurchaseOrder.setStatus("cancelled");
            productsOnPurchaseOrderService.updateProductOnPurchaseOrder(productsOnPurchaseOrder);
        }

        for(ConsumablesOnPurchaseOrder consumablesOnPurchaseOrder : originalPurchaseOrder.getConsumablesOnPurchaseOrders()){
            consumablesOnPurchaseOrder.setStatus("cancelled");
            consumablesOnPurchaseOrderService.saveNewConsumablesOnPurchaseOrder(consumablesOnPurchaseOrder);
        }

        originalPurchaseOrder.setStatus("cancelled");
        return purchaseOrderRepository.save(originalPurchaseOrder);
    }

    public List<PurchaseOrder> getPurchaseOrdersForSupplier(Long supplierId) {
        return purchaseOrderRepository.getPurchaseOrdersForSupplier(supplierId);
    }

    public PurchaseOrder changePurchaseOrderStatus(Long id, String status) {
        PurchaseOrder purchaseOrder = getPurchaseOrder(id);
        purchaseOrder.setStatus(status);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public void deletePurchaseOrder(Long id) {
        purchaseOrderRepository.deleteById(id);
    }
}
