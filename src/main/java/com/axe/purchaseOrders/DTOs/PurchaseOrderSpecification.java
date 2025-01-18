package com.axe.purchaseOrders.DTOs;

import com.axe.purchaseOrders.PurchaseOrder;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderSpecification {

    public static Specification<PurchaseOrder> getFilterSpecification(String filters) {
        return (root, query, criteriaBuilder) -> {
            if (filters == null || filters.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String[] filterArray = filters.split(",");
            List<Predicate> predicates = new ArrayList<>();

            for (String filter : filterArray) {
                String[] keyValue = filter.split(":");
                if (keyValue.length == 4) {
                    String key = keyValue[0];
                    String filterType = keyValue[1];
                    String filterOperation = keyValue[2];
                    String value = keyValue[3];

                    switch (filterOperation) {
                        case "contains":
                            if (key.equals("supplierName")) {
                                predicates.add(criteriaBuilder.like(root.join("supplier").get("name"), "%" + value + "%"));
                            } else if (key.equals("status")) {
                                predicates.add(criteriaBuilder.like(root.get("status"), "%" + value + "%"));
                            } else {
                                predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                            }
                            break;
                        case "equals":
                            if (key.equals("supplierName")) {
                                predicates.add(criteriaBuilder.equal(root.join("supplier").get("name"), value));
                            } else if (key.equals("status")) {
                                predicates.add(criteriaBuilder.equal(root.get("status"), value));
                            } else {
                                predicates.add(criteriaBuilder.equal(root.get(key), value));
                            }
                            break;
                        case "set":
                            if (key.equals("status")) {
                                predicates.add(root.get("status").in((Object[]) value.split(",")));
                            }
                            break;
                        // Add more cases for other operations if needed
                        default:
                            throw new IllegalArgumentException("Unknown filter operation: " + filterOperation);
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}