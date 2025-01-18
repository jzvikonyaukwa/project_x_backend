package com.axe.clientAddresses;

import com.axe.clientAddresses.clientAdressDTOs.ClientAddressDTO;
import com.axe.clientAddresses.clientAdressDTOs.ClientAddressPostDTO;
import com.axe.clients.ClientService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientAddressesService {

    private final ClientAddressesRepository clientAddressesRepository;
    private final ClientService clientsService;

    public ClientAddressesService(ClientAddressesRepository clientAddressesRepository, ClientService clientsService) {
        this.clientAddressesRepository = clientAddressesRepository;
        this.clientsService = clientsService;
    }

    public ClientAddress saveAddress(ClientAddress address) {
        return clientAddressesRepository.save(address);
    }

    public List<ClientAddress> getAllClientsAddresses() {
        return clientAddressesRepository.findAll();
    }

    public List<ClientAddressDTO> getAllAddressesForClient(Long clientID){
        return clientAddressesRepository.findAllAddressesForClient(clientID);
    }

    public List<ClientAddress> updateClientAddress(List<ClientAddressPostDTO> clientAddressesPosted) {

        List<ClientAddress> updatedAddresses = new ArrayList<>();

        for(ClientAddressPostDTO address : clientAddressesPosted) {

            if(address.getDelete() && address.getId() != null){
                deleteAddress(address.getId());
                continue;
            }

            ClientAddress clientAddress = convertToClientAddress(address);
            updatedAddresses.add(saveAddress(clientAddress));
        }

        return updatedAddresses;
    }

    public void deleteAddress(Long id) {
        clientAddressesRepository.deleteById(id);
    }

    public ClientAddress convertToClientAddress(ClientAddressPostDTO dto) {
        ClientAddress clientAddress = new ClientAddress();
        clientAddress.setId(dto.getId());
        clientAddress.setStreet(dto.getStreet());
        clientAddress.setSuburb(dto.getSuburb());
        clientAddress.setCity(dto.getCity());
        clientAddress.setCountry(dto.getCountry());
        clientAddress.setLabel(dto.getLabel());
        clientAddress.setClient(clientsService.getClientById(dto.getClientId()));
        return clientAddress;
    }

    public void deleteClientAddress(Long id) {
        clientAddressesRepository.deleteById(id);
    }
}
