package com.axe.finishes;

import com.axe.colors.Color;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "finishes")
@Getter
@Setter
@NoArgsConstructor
public class Finish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "finish", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Color> colors = new ArrayList<>();

}
