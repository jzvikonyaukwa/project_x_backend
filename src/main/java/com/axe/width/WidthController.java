package com.axe.width;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/widths")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class WidthController {

    private final WidthService widthService;

    public WidthController(WidthService widthService) {
        this.widthService = widthService;
    }

    @GetMapping()
    public List<Width> getAllWidths() {
        return widthService.getAllWidths();
    }
}
