package com.axe.suppliersAddresses;

import com.axe.suppliersAddresses.supplierAdressDTOs.SupplierAddressPostDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-addresses")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SupplierAddressesController {
    private final SupplierAddressesService supplierAddressesService;

    public SupplierAddressesController(SupplierAddressesService supplierAddressesService) {
        this.supplierAddressesService = supplierAddressesService;
    }

    @GetMapping("all-addresses")
    public List<SupplierAddress> getAllAddresses() {
        return supplierAddressesService.getAllAddresses();
    }

    @PatchMapping("update-addresses")
    public List<SupplierAddress> updateAddresses(@RequestBody List<SupplierAddressPostDTO> supplierAddress) {
        return supplierAddressesService.updateAddresses(supplierAddress);
    }

    @DeleteMapping("{id}")
    public void deleteAddress(@PathVariable Long id) {
        supplierAddressesService.deleteAddress(id);
    }
}
