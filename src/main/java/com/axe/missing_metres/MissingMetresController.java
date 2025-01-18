package com.axe.missing_metres;

import com.axe.missing_metres.models.MissingMetersRequest;
import com.axe.steelCoils.SteelCoil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missing-metres")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class MissingMetresController {

 private  final MissingMetresService missingMetresService;

    public MissingMetresController(MissingMetresService missingMetresService) {
        this.missingMetresService = missingMetresService;
    }

    @PostMapping("/log-missing-meters")
    public ResponseEntity<SteelCoil> logMissingMeters(@RequestBody MissingMetersRequest missingMetersRequest){
        return ResponseEntity.ok( missingMetresService.logMissingMeters(missingMetersRequest));
    }
}
