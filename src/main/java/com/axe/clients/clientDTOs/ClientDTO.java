package com.axe.clients.clientDTOs;

import com.axe.clients.Client;

public interface ClientDTO {
     static   ClientUpdateResponseDTO mapToResponseBuilder(Client client){
        return ClientUpdateResponseDTO
                .builder()
                .id(client.getId())
                .name(client.getName())
//                .notes(client.getNotes())
                .build();
    }
}
