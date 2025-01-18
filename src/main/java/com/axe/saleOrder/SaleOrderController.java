package com.axe.saleOrder;

import com.axe.clients.Client;
import com.axe.saleOrder.models.ProductsTotalLengthOnOrder;
import com.axe.saleOrder.models.SalesOrderOverview;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-orders")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SaleOrderController {
    private final SaleOrderService saleOrderService;

    public SaleOrderController(SaleOrderService saleOrderService) {
        this.saleOrderService = saleOrderService;
    }

    @GetMapping("")
    public List<SaleOrder> getAllSaleOrders(){
        return saleOrderService.getAllSaleOrders();
    }

    @GetMapping("overview")
    public List<SalesOrderOverview> getAllSaleOrdersSummaryDetails(){
        return saleOrderService.getAllSaleOrdersSummaryDetails();
    }

    @GetMapping("/{id}")
    public SaleOrder getSaleOrderById(@PathVariable Long id){
        return saleOrderService.getSaleOrderById(id);
    }

    //    Not being used
    @GetMapping("total-length-on-order/reserved")
    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderReserved(){
        return saleOrderService.getTotalLengthOnOrderReserved();
    }

    //    Not being used
    @GetMapping("total-length-on-order/not-reserved")
    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderNotReserved(){
        return saleOrderService.getTotalLengthOnOrderNotReserved();
    }

    @GetMapping("total-length-on-order")
    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrder(){
        return saleOrderService.getTotalLengthOnOrder();
    }

}
