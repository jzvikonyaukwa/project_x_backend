package com.axe.inventories;

import com.axe.inventories.inventoryDTOs.InventoryItemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoriesRepository extends JpaRepository<Inventory, Long>{

    @Query(value = """
            SELECT
            i.id as inventoryId,
            i.date_in as dateIn,
            i.date_out as dateOut,
            i.delivery_note_id as deliveryNoteId,
            q.id as quoteId, q.date_accepted as quoteAcceptedDate,
            pr.id as cuttingListId, pr.status as cuttingListStatus,
            COALESCE(pr.id, coq.id) AS itemId,
            c.name AS consumableName,
            pr.frame_name AS frameName,
            pr.frame_type AS frameType,
            pr.status as manufacturedProductStatus,
                CASE
                    WHEN pr.id IS NOT NULL THEN 'Manufactured Product'
                    WHEN coq.id IS NOT NULL THEN 'Consumable'
                    ELSE 'Unknown'
                END AS itemType,
                IF(pr.id IS NOT NULL, pr.length, NULL) AS length,
                IF(coq.id IS NOT NULL, coq.qty, NULL) AS qtyOrdered,
                p.name AS projectName,
                client.name AS clientName
               FROM axe.customer_inventory i
               LEFT JOIN axe.products pr ON i.id = pr.inventory_id
               LEFT JOIN axe.consumables_on_quote coq ON coq.id = i.consumables_on_quote_id
               LEFT JOIN axe.consumables c ON c.id = coq.consumables_id
               LEFT JOIN axe.quotes q ON q.id = pr.quote_id OR q.id = coq.quote_id
               LEFT JOIN axe.projects p ON p.id = q.project_id
               LEFT JOIN axe.clients client ON client.id = p.client_id
        """, nativeQuery = true)
    List<InventoryItemDTO> findAllWithDetails();

    @Query(value = """
   SELECT
        i.id AS inventoryId,
        p.name AS projectName,
        client.name AS clientName,
        i.date_in AS dateIn,
        i.date_out AS dateOut,
        i.delivery_note_id AS deliveryNoteId,
        q.id AS quoteId,
        q.date_accepted AS quoteAcceptedDate,
        pr.id AS productId,
        pr.status AS productStatus,
        pt.name AS productName,
        pr.total_length AS totalProductLength,
        pr.total_quantity AS numberOfAggregatedProducts,
        pr.frame_name AS frameName,
        pr.frame_type AS frameType,
        CASE
            WHEN pr.id IS NOT NULL THEN 'Product'
            WHEN coq.id IS NOT NULL THEN 'Consumable'
            ELSE 'Unknown'
        END AS itemType,
       coq.id AS consumableOnQuoteId,
        c.name AS consumableName,
        coq.qty AS numberOfConsumables
    FROM axe.customer_inventory i
    LEFT JOIN axe.products pr ON i.id = pr.inventory_id
    LEFT JOIN axe.product_type pt ON pt.id = pr.product_type_id
    LEFT JOIN axe.consumables_on_quote coq ON coq.id = i.consumables_on_quote_id
    LEFT JOIN axe.consumables c ON c.id = coq.consumables_id
    LEFT JOIN axe.quotes q ON (q.id = pr.quote_id OR q.id = coq.quote_id)
    LEFT JOIN axe.projects p ON p.id = q.project_id
    LEFT JOIN axe.clients client ON client.id = p.client_id
    WHERE p.id = :projectId
      AND i.delivery_note_id IS NULL
      AND i.date_out IS NULL
      AND (pr.status = 'completed' OR coq.id IS NOT NULL)""", nativeQuery = true)
    List<InventoryItemDTO> getAllItemsForProject(Long projectId);


    @Query("SELECT i FROM Inventory i " +
            "LEFT JOIN FETCH i.consumable " +
            "LEFT JOIN FETCH i.product " +
            "LEFT JOIN FETCH i.returnedProducts " +
            "LEFT JOIN FETCH i.deliveryNote " +
            "WHERE i.deliveryNote IS NULL")
    List<Inventory> findByDeliveryNoteIsNull();


    @Query(value = """
        SELECT\s
          i.id as inventoryId,
          i.date_in as dateIn,
          i.date_out as dateOut,
          i.delivery_note_id as deliveryNoteId,
          q.id as quoteId, q.date_accepted as quoteAcceptedDate,
          COALESCE(p.id, coq.id) AS itemId,
          c.name AS consumableName,
          pr.frame_name AS frameName,
          pr.frame_type AS frameType,
          pr.status as manufacturedProductStatus,
            CASE
                WHEN pr.id IS NOT NULL THEN 'Manufactured Product'
                WHEN coq.id IS NOT NULL THEN 'Consumable'
                ELSE 'Unknown'
            END AS itemType,
            IF(p.id IS NOT NULL, p.length, NULL) AS length,
            IF(coq.id IS NOT NULL, coq.qty, NULL) AS qtyOrdered,
            p.name AS projectName,
            client.name AS clientName
             FROM axe.customer_inventory i
             LEFT JOIN axe.products pr ON i.id = pr.inventory_id
             LEFT JOIN axe.consumables_on_quote coq ON coq.id = i.consumables_on_quote_id
             LEFT JOIN axe.consumables c ON c.id = coq.consumables_id
             LEFT JOIN axe.quotes q ON q.id = pr.quote_id OR q.id = coq.quote_id
             LEFT JOIN axe.projects p ON p.id = q.project_id
             LEFT JOIN axe.clients client ON client.id = p.client_id
        WHERE p.id = :projectId AND i.date_out IS NOT  NULL and i.returned_product_id IS NULL
    """, nativeQuery = true)
    List<InventoryItemDTO> getAllItemsForCreditNoteProject(Long projectId);

    // todo : check if this is correct
    // Adding back returned product to inventory
    @Query("SELECT i FROM Inventory i WHERE i.deliveryNote IS NULL OR i.returnedProducts IS NOT NULL")
    List<Inventory> findAvailableInventories();


    @Query(value = """
        SELECT i.* 
        FROM customer_inventory i 
        LEFT JOIN products pr ON i.id = pr.inventory_id
        LEFT JOIN consumables_on_quote coq ON i.consumables_on_quote_id = coq.id
        LEFT JOIN quotes q ON (pr.quote_id = q.id OR coq.quote_id = q.id)
        LEFT JOIN projects p ON q.project_id = p.id
        WHERE (i.delivery_note_id IS NULL OR i.returned_product_id IS NOT NULL) 
        AND p.id = :projectId
        """, nativeQuery = true)
    List<Inventory> findAvailableInventoriesInStockByProjectId(Long projectId);



    List<Inventory> findByIdIn(List<Long> ids);


}
