package com.axe.consumablesOnGrv;

import org.springframework.stereotype.Service;

@Service
public class ConsumablesOnGrvService {

    private final ConsumablesOnGrvRepository consumablesOnGrvRepository;

    public ConsumablesOnGrvService(ConsumablesOnGrvRepository consumablesOnGrvRepository) {
        this.consumablesOnGrvRepository = consumablesOnGrvRepository;
    }

    public ConsumablesOnGrv getConsumablesOnGrvById(Long consumableOnGrvId) {
        return consumablesOnGrvRepository.findById(consumableOnGrvId).orElse(null);
    }

    public ConsumablesOnGrv saveConsumablesOnGrv(ConsumablesOnGrv consumablesOnGrv){
        return consumablesOnGrvRepository.save(consumablesOnGrv);
    }


}
