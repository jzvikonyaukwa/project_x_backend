package com.axe.consumables;


import com.axe.consumable_category.ConsumableCategory;
import com.axe.consumablesInWarehouse.ConsumablesInWarehouse;
import com.axe.consumablesOnPurchaseOrder.ConsumablesOnPurchaseOrder;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.consumableSource.SourceCountry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consumables")
@Getter
@Setter
@NoArgsConstructor
public class Consumable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "uom")
    private String uom;

    @Column(name = "min_qty_alert_owned")
    private Integer minQtyAlertOwned;

    @Column(name = "min_qty_alert_consignment")
    private Integer minQtyAlertConsignment;

    @ManyToOne()
    @JoinColumn(name = "source_country_id")
    private SourceCountry sourceCountry;

    @ManyToOne
    @JoinColumn(name = "consumable_category_id")
    private ConsumableCategory category;

    @OneToMany(mappedBy = "consumable", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ConsumableOnQuote> consumableOnQuote = new ArrayList<>();

    @OneToMany(mappedBy = "consumable", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ConsumablesOnPurchaseOrder> consumablesOnPurchaseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "consumable", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ConsumablesInWarehouse> consumablesInWarehouses = new ArrayList<>();

    @Override
    public String toString() {
        return "Consumable{" +
                "id=" + id +
                ", serialNumber='" + serialNumber + '\'' +
                ", name='" + name + '\'' +
                ", uom='" + uom + '\'' +
                ", minQtyAlertOwned=" + minQtyAlertOwned +
                ", minQtyAlertConsignment=" + minQtyAlertConsignment +
                '}';
    }
}
