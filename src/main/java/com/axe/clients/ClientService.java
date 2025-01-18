package com.axe.clients;

import com.axe.clientAddresses.ClientAddress;
import com.axe.clientEmails.ClientEmail;
import com.axe.clientPhones.ClientPhone;
import com.axe.clients.clientDTOs.ClientDTO;
import com.axe.clients.clientDTOs.ClientUpdateRequestDTO;
import com.axe.clients.clientDTOs.ClientUpdateResponseDTO;
import com.axe.common.utils.PatchUtils;
import com.axe.quotes.Quote;
import com.axe.utilities.ClientVisibleException;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.axe.clients.ClientRepository.Specs.withId;
import static com.axe.clients.ClientRepository.Specs.withName;

@Service
public class ClientService implements PatchUtils, ClientDTO {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<ClientDetails> getAllClientsWithDetails() {
        return clientRepository.getAllClientsWithDetails();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Transactional
    public Client addClient(Client client) {
        log.debug("Adding new Client {}", client); 

        // Check if client name is taken, ignoring blanks and trimming whitespace
        Optional<String> optionalClientName = Optional.ofNullable(client.getName());
        if (optionalClientName.isEmpty()) {
            throw new ClientVisibleException("Client name is required");
        }
        String clientName = optionalClientName.get().trim();
        if (clientName.isBlank()) {
            throw new ClientVisibleException("Client name is required");
        }
        boolean isClientNameTaken = clientName.isBlank() ? false
                : clientRepository.count( //
                        withName(clientName)) > 0;
        if (isClientNameTaken) {
            throw new ClientVisibleException("Client with name [%s] already exists".formatted(clientName));
        }

        Client newClient = new Client();
        newClient.setName(client.getName());
        newClient.setNotes(client.getNotes());

        Client newSavedClient = clientRepository.save(newClient);

        for(ClientAddress clientAddress : client.getAddresses()){
            clientAddress.setClient(newSavedClient);
            newSavedClient.getAddresses().add(clientAddress);
        }

        for(ClientPhone clientPhone : client.getPhones()){
            clientPhone.setClient(newSavedClient);
            newSavedClient.getPhones().add(clientPhone);
        }

        for (ClientEmail clientEmail : client.getEmails()){
            clientEmail.setClient(newSavedClient);
            newSavedClient.getEmails().add(clientEmail);
        }


        return clientRepository.save(newSavedClient);
    }

    public List<Quote> getClientsQuotes() {
        return null;
    }

    @Transactional
    public ClientUpdateResponseDTO updateClient(@NotNull ClientUpdateRequestDTO client) {
        log.debug("Updating Client with id [%s]".formatted(client.id()));

        boolean isClientPresent = clientRepository.count(withId(client.id())) > 0;
        if (isClientPresent == false) {
            throw new ClientVisibleException("Client with id [%s] not found".formatted(client.id()));
        }

        final Specification<Client> isNotCurrentClient = (root, query, cb) -> cb.notEqual(root.get("id"), client.id());

        // Check if client name is taken, ignoring blanks and trimming whitespace
        String clientName = client.name().trim();
        boolean isClientNameTaken = clientName.isBlank() ? false
                : clientRepository.count( //
                        isNotCurrentClient.and(withName(clientName))) > 0;
        if (isClientNameTaken) {
            throw new ClientVisibleException("Client with name [%s] already exists".formatted(clientName));
        }

        // Fetch the existing client and apply the update
        Client existingClient = clientRepository.findOne(withId(client.id()))
                .map(param -> patch(param, client))
                .orElseThrow(
                        () -> new ClientVisibleException("Client with id [%s] not found".formatted(client.id())));

        // Save the updated client and log success
        if (clientRepository.save(existingClient) != null) {
            log.info("Client Updated Successfully");
        }

        return ClientDTO.mapToResponseBuilder(existingClient);
    }

}
