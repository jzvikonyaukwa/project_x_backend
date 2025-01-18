package com.axe.consumablesOnPurchaseOrder;

import org.springframework.stereotype.Service;

@Service
public class ConsumablesOnPurchaseOrderService {
    private final ConsumablesOnPurchaseOrderRepository consumablesOnPurchaseOrderRepository;

    public ConsumablesOnPurchaseOrderService(ConsumablesOnPurchaseOrderRepository consumablesOnPurchaseOrderRepository) {
        this.consumablesOnPurchaseOrderRepository = consumablesOnPurchaseOrderRepository;
    }

    public ConsumablesOnPurchaseOrder saveNewConsumablesOnPurchaseOrder(ConsumablesOnPurchaseOrder copo) {
        return consumablesOnPurchaseOrderRepository.save(copo);
    }

    public ConsumablesOnPurchaseOrder getConsumablesOnPurchaseOrderById(Long id) {
        return consumablesOnPurchaseOrderRepository.findById(id).orElse(null);
    }

    public ConsumablesOnPurchaseOrder changeProductStatus(String newStatus, Long id) {
        ConsumablesOnPurchaseOrder copo = consumablesOnPurchaseOrderRepository.findById(id).orElse(null);
        assert copo != null;
        copo.setStatus(newStatus);
        return consumablesOnPurchaseOrderRepository.save(copo);
    }
}
