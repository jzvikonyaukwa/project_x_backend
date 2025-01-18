package com.axe.delivery_notes.delivery_notesDTOs;

import com.axe.delivery_notes.Status;
import com.axe.inventories.inventoryDTOs.InvertoryNewDto;
import com.axe.projects.projectDTO.project_overviewDTO.ProjectDTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeliveryNoteNewDto {
    private Long id;

    private  LocalDate dateCreated;

    private   LocalDate dateDelivered;

    private   String deliveryAddress;

    private   Status status;

    private   ProjectDTO project;

    private  List<InvertoryNewDto> inventories;
}