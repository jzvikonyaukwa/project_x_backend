package com.axe.clientPhones;

import com.axe.clientPhones.clientPhoneDTOs.ClientPhonePostDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//todo to be properly done in the Clients package and controller
@RestController
@RequestMapping("/api/client-phones")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "http://axebuild.io"})
public class ClientPhonesController {

    private final ClientPhonesService clientPhonesService;

    public ClientPhonesController(ClientPhonesService clientPhonesService) {
        this.clientPhonesService = clientPhonesService;
    }

    @GetMapping("all-phones")
    public List<ClientPhone> getAllClientPhones(){
        return clientPhonesService.getAllClientPhones();
    }

    @PatchMapping("update-phones")
    public List<ClientPhone> updateClientPhones(@RequestBody List<ClientPhonePostDTO> clientPhones){
        return clientPhonesService.updateClientPhones(clientPhones);
    }

    @DeleteMapping("{id}")
    public void deleteClientPhone(@PathVariable Long id){
        clientPhonesService.deleteClientPhone(id);
    }
}
