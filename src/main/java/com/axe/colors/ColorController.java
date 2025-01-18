package com.axe.colors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
@CrossOrigin(origins = {"http://localhost:4200", "http://axebuild.io", "https://axebuild.io"})
public class ColorController {

    private final ColorServices colorServices;

    public ColorController(ColorServices colorServices) {
        this.colorServices = colorServices;
    }

    @GetMapping("all-colors")
    public List<Color> getAllColors(){
        return colorServices.getAllColors();
    }

    @GetMapping("galvanize")
    public Color ganvanizeColor(){
        return colorServices.getGalvanizeColor();
    }
}
