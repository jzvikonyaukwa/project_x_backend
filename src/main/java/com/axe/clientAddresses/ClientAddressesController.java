package com.axe.clientAddresses;

import com.axe.clientAddresses.clientAdressDTOs.ClientAddressDTO;
import com.axe.clientAddresses.clientAdressDTOs.ClientAddressPostDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//todo to be properly done in the Clients package and controller
@RestController
@RequestMapping("/api/client-addresses")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "http://axebuild.io"})
public class ClientAddressesController {

    private final ClientAddressesService clientAddressesService;

    public ClientAddressesController(ClientAddressesService clientAddressesService) {
        this.clientAddressesService = clientAddressesService;
    }

    @GetMapping("all-addresses")
    public List<ClientAddress> getAllClientsAddresses(){
        return clientAddressesService.getAllClientsAddresses();
    }

    @GetMapping("address-for-client/{clientId}")
    public List<ClientAddressDTO> getAllAddressesForClient(@PathVariable Long clientId){
        return clientAddressesService.getAllAddressesForClient(clientId);
    }

    @PatchMapping("update-addresses")
    public List<ClientAddress> updateClientAddress(@RequestBody List<ClientAddressPostDTO> clientAddresses){
        return clientAddressesService.updateClientAddress(clientAddresses);
    }

    @DeleteMapping("{id}")
    public void deleteClientAddress(@PathVariable Long id){
        clientAddressesService.deleteClientAddress(id);
    }
}
