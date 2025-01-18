package com.axe.clients;

import com.axe.clients.clientDTOs.ClientUpdateRequestDTO;
import com.axe.clients.clientDTOs.ClientUpdateResponseDTO;
import com.axe.quotes.Quote;
import com.axe.utilities.ClientVisibleException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = { "http://localhost:4200", "http://axebuild.io", "https://axebuild.io" })
public class ClientController {

    private final ClientService clientService;
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("all-clients")
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("all-clients-with-details")
    public List<ClientDetails> getAllClientsWithDetails() {
        return clientService.getAllClientsWithDetails();
    }

    @GetMapping("get-client-by-id/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PostMapping("")
    public Client addClient(@RequestBody Client client) {
        try {
            return clientService.addClient(client);
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while adding client");
        }
    }

    @GetMapping("cliets-quotes")
    public List<Quote> getClientsQuotes() {
        return clientService.getClientsQuotes();
    }

    @PatchMapping("update-client")
    public ResponseEntity<ClientUpdateResponseDTO> updateClientDetails(
            @RequestBody ClientUpdateRequestDTO clientDTO) {
        try {
            return ResponseEntity.ok(clientService.updateClient(clientDTO));
        } catch (ClientVisibleException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException("Server encountered an error while updating client");
        }
    }
}
