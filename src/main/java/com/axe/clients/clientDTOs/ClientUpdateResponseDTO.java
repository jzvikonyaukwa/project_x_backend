package com.axe.clients.clientDTOs;


import lombok.Builder;



@Builder
public record ClientUpdateResponseDTO(
        long id,
        String name
//        String notes
//        List<ClientAddress> addresses,
//        List<ClientPhone> phones,
//        List<ClientEmail> emails
) {
}
