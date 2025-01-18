package com.axe.interBranchTransfer.steelCoils;

import com.axe.interBranchTransfer.DTOs.ConsumableInterBranchTransferDetails;
import com.axe.interBranchTransfer.DTOs.InterBranchTransferDetails;
import com.axe.interBranchTransfer.DTOs.SteelCoilInterBranchTransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SteelCoilInterBranchTransferRepository extends JpaRepository<SteelCoilInterBranchTransfer, Long> {

    @Query(value= """
            SELECT
                ibt.id as interBranchTransferId,
                ibt.date as dateTransferred,
                ibt.mtrs as mtrsTransferred,
            	sc_from.id AS steelCoilIdFrom,
            	sc_from.coil_number AS coilNumberFrom,
            	sc_from.card_number AS cardNumberFrom,
            	sc_from.landed_cost_per_mtr AS landedCostPerMtrFrom,
            	sc_to.id AS steelCoilIdTo,
            	sc_to.coil_number AS coilNumberTo,
            	sc_to.card_number AS cardNumberTo,
            	sc_to.landed_cost_per_mtr AS landedCostPerMtrTo,
                ss.ISQ_grade AS ISQGrade,
            	ss.coating, w.width AS width, color.color AS color, gauge.gauge
            FROM\s
            	axe.steel_coil_inter_branch_transfer AS ibt
            JOIN
            	axe.steel_coils AS sc_from ON sc_from.id = ibt.steel_coil_id_from
            JOIN
            	axe.steel_coils AS sc_to ON sc_to.id = ibt.steel_coil_id_to
            JOIN
            	axe.steel_specifications AS ss ON ss.id = sc_from.steel_specification_id
            JOIN
            	axe.colors AS color ON color.id = ss.color_id
            JOIN
            	axe.gauges AS gauge ON gauge.id = ss.gauge_id
            JOIN
            	axe.widths AS w ON w.id = ss.width_id                         
             """, nativeQuery = true)
    List<SteelCoilInterBranchTransferDetails> getAllSteelCoilInterBranchTransferDetails();

    @Query(value= """
            SELECT
                
                cibt.id as interBranchTransferId, date as dateTransferred, cibt.qty as qtyTransferred,
                ciw_from.id as consumableInWarehouseFromId, ciw_from.avg_landed_price as consumableInWarehouseFromAvgLandedPrice,\s
                ciw_from.qty as consumableInWarehouseFromQty,
                ciw_to.id as consumableInWarehouseToId, ciw_to.avg_landed_price as consumableInWarehouseToAvgLandedPrice,\s
                ciw_to.qty as consumableInWarehouseToqty,
                c.id as consumableId, c.serial_number as serialNumber, c.name as consumableName
            FROM\s
                axe.consumable_inter_branch_transfer as cibt
            JOIN\s
                axe.consumables_in_warehouse as ciw_from ON cibt.consumable_in_warehouse_id_from = ciw_from.id
            JOIN\s
                axe.consumables_in_warehouse as ciw_to ON cibt.consumable_in_warehouse_id_to = ciw_to.id
            JOIN\s
                axe.consumables as c ON c.id = ciw_from.consumable_id
          """, nativeQuery = true)
    List<ConsumableInterBranchTransferDetails> getAllConsumableInterBranchTransferDetails();

    @Query(value= """
            SELECT
                ibt.id as interBranchTransferId,
                ibt.date as dateTransferred,
                ibt.mtrs as mtrsTransferred,
                sc_from.id AS steelCoilIdFrom,
                sc_from.coil_number AS coilNumberFrom,
                sc_from.card_number AS cardNumberFrom,
                sc_to.id AS steelCoilIdTo,
                sc_to.coil_number AS coilNumberTo,
                sc_to.card_number AS cardNumberTo
            FROM
                axe.steel_coil_inter_branch_transfer AS ibt
            JOIN
                axe.steel_coils AS sc_from ON sc_from.id = ibt.steel_coil_id_from
            JOIN
                axe.steel_coils AS sc_to ON sc_to.id = ibt.steel_coil_id_to
            WHERE sc_from.id = :steelCoilId OR sc_from.id = :steelCoilId
    """, nativeQuery = true)
    List<InterBranchTransferDetails> getSteelCoilInterBranchTransferDetailsForSteelCoil(Long steelCoilId);

}
