package com.axe.clientEmails;

import com.axe.clientEmails.clientEmailDTOs.ClientEmailPostDTO;
import com.axe.clients.ClientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientEmailService {

    private final ClientEmailRepository clientEmailRepository;
    private final ClientService clientsService;


    public ClientEmailService(ClientEmailRepository clientEmailRepository, ClientService clientsService) {
        this.clientEmailRepository = clientEmailRepository;
        this.clientsService = clientsService;
    }


    public List<ClientEmail> getAllEmails() {
        return clientEmailRepository.findAll();
    }

    public ClientEmail saveEmail(ClientEmail email) {
        return clientEmailRepository.save(email);
    }

    public List<ClientEmail> updateClientEmails(List<ClientEmailPostDTO> clientEmails) {
        List<ClientEmail> savedEmails = new ArrayList<>();

        for(ClientEmailPostDTO email : clientEmails) {

            if(email.getDelete() && email.getId() != null) {
                deleteEmail(email.getId());
                continue;
            }
            ClientEmail clientEmail = convertToClientEmail(email);
            savedEmails.add(saveEmail(clientEmail));
        }

        return savedEmails;
    }

    private void deleteEmail(Long id) {
        clientEmailRepository.deleteById(id);
    }


    public ClientEmail convertToClientEmail(ClientEmailPostDTO dto) {
        ClientEmail clientEmail = new ClientEmail();
        clientEmail.setId(dto.getId());
        clientEmail.setEmail(dto.getEmail());
        clientEmail.setLabel(dto.getLabel());
        clientEmail.setClient(clientsService.getClientById(dto.getClientId()));
        return clientEmail;
    }

    public void deleteClientEmail(Long id) {
        clientEmailRepository.deleteById(id);
    }
}
