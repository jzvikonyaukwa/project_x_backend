package com.axe.suppliers;

import com.axe.clientAddresses.ClientAddress;
import com.axe.clientEmails.ClientEmail;
import com.axe.clientPhones.ClientPhone;
import com.axe.clients.Client;
import com.axe.suppliers.SupplierDTOs.SupplierDetailsDTO;
import com.axe.suppliersAddresses.SupplierAddress;
import com.axe.suppliersEmails.SupplierEmail;
import com.axe.suppliersPhones.SupplierPhone;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {

    private final SuppliersRepository suppliersRepository;

    public SuppliersService(SuppliersRepository suppliersRepository) {
        this.suppliersRepository = suppliersRepository;
    }

    public List<Supplier> getAllSuppliers() {

        return suppliersRepository.findAll(
            Sort.by(Sort.Direction.ASC, "name")
        );
    }

    public Supplier getSupplier(Long supplierId) {
        return suppliersRepository.findById(supplierId).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public Supplier addSupplier(Supplier supplier) {

        Supplier newSupplier = new Supplier();
        newSupplier.setName(supplier.getName());
        newSupplier.setNotes(supplier.getNotes());

        Supplier newSavedSupplier = suppliersRepository.save(newSupplier);

        for(SupplierAddress supplierAddress : supplier.getAddresses()){
            supplierAddress.setSupplier(newSupplier);
            newSavedSupplier.getAddresses().add(supplierAddress);
        }

        for(SupplierPhone supplierPhone : supplier.getPhones()){
            supplierPhone.setSupplier(newSavedSupplier);
            newSavedSupplier.getPhones().add(supplierPhone);
        }

        for (SupplierEmail supplierEmail : supplier.getEmails()){
            supplierEmail.setSupplier(newSavedSupplier);
            newSavedSupplier.getEmails().add(supplierEmail);
        }

        return suppliersRepository.save(newSavedSupplier);
    }

    public List<SupplierDetailsDTO> getAllSuppliersWithDetails() {
        return suppliersRepository.getAllSuppliersWithDetails();
    }

    public Supplier getSupplierById(Long id) {
        return suppliersRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    public void deleteSupplier(Long id) {

        Supplier supplier = suppliersRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found"));

        for(SupplierAddress supplierAddress : supplier.getAddresses()){
            supplierAddress.setSupplier(null);
        }

        for(SupplierPhone supplierPhone : supplier.getPhones()){
            supplierPhone.setSupplier(null);
        }

        for(SupplierEmail supplierEmail : supplier.getEmails()){
            supplierEmail.setSupplier(null);
        }

        suppliersRepository.deleteById(id);
    }
}
