package com.axe.interBranchTransfer.steelCoils;

import com.axe.steelCoils.SteelCoil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "steel_coil_inter_branch_transfer")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SteelCoilInterBranchTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "mtrs")
    private BigDecimal mtrs;

    @ManyToOne()
    @JoinColumn(name = "steel_coil_id_from")
    private SteelCoil steelCoilFrom;

    @ManyToOne()
    @JoinColumn(name = "steel_coil_id_to")
    @JsonIgnore
    private SteelCoil steelCoilTo;

}
