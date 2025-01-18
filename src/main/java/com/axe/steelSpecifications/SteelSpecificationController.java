package com.axe.steelSpecifications;

import com.axe.steelSpecifications.DTO.SupplierProductsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/steel-specifications")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SteelSpecificationController {

    private final SteelSpecificationService steelSpecificationService;

    public SteelSpecificationController(SteelSpecificationService steelSpecificationService) {
        this.steelSpecificationService = steelSpecificationService;
    }

    @GetMapping("all-specifications")
    public List<SteelSpecification> getAllProductTypes(){
        return steelSpecificationService.getAllProductTypes();
    }

//    @GetMapping("supplied-by/supplier/{supplierId}")
//    public List<SupplierProductsDTO> getAllProductTypesSupplierBySupplier(@PathVariable Long supplierId){
//        return steelSpecificationService.getAllProductTypesSupplierBySupplier(supplierId);
//    }
}
