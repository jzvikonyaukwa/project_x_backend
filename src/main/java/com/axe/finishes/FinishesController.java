package com.axe.finishes;


import com.axe.finishes.DTOs.FinishDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finishes")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class FinishesController {

    private final FinishesService finishesService;

    public FinishesController(FinishesService finishesService) {
        this.finishesService = finishesService;
    }

    @GetMapping("all-finishes")
    public List<FinishDTO> getAllFinishes(){
        return finishesService.getAllFinishes();
    }
}
