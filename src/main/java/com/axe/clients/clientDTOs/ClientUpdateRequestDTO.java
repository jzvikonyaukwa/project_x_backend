package com.axe.clients.clientDTOs;


// should not do this
public record ClientUpdateRequestDTO(long id,
                                     String name
//                                     String notes
//                                     List<ClientAddress> addresses,
//                                     List<ClientPhone> phones,
//                                     List<ClientEmail> emails
) {
}
