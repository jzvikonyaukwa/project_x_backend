package com.axe.productTransactions;

import com.axe.productTransactions.dtos.FieldNameMapper;
import com.axe.productTransactions.dtos.ProductTransactionDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.ArrayList;

@Repository
public class ProductTransactionsCustomRepositoryImpl implements ProductTransactionsCustomRepository {

    private final Logger logger = LoggerFactory.getLogger(ProductTransactionsCustomRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductTransactionDetails> getFilteredProductTransactionDetails(Pageable pageable, String filters) {
        String baseQuery = """
              SELECT pt.id as id, pt.date as date, ap.id as manufacturedProductId, p.total_length as length, p.id as productId,
                                        ap.code as manufacturedProductCode, p.frame_name as frameName,
                                        sc.id as steelCoilId, sc.coil_number as coilNumber,
                                        w.id as wastageId, w.mtrs_waste as mtrsWasted,
                                        soh.id as stockOnHandId, ap.length as stockOnHandLength,
                                        wid.width as steelCoilWidth,
                                        g.gauge as gauge,
                                        co.color as color, 
                                        pty.name as productType
                                        FROM axe.product_transactions as pt
                                        LEFT JOIN axe.aggregated_products ap ON pt.id = ap.product_transaction_id
                                        LEFT JOIN axe.products as p ON p.id = ap.product_id
                                        LEFT JOIN axe.steel_coils as sc ON sc.id = pt.steel_coil_id
                                        LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
                                        LEFT JOIN axe.stock_on_hand as soh ON soh.aggregate_product_id = ap.id
                                        LEFT JOIN axe.steel_specifications as ss ON sc.steel_specification_id = ss.id
                                        LEFT JOIN axe.product_type as pty ON p.product_type_id =pty.id
                                        LEFT JOIN axe.gauges as g On ss.gauge_id = g.id
                                        LEFT JOIN axe.colors as co On ss.color_id = co.id
                                        LEFT JOIN axe.widths as wid On ss.width_id = wid.id
        """;

        String countQuery = """
            SELECT COUNT(*)
            FROM axe.product_transactions as pt
            LEFT JOIN axe.aggregated_products ap ON pt.id = ap.product_transaction_id
            LEFT JOIN axe.steel_coils as sc ON sc.id = pt.steel_coil_id
            LEFT JOIN axe.wastage as w ON w.product_transaction_id = pt.id
            LEFT JOIN axe.stock_on_hand as soh ON soh.aggregate_product_id = ap.id
        """;
        String whereClause = " WHERE 1=1 ";

        List<String> filterClauses = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {
            String[] filterArray = filters.split(",");
            for (String filter : filterArray) {
                String[] parts = filter.split(":");
                if (parts.length == 4) {
                    String frontendField = parts[0];
                    String filterType = parts[1];
                    String filterCondition = parts[2];
                    String filterValue = parts[3];

                    if ("undefined".equals(filterValue)) {
                        continue;
                    }

                    String dbField = FieldNameMapper.getDatabaseColumnName(frontendField);

                    switch (filterType) {
                        case "number":
                            if ("equals".equals(filterCondition)) {
                                filterClauses.add(dbField + " = " + filterValue);
                            }
                            break;
                        case "text":
                            if ("equals".equals(filterCondition)) {
                                filterClauses.add(dbField + " = '" + filterValue + "'");
                            } else if ("contains".equals(filterCondition)) {
                                filterClauses.add(dbField + " LIKE '%" + filterValue + "%'");
                            }
                            break;
                        case "date":
                            if ("equals".equals(filterCondition)) {
                                filterClauses.add(dbField + " = '" + filterValue + "'");
                            }
                            break;
                        // Add more cases for other filter conditions if necessary
                    }
                }
            }
        }

        if (!filterClauses.isEmpty()) {
            whereClause += " AND " + String.join(" AND ", filterClauses);
        }

        String finalQuery = baseQuery + whereClause;
        String finalCountQuery = countQuery + whereClause;


        Query query = entityManager.createNativeQuery(finalQuery, "ProductTransactionDetailsMapping");
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ProductTransactionDetails> resultList = query.getResultList();

        Query countQ = entityManager.createNativeQuery(finalCountQuery);
        long totalRecords = ((Number) countQ.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, totalRecords);
    }
}