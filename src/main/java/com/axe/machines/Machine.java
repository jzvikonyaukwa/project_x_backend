package com.axe.machines;

import com.axe.machineEvents.MachineEvent;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "machines")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MachineEvent> machineEvents = new ArrayList<>();

}
