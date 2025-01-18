package com.axe.width;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WidthService {

    private final WidthRepository widthRepository;

    public WidthService(WidthRepository widthRepository) {
        this.widthRepository = widthRepository;
    }

    public List<Width> getAllWidths() {
        return widthRepository.findAll(
            Sort.by(Sort.Direction.ASC, "width")
        );
    }

    public Width getWidthById(Long widthId){
        return widthRepository.findById(widthId).orElseThrow(() -> new RuntimeException("Width not found"));
    }

    public BigDecimal getWidthFromMachineId(Long machineId) {
        BigDecimal width;

        if(machineId == 1) {
            width = new BigDecimal("925");
        } else if(machineId == 2) {
            width = new BigDecimal("150");
        } else if(machineId == 3) {
            width = new BigDecimal("103");
        } else {
            width = new BigDecimal("182");
        }

//        if(machineName.equals("double roll-forming machine")) {
//            width = new BigDecimal("925");
//        } else if(machineName.equals("ceiling batten")) {
//            width = new BigDecimal("103");
//        } else if(machineName.equals("roof purlin")) {
//            width = new BigDecimal("150");
//        } else {
//            width = new BigDecimal("180");
//        }

        return width;
    }

    public Width getWidthByWidth(BigDecimal width){
        return widthRepository.findByWidth(width);
    }
}
