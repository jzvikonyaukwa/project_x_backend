package com.axe.suppliersPhones;

import com.axe.suppliers.SuppliersService;
import com.axe.suppliersPhones.supplierPhonesDTOs.SupplierPhonesPostDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierPhonesService {

    private final SupplierPhonesRepository supplierPhonesRepository;
    private final SuppliersService suppliersService;

    public SupplierPhonesService(SupplierPhonesRepository supplierPhonesRepository, SuppliersService suppliersService) {
        this.supplierPhonesRepository = supplierPhonesRepository;
        this.suppliersService = suppliersService;
    }

    public SupplierPhone save(SupplierPhone phone) {
        return supplierPhonesRepository.save(phone);
    }

    public List<SupplierPhone> getAllPhones() {
        return supplierPhonesRepository.findAll();
    }

    public List<SupplierPhone> updatePhones(List<SupplierPhonesPostDTO> supplierPhones) {
        List<SupplierPhone> savedPhones = new ArrayList<>();

        for(SupplierPhonesPostDTO supplierPhone : supplierPhones) {

            if(supplierPhone.getDelete() && supplierPhone.getId() != null){
                deletePhone(supplierPhone.getId());
                continue;
            }
            SupplierPhone phone = convertToSupplierPhone(supplierPhone);
            savedPhones.add(save(phone));
        }
        return savedPhones;
    }

    void deletePhone(Long id) {
        supplierPhonesRepository.deleteById(id);
    }

    private SupplierPhone convertToSupplierPhone(SupplierPhonesPostDTO dto) {
        SupplierPhone supplierPhone = new SupplierPhone();
        supplierPhone.setId(dto.getId());
        supplierPhone.setPhone(dto.getPhone());
        supplierPhone.setLabel(dto.getLabel());
        supplierPhone.setSupplier(suppliersService.getSupplierById(dto.getSupplierId()));
        return supplierPhone;
    }
}
