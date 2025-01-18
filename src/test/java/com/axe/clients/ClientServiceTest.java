package com.axe.clients;

import com.axe.clients.clientDTOs.ClientUpdateRequestDTO;
import com.axe.clients.clientDTOs.ClientUpdateResponseDTO;

import com.axe.clients.exceptions.ClientNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import static com.axe.clients.ClientRepository.Specs.withId;
import static com.axe.clients.ClientRepository.Specs.withName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;



import org.mockito.InjectMocks;


import java.util.Optional;


class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;
    @Mock
    ClientRepository clientRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(clientRepository);
    }

    // @Test
    // void updateClient_ClientNotFound() {
    //     Long clientId = 1L;
    //     String clientName = "Jack";
    //     // given
    //     ClientUpdateRequestDTO request = new ClientUpdateRequestDTO(clientId, clientName);

    //     Specification<Client> spec = withId(clientId);
    //     Specification<Client> isNotCurrentClient = (root, query, cb) -> cb.notEqual(root.get("id"), clientId);
    //     isNotCurrentClient.and(withName(clientName));
    //     // when
    //     when(clientRepository.count(spec)).thenReturn(0L);
    //     // when(clientRepository.count(isNotCurrentClient.and(withName(clientName)))).thenReturn(0L);        
    //     // when(clientRepository.findOne(spec)).thenReturn(Optional.empty());

    //     // then
    //     assertThrows(ClientNotFoundException.class, () -> clientService.updateClient(request));
    //     verify(clientRepository).count(spec);
    //     // verify(clientRepository).findOne(spec);
    // }

    // @Test
    // void updateClient_Fail() {
    //     // given
    //     Client client = new Client();
    //     client.setId(1L);
    //     client.setName("Jotham");
    //     ClientUpdateRequestDTO request = new ClientUpdateRequestDTO(1L, "Jack");

    //     // when
    //     when(clientRepository.findOne(withId(1L))).thenReturn(Optional.of(client));
    //     doThrow(RuntimeException.class).when(clientRepository).save(client);

    //     // then
    //     assertThrows(RuntimeException.class, () -> clientService.updateClient(request));
    //     // verify(clientRepository).count(withId(1L));
    //     // verify(clientRepository).findOne(withId(1L));
    //     // verify(clientRepository).save(client);
    // }

    // @Test
    // void updateClient_Success() {
    //     long clientId = 1L;
    //     String clientName = "Jack";

    //     // given
    //     Client client = new Client();
    //     client.setId(1L);
    //     client.setName("Jotham");

    //     ClientUpdateRequestDTO request = new ClientUpdateRequestDTO(clientId, clientName);

    //     Specification<Client> spec = withId(clientId);
    //     System.out.println("spec: " + spec);
    //     Specification<Client> isNotCurrentClient = (root, query, cb) -> cb.notEqual(root.get("id"), clientId);
    //     isNotCurrentClient.and(withName(clientName));

    //     // when
        
    //     // when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

    //     // when(clientRepository.count(spec)).thenReturn(1L);
    //     // when(clientRepository.count(isNotCurrentClient.and(withName(clientName)))).thenReturn(0L);        
    //     // when(clientRepository.findOne(withId(1L))).thenReturn(Optional.of(client));
    //     // when(clientRepository.findOne(spec)).thenReturn(Optional.empty());
    //     // when(clientRepository.save(null)).thenReturn(client);

    //     // then
    //     ClientUpdateResponseDTO result = clientService.updateClient(request);

    //     // Assert
    //     // assertNotNull(result);
    //     // assertEquals("Jack", result.name());
    //     // verify(clientRepository).count(withId(1L));
    //     // verify(clientRepository).findOne(withId(1L));
    //     // verify(clientRepository).save(client);
    //     // verify(clientRepository).findById(1L);

    // }
}




