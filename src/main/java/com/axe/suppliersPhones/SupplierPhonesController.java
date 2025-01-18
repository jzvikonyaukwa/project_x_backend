package com.axe.suppliersPhones;

import com.axe.suppliersAddresses.SupplierAddress;
import com.axe.suppliersPhones.supplierPhonesDTOs.SupplierPhonesPostDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-phones")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SupplierPhonesController {

    private final SupplierPhonesService supplierPhonesService;

    public SupplierPhonesController(SupplierPhonesService supplierPhonesService) {
        this.supplierPhonesService = supplierPhonesService;
    }

    @GetMapping("all-phones")
    public List<SupplierPhone> getAllPhones() {
        return supplierPhonesService.getAllPhones();
    }

    @PatchMapping("update-phones")
    public List<SupplierPhone> updatePhones(@RequestBody List<SupplierPhonesPostDTO> supplierPhones) {
        return supplierPhonesService.updatePhones(supplierPhones);
    }

    @DeleteMapping("{id}")
    public void deletePhone(@PathVariable Long id){
        supplierPhonesService.deletePhone(id);
    }


}
