package com.axe.wastage;


import com.axe.product.Product;
import com.axe.product.services.ProductService;
import com.axe.productTransactions.ProductTransaction;
import com.axe.productTransactions.ProductTransactionsService;
import com.axe.steelCoils.SteelCoil;
import com.axe.steelCoils.SteelCoilService;
import com.axe.wastage.models.AddWastageDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WastageService {

    private final WastageRepository wastageRepository;
    private final SteelCoilService steelCoilService;
    private final ProductService productService;
    private final ProductTransactionsService productTransactionsService;

    public WastageService(WastageRepository wastageRepository, SteelCoilService steelCoilService,
                          ProductService productService, ProductTransactionsService productTransactionsService) {
        this.wastageRepository = wastageRepository;
        this.steelCoilService = steelCoilService;
        this.productService = productService;
        this.productTransactionsService = productTransactionsService;
    }

    @Transactional
    public SteelCoil addWastage(AddWastageDTO addWastageDTO) {
        SteelCoil steelCoil = steelCoilService.getSteelCoil(addWastageDTO.getSteelCoilId());

        Wastage wastage = new Wastage();
        wastage.setMtrsWaste(addWastageDTO.getWastageInMeters());

        if(steelCoil == null){
            throw new RuntimeException("Steel Coil not found");
        }

        ProductTransaction productTransaction = productTransactionsService.createProductTransaction(
                steelCoil.getId(), addWastageDTO.getDate(), wastage);

        wastage.setProductTransaction(productTransaction);

        if(addWastageDTO.getProductId() != null){
            Product product = productService.getProductById(addWastageDTO.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product id [%s] not found".formatted(addWastageDTO.getProductId())));
            if(product == null){
                throw new RuntimeException("Cutting List not found");
            }
//            wastage.setProduct(product);
        }

        wastageRepository.save(wastage);

        return steelCoil;
    }
}
