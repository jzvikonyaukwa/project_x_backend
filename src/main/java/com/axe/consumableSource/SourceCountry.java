package com.axe.consumableSource;

import com.axe.consumables.Consumable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "source_country")
@Getter
@Setter
@NoArgsConstructor
public class SourceCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "sourceCountry", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Consumable> consumables = new ArrayList<>();
}
