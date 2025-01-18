package com.axe.delivery_notes.delivery_notesDTOs;

import com.axe.delivery_notes.Status;

import lombok.Data;

import java.time.LocalDate;


@Data
public class DeliveryNoteGetAllDto {
   private Long id;
   private LocalDate dateCreated;
   private LocalDate dateDelivered;
   private Status status;
   private String deliveryAddress;

   public String getStatus() {
         return status == null ? null : status.name();
   } 
} 