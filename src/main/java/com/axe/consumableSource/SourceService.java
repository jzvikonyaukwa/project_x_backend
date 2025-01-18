package com.axe.consumableSource;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {
    private final SourceRepository sourceRepository;

    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public List<SourceCountry> getAllSources() {
        return sourceRepository.findAll();
    }

    public SourceCountry getSourceCountryByName(String sourceCountry) {
        return sourceRepository.findByName(sourceCountry);
    }
}
