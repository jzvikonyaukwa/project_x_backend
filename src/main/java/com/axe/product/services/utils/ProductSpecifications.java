package com.axe.product.services.utils;

import com.axe.product.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class ProductSpecifications {
    public static Specification<Product> withFilters(String filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always filter by completed status
            predicates.add(cb.equal(root.get("status"), "completed"));

            if (filters != null && !filters.isEmpty()) {
                String[] conditions = filters.split(",");
                for (String condition : conditions) {
                    String[] parts = condition.split(":");
                    if (parts.length < 4) continue;

                    String field = parts[0].trim();
                    String type = parts[1].trim();
                    String operator = parts[2].trim();
                    String value = parts[3].trim();

                    Object convertedValue = convertValue(type, value);

                    if ("equals".equalsIgnoreCase(operator)) {
                        if ("date".equalsIgnoreCase(type)) {
                            if (root.get(field).getJavaType().equals(LocalDate.class)) {
                                predicates.add(cb.equal(root.get(field), convertedValue));
                            }
                             else if (root.get(field).getJavaType().equals(LocalDateTime.class)) {
                                 LocalDate dateVal = (LocalDate) convertedValue;
                                 LocalDateTime startOfDay = dateVal.atStartOfDay();
                                 LocalDateTime endOfDay = dateVal.atTime(LocalTime.MAX);
                                 Path<LocalDateTime> dateTimePath = root.get(field);
                                 predicates.add(cb.between(dateTimePath, startOfDay, endOfDay));
                             }
                        } else {
                            // For non-date fields
                            predicates.add(cb.equal(root.get(field), convertedValue));
                        }
                    } else if ("like".equalsIgnoreCase(operator) && convertedValue instanceof String) {
                        predicates.add(cb.like(root.get(field), "%" + convertedValue + "%"));
                    }
                    // Add more operators as needed (greaterThan, lessThan, etc.)
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Object convertValue(String type, String value) {
        switch (type.toLowerCase()) {
            case "number":
                return Long.valueOf(value);
            case "date":
                return LocalDate.parse(value);
            default:
                return value;
        }
    }

}
