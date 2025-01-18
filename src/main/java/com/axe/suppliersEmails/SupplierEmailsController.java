package com.axe.suppliersEmails;

import com.axe.suppliersAddresses.SupplierAddress;
import com.axe.suppliersEmails.supplierEmailsDTOs.SupplierEmailsPostDTOs;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier-emails")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SupplierEmailsController {

    private final SupplierEmailsService supplierEmailsService;

    public SupplierEmailsController(SupplierEmailsService supplierEmailsService) {
        this.supplierEmailsService = supplierEmailsService;
    }

    @GetMapping("all-emails")
    public List<SupplierEmail> getAllEmails() {
        return supplierEmailsService.getAllEmails();
    }

    @PatchMapping("update-emails")
    public List<SupplierEmail> updateEmails(@RequestBody List<SupplierEmailsPostDTOs> supplierEmails) {
        return supplierEmailsService.updateEmails(supplierEmails);
    }

    @DeleteMapping("{id}")
    public void deleteEmail(@PathVariable Long id){
        supplierEmailsService.deleteEmail(id);
    }

}
