package com.axe.suppliers;

import com.axe.suppliers.SupplierDTOs.SupplierDetailsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SuppliersController {

    private final SuppliersService suppliersService;

    public SuppliersController(SuppliersService suppliersService) {
        this.suppliersService = suppliersService;
    }

    @GetMapping("all-suppliers")
    public List<Supplier> getAllSuppliers(){
        return suppliersService.getAllSuppliers();
    }

    @GetMapping("all-suppliers-with-details")
    public List<SupplierDetailsDTO> getAllSuppliersWithDetails(){
        return suppliersService.getAllSuppliersWithDetails();
    }

    @GetMapping("get-supplier-by-id/{id}")
    public Supplier getSupplierById(@PathVariable Long id){
        return suppliersService.getSupplierById(id);
    }

    @PostMapping("")
    public Supplier addSupplier(@RequestBody Supplier supplier){
        return suppliersService.addSupplier(supplier);
    }



}
