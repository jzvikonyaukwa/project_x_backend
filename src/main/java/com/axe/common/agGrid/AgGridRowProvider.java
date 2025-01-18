package com.axe.common.agGrid;

import java.util.function.Function;

import org.springframework.transaction.annotation.Transactional;

import com.axe.common.agGrid.request.ServerSideGetRowsRequest;
import com.axe.common.agGrid.response.ServerSideGetRowsResponse;

public interface AgGridRowProvider<T> {

    @Transactional(readOnly = true)
    ServerSideGetRowsResponse<T> getRows(final ServerSideGetRowsRequest request);

    default Function<String,String> escapeSpecialCharactersInString() {
        return inputString -> inputString.trim()
                     .replace("!", "!!")
                     .replace("%", "!%")
                     .replace("_", "!_");
    }
  
}
