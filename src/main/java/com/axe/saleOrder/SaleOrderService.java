package com.axe.saleOrder;

import com.axe.common.enums.SaleOrderStatus;
import com.axe.quotes.Quote;
import com.axe.saleOrder.models.ProductsTotalLengthOnOrder;
import com.axe.saleOrder.models.SaleOrderData;
import com.axe.saleOrder.models.SalesOrderOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderService {
    private final static Logger logger = LoggerFactory.getLogger(SaleOrderService.class);

    private final SaleOrderRepository saleOrdersRepository;
    
    public SaleOrderService(SaleOrderRepository ordersRepository) {
        this.saleOrdersRepository = ordersRepository;
    }

    public SaleOrder saveOrder(SaleOrder order){
        return saleOrdersRepository.save(order);
    }

    public SaleOrder createOrder(Quote quote) {
        SaleOrder order = new SaleOrder();
        order.setDateIssued(LocalDate.now());
        order.setTargetDate(LocalDate.now().plusDays(7));
        order.setStatus(SaleOrderStatus.open);
        order.setQuote(quote);
        return saveOrder(order);
    }

    public List<SaleOrder> getAllSaleOrders() {
        return saleOrdersRepository.findAll();
    }

    public SaleOrder getSaleOrderById(Long id) {
        return saleOrdersRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale Order not found"));
    }

    @Transactional(readOnly = true)
    public List<SalesOrderOverview> getAllSaleOrdersSummaryDetails() {
        logger.info("Getting all sale orders summary details");
        List<SaleOrderData> saleOrderData = saleOrdersRepository.getAllSaleOrdersSummaryDetails();
        List<SalesOrderOverview> salesOrderSummaries = new ArrayList<>();

        logger.info("Sale orders found from DB: " + saleOrderData.size());
        if (saleOrderData.isEmpty()) {
            logger.info("No sale orders found");
            return salesOrderSummaries;
        }

        SalesOrderOverview salesOrderOverview = null;
        Long currentSaleOrderId = null;

        for (SaleOrderData row : saleOrderData) {
            if (salesOrderOverview == null || !row.getSaleOrderId().equals(currentSaleOrderId)) {
                if (salesOrderOverview != null) {
                    salesOrderSummaries.add(salesOrderOverview);
                }
                currentSaleOrderId = row.getSaleOrderId();
                salesOrderOverview = new SalesOrderOverview();
                salesOrderOverview.setSaleOrderId(row.getSaleOrderId());
                salesOrderOverview.setSalesOrderIssued(row.getSalesOrderIssued());
                salesOrderOverview.setClientName(row.getClientName());
                salesOrderOverview.setTargetDate(row.getTargetDate());
                salesOrderOverview.setSalesOrderStatus(row.getSalesOrderStatus());
                salesOrderOverview.setCompletedCuttingList(0);
                salesOrderOverview.setInProgressCuttingLists(0);
            }

            if (row.getCuttingListStatus() != null) {
                if (row.getCuttingListStatus().equals("completed")) {
                    salesOrderOverview.setCompletedCuttingList(salesOrderOverview.getCompletedCuttingList() + 1);
                } else {
                    salesOrderOverview.setInProgressCuttingLists(salesOrderOverview.getInProgressCuttingLists() + 1);
                }
            }
        }

        salesOrderSummaries.add(salesOrderOverview); // Don't forget to add the last one

        logger.info("Sale orders processed: " + salesOrderSummaries.size());
        return salesOrderSummaries;
    }

    public void deleteSaleOrder(Long id) {
        saleOrdersRepository.deleteById(id);
    }

    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderReserved() {
        return saleOrdersRepository.getTotalLengthOnOrderReserved();
    }

    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrderNotReserved() {
        return saleOrdersRepository.getTotalLengthOnOrderNotReserved();
    }

    public List<ProductsTotalLengthOnOrder> getTotalLengthOnOrder() {
        return saleOrdersRepository.getTotalLengthOnOrder();
    }

//    public Client getSaleOrderForInvoice(Long invoiceId) {
//        return saleOrdersRepository.getSaleOrderForInvoice(invoiceId);
//    }
}
