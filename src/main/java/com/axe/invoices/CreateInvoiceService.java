package com.axe.invoices;

import com.axe.consumablesInWarehouse.ConsumableInWarehouseService;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.consumablesOnQuote.ConsumablesOnQuoteService;
import com.axe.inventories.Inventory;
import com.axe.inventories.InventoryService;
import com.axe.invoices.invoiceDTOs.IssueInvoiceDTO;
import com.axe.product.Product;
import com.axe.product.services.ProductService;
import com.axe.saleOrder.SaleOrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.axe.invoices.invoiceDTOs.ItemsToBeInvoiced;

@Service
public class CreateInvoiceService {

    private final ProductService productService;
    private final ConsumablesOnQuoteService consumablesOnQuoteService;
    private final InvoiceRepository invoiceRepository;
    private final ConsumableInWarehouseService consumableInWarehouseService;
    private final SaleOrderRepository saleOrderRepository;
    private  final InventoryService inventoryService;
    private final Logger logger = LoggerFactory.getLogger(CreateInvoiceService.class);

    public CreateInvoiceService(ProductService productService,
                                ConsumablesOnQuoteService consumablesOnQuoteService,
                                InvoiceRepository invoiceRepository,
                                ConsumableInWarehouseService consumableInWarehouseService,
                                SaleOrderRepository saleOrderRepository,
                                InventoryService inventoryService) {
        this.productService = productService;
        this.consumablesOnQuoteService = consumablesOnQuoteService;
        this.invoiceRepository = invoiceRepository;
        this.consumableInWarehouseService = consumableInWarehouseService;
        this.saleOrderRepository = saleOrderRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public Invoice  createInvoice(IssueInvoiceDTO invoicesToIssue) {
        Invoice createInvoice = saleOrderRepository
                .findById(invoicesToIssue.getSaleOrderId())
                .map(saleOrder ->{
                            Invoice invoice = new Invoice();
                            invoice.setDateInvoiced(invoicesToIssue.getDateInvoiced());
                            invoice.setPaid(false);
                            invoice.setSaleOrder(saleOrder);

                            invoice = invoiceRepository.save(invoice);
                            for(ItemsToBeInvoiced item : invoicesToIssue.getItemsToBeInvoiced()){
                                if( item.getType().equalsIgnoreCase("cutting list")){
                                    logger.info("Cutting list id: {}", item.getId());
                                    invoiceCuttingList(item.getId(), invoice);
                                }
                                else if( item.getType().equalsIgnoreCase("consumable")){
                                    logger.info("Consumable id: {}", item.getId());
                                    invoiceConsumableOnQuote(item.getId(), invoice);
                                }
                                else {
                                    throw new IllegalStateException("Item type: [%s]  is not supported"
                                            .formatted(item.getType()));
                                }
                            }
                            return invoice;
                        })
                .orElseThrow(()->new IllegalStateException("Couldn't find sales order with id [%s] : to issue invoice"
                        .formatted(invoicesToIssue.getSaleOrderId())));

        return invoiceRepository.save(createInvoice);
    }

    private void invoiceCuttingList(Long productId, Invoice invoice){
        Product product = productService.getProductById(productId).orElseThrow(() -> new RuntimeException("Product id [%s] Not Found" .formatted(productId)));
        product.setInvoice(invoice);
        product = productService.createProduct(product);
        invoice.getProducts().add(product);
        logger.info("invoice created for Cutting list id: {}", product.getId());
    }

    private void invoiceConsumableOnQuote(Long consumableOnQuoteId, Invoice invoice){
        ConsumableOnQuote consumableOnQuote = consumablesOnQuoteService.getConsumableOnQuoteById(consumableOnQuoteId);

        ConsumablesInWarehouse consumablesInWarehouse
                = consumableInWarehouseService.updateQuantity(
                consumableOnQuote.getConsumable().getId(), 1L, -consumableOnQuote.getQty());

         consumableInWarehouseService.saveConsumableInWarehouse(consumablesInWarehouse);

        consumableOnQuote.setInvoice(invoice);
        consumableOnQuote = consumablesOnQuoteService.saveConsumableOnQuote(consumableOnQuote);

        if(consumableOnQuote != null){
                Inventory inventory = Inventory.builder()
                        .dateIn(invoice.getDateInvoiced())
                        .build();
                inventory.setConsumable(consumableOnQuote);
                consumableOnQuote.getInventoryConsumables().add(inventory);
                inventoryService.createInventory(inventory);
            logger.info("invoice created for Consumable id: {}", consumableOnQuote.getId());
        }
        invoice.getConsumablesOnQuote().add(consumableOnQuote);
    }

}
