package com.axe.consignor;

import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consignors")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ConsignorController {
    private final ConsignorService consignorService;

    public ConsignorController(ConsignorService consignorService) {
        this.consignorService = consignorService;
    }

    @GetMapping("")
    public List<Consignor> getAllConsignors(){
        return consignorService.getAllConsignors();
    }

    @GetMapping("/consignor/{consignorId}/steel")
    public List<SteelCoilDetailsDTO> getConsignorSteel(@PathVariable Long consignorId){
        return consignorService.getConsignorSteel(consignorId);
    }

    @GetMapping("/consignor/{consignorId}/consumables")
    public List<ConsumablesInWarehouse> getConsignorConsumables(@PathVariable Long consignorId){
        return consignorService.getConsignorConsumables(consignorId);
    }

    @PostMapping("")
    public Consignor addConsignor(@RequestBody Consignor consignor){
        return consignorService.addConsignor(consignor);
    }
}
