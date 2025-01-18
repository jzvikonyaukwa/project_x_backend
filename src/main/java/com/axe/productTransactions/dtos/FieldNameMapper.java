package com.axe.productTransactions.dtos;

import java.util.HashMap;
import java.util.Map;

public class FieldNameMapper {

    private static final Map<String, String> fieldNameMap = new HashMap<>();

    static {
        fieldNameMap.put("coilNumber", "coil_number");
        fieldNameMap.put("id", "pt.id");
        fieldNameMap.put("date", "DATE(pt.date)");
        // Add more mappings as needed, from frontend field name to database column name
    }

    public static String getDatabaseColumnName(String frontendFieldName) {
        return fieldNameMap.getOrDefault(frontendFieldName, frontendFieldName);
    }
}
