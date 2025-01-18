package com.axe.clientPhones;

import com.axe.clientPhones.clientPhoneDTOs.ClientPhonePostDTO;
import com.axe.clients.ClientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientPhonesService {

    private final ClientPhonesRepository clientPhonesRepository;
    private final ClientService clientsService;

    public ClientPhonesService(ClientPhonesRepository clientPhonesRepository, ClientService clientsService) {
        this.clientPhonesRepository = clientPhonesRepository;
        this.clientsService = clientsService;
    }

    public List<ClientPhone> getAllClientPhones() {
        return clientPhonesRepository.findAll();
    }

    public ClientPhone savePhone(ClientPhone phone) {
        return clientPhonesRepository.save(phone);
    }

    public List<ClientPhone> updateClientPhones(List<ClientPhonePostDTO> clientPhones) {
        List<ClientPhone> savedPhones = new ArrayList<>();

        for(ClientPhonePostDTO phone : clientPhones) {

            if(phone.getDelete() && phone.getId() != null){
                deleteClientPhone(phone.getId());
                continue;
            }

            ClientPhone clientPhone = convertToClientPhone(phone);
            savedPhones.add(savePhone(clientPhone));
        }

        return savedPhones;
    }

    void deleteClientPhone(Long id) {
        clientPhonesRepository.deleteById(id);
    }

    public ClientPhone convertToClientPhone(ClientPhonePostDTO dto) {
        ClientPhone clientPhone = new ClientPhone();
        clientPhone.setId(dto.getId());
        clientPhone.setPhone(dto.getPhone());
        clientPhone.setLabel(dto.getLabel());
        clientPhone.setClient(clientsService.getClientById(dto.getClientId()));
        return clientPhone;
    }
}
