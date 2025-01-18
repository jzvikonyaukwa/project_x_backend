package com.axe.steelCoils.steelCoilsDTO;

import lombok.Builder;

import java.util.List;

@Builder
public record SteelCoilFilterResponse(List<SteelCoilDetailsDTO> steelCoilDetailsDTO, long totalElements) {

}
