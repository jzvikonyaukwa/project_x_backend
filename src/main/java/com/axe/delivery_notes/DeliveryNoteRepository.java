package com.axe.delivery_notes;

import com.axe.delivery_notes.delivery_notesDTOs.ProjectInformation;
import com.axe.delivery_notes.delivery_notesDTOs.delivery_notes_inventories.DeliveryNoteInventoryDto;
import com.axe.delivery_notes.delivery_notesDTOs.DeliveryNoteItemDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> //
, JpaSpecificationExecutor<DeliveryNote> {

    @Query(value = """
       SELECT
           d.id as deliveryNoteId,
           d.date_created as dateCreated,
           d.date_delivered as dateDelivered,
           d.delivery_address as deliveryAddress,
           d.status as status,
           i.id as inventoryId,
           i.date_in as inventoryDateIn,
           i.date_out as inventoryDateOut,
           p.id as manufacturedId,
           p.frame_type as frameType,
           p.frame_name as frameName,
           p.total_length as totalLength,
           p.total_quantity as manufacturedQuantity,
           pt.name as productName,
           pt.id as productTypeId,
           pt.code as code,
           coq.qty as quantity,
           coq.unit_price as price,
           c.name as name,
           c.id as consumableId
          FROM
              delivery_notes d
            JOIN
                  customer_inventory i on d.id = i.delivery_note_id
            LEFT JOIN
                  products p on i.id = p.inventory_id
            LEFT JOIN
                  product_type pt ON pt.id = p.product_type_id
            LEFT JOIN
                  consumables_on_quote coq on coq.id=i.consumables_on_quote_id
            LEFT JOIN
                  consumables c on c.id =coq.consumables_id
          where d.project_id = :projectId
""", nativeQuery = true)
    List<DeliveryNoteInventoryDto> findByProjectId(@Param("projectId") Long projectId);

    @Query(value = """
    SELECT new com.axe.delivery_notes.delivery_notesDTOs.DeliveryNoteItemDTO(
      i.id,
      i.dateIn,
      i.dateOut,
      i.deliveryNote.id,
      q.id,
      q.dateAccepted,
      pr.id,
      pr.status,
            c.name,
      pr.frameName,
      pr.frameType,
      pr.status,
      CASE
          WHEN pr.id IS NOT NULL THEN 'Manufactured Product'
          WHEN coq.id IS NOT NULL THEN 'Consumable'
          ELSE 'Unknown'
      END,
      pr.totalLength,
      CAST(coq.qty AS integer),
      p.name,
      client.name
    )
    FROM Inventory i
    LEFT JOIN Product pr ON i.id = pr.inventory.id
    LEFT JOIN ConsumableOnQuote coq ON coq.id = i.consumable.id
    LEFT JOIN Consumable c ON c.id = coq.consumable.id
    LEFT JOIN Quote q ON q.id = pr.quote.id OR q.id = coq.quote.id
    LEFT JOIN Project p ON p.id = q.project.id
    LEFT JOIN Client client ON client.id = p.client.id
    WHERE i.deliveryNote.id = :deliveryNoteId
""")
    List<DeliveryNoteItemDTO> getDeliveryNoteItemsForUpdate(@Param("deliveryNoteId") Long deliveryNoteId);



    @Query(value = """
        SELECT p.id as projectId, p.name
        FROM axe.projects p
        JOIN axe.delivery_notes dn ON dn.project_id = p.id
        WHERE dn.id = :id \s
        """, nativeQuery = true)
    ProjectInformation getProjectInfoByDeliveryNoteId(Long id);


    @Query("SELECT dn FROM DeliveryNote dn " +
            "LEFT JOIN FETCH dn.inventories " +
            "WHERE dn.project.id = :projectId")
    List<DeliveryNote> findDeliveryNotesByProjectId(@Param("projectId") Long projectId);

    interface Specs {
        /**
         * Filter Delivery Note by ID.
         * 
         * @param id The ID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<DeliveryNote> withId(Number id) {
            return (root, query, builder) -> builder.equal(root.get("id"), id);
        }

        /**
         * Filter Delivery Note by status.
         * 
         * @param status The status to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<DeliveryNote> withStatus(Status status) {
            return (root, query, builder) -> builder.equal(root.get("status"), status);
        }
        
        /**
         * Filter Delivery Note by clientID.
         * 
         * @param id The clientID to filter by.
         * @return A Specification object representing the filter criteria.
         */
        static Specification<DeliveryNote> withClientId(Number clientID) {
            return (root, query, builder) -> builder.equal(root.get("project").get("client").get("id"), clientID);
        }
    }
}
