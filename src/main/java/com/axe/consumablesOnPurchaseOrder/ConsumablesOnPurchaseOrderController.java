package com.axe.consumablesOnPurchaseOrder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumable-on-purchase-order")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsumablesOnPurchaseOrderController {

    private final ConsumablesOnPurchaseOrderService consumablesOnPurchaseOrderService;

    public ConsumablesOnPurchaseOrderController(ConsumablesOnPurchaseOrderService consumablesOnPurchaseOrderService) {
        this.consumablesOnPurchaseOrderService = consumablesOnPurchaseOrderService;
    }

    @PatchMapping("change-status/{id}/new-status/{newStatus}")
    public ConsumablesOnPurchaseOrder changeProductStatus(@PathVariable Long id, @PathVariable String newStatus){
        return consumablesOnPurchaseOrderService.changeProductStatus(newStatus, id);
    }
}
