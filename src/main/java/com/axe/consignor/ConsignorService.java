package com.axe.consignor;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsignorService {
    private final ConsignorRepository consignorRepository;

    public ConsignorService(ConsignorRepository consignorRepository) {
        this.consignorRepository = consignorRepository;
    }

    public List<Consignor> getAllConsignors() {
        return consignorRepository.findAll();
    }

    public List<SteelCoilDetailsDTO> getConsignorSteel(Long consignorId) {
        return consignorRepository.getConsignorSteel(consignorId);
    }

    public List<ConsumablesInWarehouse> getConsignorConsumables(Long consignorId) {
        return consignorRepository.getConsignorConsumables(consignorId);
    }

    public Consignor addConsignor(Consignor consignor) {
        return consignorRepository.save(consignor);
    }
}
