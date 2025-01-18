package com.axe.credit_note;

import com.axe.credit_note.credit_notesDTO.CreditNoteDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CreditNoteRepository extends JpaRepository<CreditNote, Long> {

    @Query("SELECT dn FROM CreditNote dn WHERE dn.project.id = :projectId")
    List<CreditNote> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT new com.axe.credit_note.credit_notesDTO.CreditNoteDto(cn.id, cn.dateCreated, " +
            "rp.id, rp.reason, rp.returnedDate, " +
            "inv.id, inv.dateIn, inv.dateOut, " +
            "pr.id, pr.frameName, pr.totalLength, pr.frameType, pr.status ," +
            "coq.id, coq.qty, coq.sellPrice, cp.name, CASE\n" +
            "                    WHEN pr.id IS NOT NULL THEN 'Manufactured Product'\n" +
            "                    WHEN coq.id IS NOT NULL THEN 'Consumable'\n" +
            "                    ELSE 'Unknown'\n" +
            "                END ) " +
            "FROM CreditNote cn " +
            "JOIN cn.returnedProducts rp " +
            "JOIN rp.inventories inv " +
            "LEFT JOIN inv.product pr " +
            "LEFT JOIN inv.consumable coq " +
            "LEFT JOIN coq.consumable cp " +
            "WHERE cn.id = :id")
    List<CreditNoteDto> findCreditNoteDetailsById(@Param("id") Long id);



}
    
