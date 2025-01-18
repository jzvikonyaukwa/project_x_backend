package com.axe.wastage;

import com.axe.steelCoils.SteelCoil;
import com.axe.wastage.models.AddWastageDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wastage")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class WastageController {

    private final WastageService wastageService;

    public WastageController(WastageService wastageService) {
        this.wastageService = wastageService;
    }

    @PostMapping()
    public SteelCoil addWastage(@RequestBody AddWastageDTO addWastageDTO){
        return wastageService.addWastage(addWastageDTO);
    }
}
