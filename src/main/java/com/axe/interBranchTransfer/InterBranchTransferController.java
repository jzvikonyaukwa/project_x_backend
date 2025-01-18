package com.axe.interBranchTransfer;

import com.axe.interBranchTransfer.DTOs.*;
import com.axe.interBranchTransfer.consumables.ConsumableInterBranchTransfer;
import com.axe.interBranchTransfer.consumables.ConsumableInterBranchTransferService;
import com.axe.interBranchTransfer.steelCoils.SteelCoilInterBranchTransfer;
import com.axe.interBranchTransfer.steelCoils.SteelCoilInterBranchTransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inter-branch-transfers")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class InterBranchTransferController {

    private final SteelCoilInterBranchTransferService steelCoilInterBranchTransferService;
    private final ConsumableInterBranchTransferService consumableInterBranchTransferService;

    public InterBranchTransferController(SteelCoilInterBranchTransferService steelCoilInterBranchTransferService,
                                         ConsumableInterBranchTransferService consumableInterBranchTransferService) {
        this.steelCoilInterBranchTransferService = steelCoilInterBranchTransferService;
        this.consumableInterBranchTransferService = consumableInterBranchTransferService;
    }

    @GetMapping("")
    public ResponseEntity<List<SteelCoilInterBranchTransfer>> getAllInterBranchTransfers(){
        return ResponseEntity.ok(steelCoilInterBranchTransferService.getAllInterBranchTransfers());
    }

    @GetMapping("steel-coils/details")
    public ResponseEntity<List<SteelCoilInterBranchTransferDetails>> getAllSteelCoilInterBranchTransferDetails(){
        return ResponseEntity.ok(steelCoilInterBranchTransferService.getAllSteelCoilInterBranchTransferDetails());
    }

    @GetMapping("steel-coil/{id}/details")
    public ResponseEntity<List<InterBranchTransferDetails>> getSteelCoilInterBranchTransferDetailsForSteelCoil(@PathVariable("id") Long id){
        return ResponseEntity.ok(steelCoilInterBranchTransferService.getSteelCoilInterBranchTransferDetailsForSteelCoil(id));
    }

    @GetMapping("consumables/details")
    public ResponseEntity<List<ConsumableInterBranchTransferDetails>> getAllConsumableInterBranchTransferDetails(){
        return ResponseEntity.ok(steelCoilInterBranchTransferService.getAllConsumableInterBranchTransferDetails());
    }

    @PostMapping("steel-coil")
    public ResponseEntity<SteelCoilInterBranchTransfer> addSteelCoilInterBranchTransfer(@RequestBody SteelCoilInterBranchTransferDTO interBranchTransfer){
        return ResponseEntity.ok(steelCoilInterBranchTransferService.createInterBranchTransfer(interBranchTransfer));
    }

    @PostMapping("consumable")
    public ResponseEntity<ConsumableInterBranchTransfer> addSteelCoilInterBranchTransfer(@RequestBody ConsumableInterBranchTransferDTO interBranchTransfer){
        return ResponseEntity.ok(consumableInterBranchTransferService.createConsumableInterBranchTransfer(interBranchTransfer));
    }
}
