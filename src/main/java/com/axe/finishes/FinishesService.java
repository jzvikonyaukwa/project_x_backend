package com.axe.finishes;

import com.axe.finishes.DTOs.FinishDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinishesService {

    private final FinishesRepository finishesRepository;

    public FinishesService(FinishesRepository finishesRepository) {
        this.finishesRepository = finishesRepository;
    }

    public List<FinishDTO> getAllFinishes() {
        return finishesRepository.getAllFinishes();
    }
}
