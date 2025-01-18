package com.axe.suppliersEmails;

import com.axe.suppliers.SuppliersService;
import com.axe.suppliersEmails.supplierEmailsDTOs.SupplierEmailsPostDTOs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierEmailsService {

    private final SupplierEmailsRepository supplierEmailsRepository;

    private final SuppliersService suppliersService;

    public SupplierEmailsService(SupplierEmailsRepository supplierEmailsRepository, SuppliersService suppliersService) {
        this.supplierEmailsRepository = supplierEmailsRepository;
        this.suppliersService = suppliersService;
    }

    public List<SupplierEmail> getAllEmails() {
        return supplierEmailsRepository.findAll();
    }

    public SupplierEmail saveEmail(SupplierEmail email) {
        return supplierEmailsRepository.save(email);
    }

    public List<SupplierEmail> updateEmails(List<SupplierEmailsPostDTOs> supplierEmails) {
        List<SupplierEmail> savedEmail = new ArrayList<>();

        for(SupplierEmailsPostDTOs email : supplierEmails) {

            if(email.getDelete() && email.getId() != null){
                deleteEmail(email.getId());
                continue;
            }
            SupplierEmail supplierEmail = convertToSupplierEmail(email);
            savedEmail.add(saveEmail(supplierEmail));
        }

        return savedEmail;
    }

    void deleteEmail(Long id) {
        supplierEmailsRepository.deleteById(id);
    }

    private SupplierEmail convertToSupplierEmail(SupplierEmailsPostDTOs dto) {
        SupplierEmail supplierEmail = new SupplierEmail();
        supplierEmail.setId(dto.getId());
        supplierEmail.setEmail(dto.getEmail());
        supplierEmail.setLabel(dto.getLabel());
        supplierEmail.setSupplier(suppliersService.getSupplierById(dto.getSupplierId()));
        return supplierEmail;
    }
}
