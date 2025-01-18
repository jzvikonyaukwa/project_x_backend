package com.axe.suppliersAddresses;

import com.axe.suppliers.SuppliersService;
import com.axe.suppliersAddresses.supplierAdressDTOs.SupplierAddressPostDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierAddressesService {

    private final SupplierAddressesRepository supplierAddressesRepository;

    private final SuppliersService suppliersService;

    public SupplierAddressesService(SupplierAddressesRepository supplierAddressesRepository,
                                    SuppliersService suppliersService) {
        this.supplierAddressesRepository = supplierAddressesRepository;
        this.suppliersService = suppliersService;
    }

    public SupplierAddress saveAddress(SupplierAddress address) {
        return supplierAddressesRepository.save(address);
    }

    public List<SupplierAddress> updateAddresses(List<SupplierAddressPostDTO> supplierAddress) {

        List<SupplierAddress> savedAddresses = new ArrayList<>();
        for(SupplierAddressPostDTO address : supplierAddress) {

            if(address.getDelete() && address.getId() != null){
                deleteAddress(address.getId());
                continue;
            }
            SupplierAddress supplierAddress1 = convertToSupplierAddress(address);
            savedAddresses.add(saveAddress(supplierAddress1));
        }

        return savedAddresses;
    }

    public void deleteAddress(Long id) {
        supplierAddressesRepository.deleteById(id);
    }

    public List<SupplierAddress> getAllAddresses() {
        return supplierAddressesRepository.findAll();
    }

    private SupplierAddress convertToSupplierAddress(SupplierAddressPostDTO dto) {
        SupplierAddress supplierAddress = new SupplierAddress();
        supplierAddress.setId(dto.getId());
        supplierAddress.setStreet(dto.getStreet());
        supplierAddress.setSuburb(dto.getSuburb());
        supplierAddress.setCity(dto.getCity());
        supplierAddress.setCountry(dto.getCountry());
        supplierAddress.setLabel(dto.getLabel());
        supplierAddress.setSupplier(suppliersService.getSupplierById(dto.getSupplierId()));
        return supplierAddress;
    }
}
