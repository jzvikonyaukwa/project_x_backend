package com.axe.common.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public interface PatchUtils {
    ObjectMapper mapper = new ObjectMapper();

    default    <Data, Patch> Data patch(Data data, Patch patch) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        ObjectReader objectReader = mapper.readerForUpdating(data);
        JsonNode patchNode = mapper.valueToTree(patch);
        try {
            return objectReader.readValue(patchNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
