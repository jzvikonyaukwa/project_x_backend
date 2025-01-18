package com.axe.colors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorServices {

    private final ColorRepository colorRepository;

    public ColorServices(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    public List<Color> getAllColors() {
        return colorRepository.findAll(
            Sort.by(Sort.Direction.ASC, "color")
        );
    }

    public Color getColorColor(String color) {
        return colorRepository.findByColor(color).orElseThrow();
    }

    public Color getColorId(Long id) {
        return colorRepository.findById(id).orElseThrow();
    }

    public Color getGalvanizeColor() {
        return colorRepository.findByColor("Galvanize").orElseThrow();
    }
}
