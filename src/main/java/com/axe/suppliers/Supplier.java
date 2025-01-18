package com.axe.suppliers;

import com.axe.purchaseOrders.PurchaseOrder;
import com.axe.steelCoils.SteelCoil;
import com.axe.suppliersAddresses.SupplierAddress;
import com.axe.suppliersEmails.SupplierEmail;
import com.axe.suppliersPhones.SupplierPhone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierAddress> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierEmail> emails = new ArrayList<>();

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierPhone> phones = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    @JsonIgnore
    private List<SteelCoil> steelCoils = new ArrayList<>();

}
