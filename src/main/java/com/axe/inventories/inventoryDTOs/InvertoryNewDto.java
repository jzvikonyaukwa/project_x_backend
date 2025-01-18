package com.axe.inventories.inventoryDTOs;

import java.time.LocalDate;

import com.axe.colors.Color;
import com.axe.consumablesOnQuote.ConsumableOnQuote;
import com.axe.delivery_notes.DeliveryNote;
import com.axe.gauges.Gauge;
import com.axe.product.Product;
import com.axe.profile.Profile;
import com.axe.returned_products.ReturnedProducts;
import com.axe.width.Width;

import lombok.Data;

@Data
public class InvertoryNewDto {
   private Long id;

   private LocalDate dateIn;

   private  LocalDate dateOut;

   private ConsumableOnQuote consumable;

   private Product product;

   private ReturnedProducts returnedProducts;

   private  DeliveryNote deliveryNote;

    // Getting Colors, Widths, Gauge and Profile - Marked Transient not to allow
    // them to be serialised to DB
    // @Transient
    // public 
    private   Color color;
    // {
    // return this.manufacturedProduct != null &&
    // this.manufacturedProduct.getCuttingList() != null
    // ? this.manufacturedProduct.getCuttingList().getColor() : null;
    // }
    //
    // @Transient
    // public 
    private  Width width;
    // {
    // return this.manufacturedProduct != null &&
    // this.manufacturedProduct.getCuttingList() != null
    // ? this.manufacturedProduct.getCuttingList().getWidth() : null;
    // }
    //
    // @Transient
    // public 
    private  Gauge gauge;
    //  {
    // return this.manufacturedProduct != null &&
    // this.manufacturedProduct.getCuttingList() != null
    // ? this.manufacturedProduct.getCuttingList().getGauge() : null;
    // }
    //
    // @Transient
    // public 
    private Profile profile;
    // {
    // return this.manufacturedProduct != null &&
    // this.manufacturedProduct.getCuttingList() != null
    // ? this.manufacturedProduct.getCuttingList().getProfile() : null;
    // }
}