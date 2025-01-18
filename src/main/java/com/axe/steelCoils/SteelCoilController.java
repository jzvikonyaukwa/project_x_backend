package com.axe.steelCoils;

import com.axe.saleOrder.models.TotalMtrsRemaingForEachCategoryDTO;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilDetailsDTO;
import com.axe.steelCoils.steelCoilsDTO.SteelCoilTransactionInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/steel-coils")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class SteelCoilController {

    private static final Logger log = LoggerFactory.getLogger(SteelCoilController.class);
    private final SteelCoilService steelCoilService;

    public SteelCoilController(SteelCoilService steelCoilService) {
        this.steelCoilService= steelCoilService;
    }

    @GetMapping("")
    public List<SteelCoil> getAllSteelCoils(){
        return steelCoilService.getAllSteelCoils();
    }

    @GetMapping("{steelCoilId}")
    public SteelCoil getStealCoil(@PathVariable Long steelCoilId){
        return steelCoilService.getSteelCoil(steelCoilId);
    }

    @GetMapping("{steelCoilId}/details")
    public SteelCoilDetailsDTO getSteelCoilDetails(@PathVariable Long steelCoilId){
        return steelCoilService.getSteelCoilDetails(steelCoilId);
    }

    @GetMapping("{steelCoilId}/transactions")
    public List<SteelCoilTransactionInformation> getSteelCoilTransactions(@PathVariable Long steelCoilId){
        return steelCoilService.getSteelCoilTransactions(steelCoilId);
    }

    @GetMapping("available/for-machine/width/{width}")
    public List<SteelCoilDetailsDTO> getAvailableSteelCoilsForMachine(@PathVariable("width") BigDecimal width){
        return steelCoilService.getAvailableSteelCoilsForMachine(width);
    }


    @GetMapping("/total-products-lengths")
    public List<TotalMtrsRemaingForEachCategoryDTO> getAllStockLevels(){
        return steelCoilService.getAllStockLevels();
    }

    @GetMapping("stock-levels/{width}/{gauge}/{color}")
    public BigDecimal getStockLevelsForSteelType(@PathVariable BigDecimal width, @PathVariable BigDecimal gauge,
                                                 @PathVariable String color){
        return steelCoilService.getStockLevelsForSteelType(width, gauge, color);
    }

    @GetMapping("filter/{width}")
    public ResponseEntity<List<SteelCoilDetailsDTO>> getFilteredAvailableSteelCoils(
            @PathVariable BigDecimal width,
            @RequestParam(required = false) Double gauge,
            @RequestParam(required = false) String color
          ) {
        log.info("Filtering steel coils with width: {}, gauge: {}, color: {}", width, gauge, color);
        return ResponseEntity.ok(steelCoilService.getFilteredAvailableSteelCoils(width,gauge, color));
    }

    @GetMapping("average-price/{widthId}/{gaugeId}/{colorId}")
    public BigDecimal getAveragePriceForProduct(@PathVariable Long widthId, @PathVariable Long gaugeId,
                                                 @PathVariable Long colorId){
        return steelCoilService.calculateWeightedAvgCostForSteelType(widthId, gaugeId, colorId);
    }

}
