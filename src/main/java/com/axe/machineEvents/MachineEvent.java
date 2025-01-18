package com.axe.machineEvents;

import com.axe.machines.Machine;
import com.axe.steelCoils.SteelCoil;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "machine_events")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MachineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "loaded_time")
    private LocalDateTime loadedTime;

    @Column(name = "unloaded_time")
    private LocalDateTime unloadedTime;

    @Column(name = "total_meters_cut")
    private Float totalMetersCut;

    @Column(name = "cuts_made")
    private Integer cutsMade;

    @ManyToOne()
    @JoinColumn(name = "machine_id")
    @JsonBackReference
    private Machine machine;

    @ManyToOne()
    @JoinColumn(name = "steel_coil_id")
    private SteelCoil steelCoil;
}
