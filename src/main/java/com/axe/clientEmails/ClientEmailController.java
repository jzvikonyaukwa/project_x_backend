package com.axe.clientEmails;

import com.axe.clientEmails.clientEmailDTOs.ClientEmailPostDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//todo: to be properly done in the Clients package and controller
@RestController
@RequestMapping("/api/client-emails")
@CrossOrigin(origins = {"http://localhost:4200", "https://axebuild.io", "http://axebuild.io"})
public class ClientEmailController {

    private final ClientEmailService clientEmailService;

    public ClientEmailController(ClientEmailService clientEmailService) {
        this.clientEmailService = clientEmailService;
    }

    @GetMapping("all-emails")
    public List<ClientEmail> getAllEmails(){
        return clientEmailService.getAllEmails();
    }

    @PatchMapping("update-emails")
    public List<ClientEmail> updateClientEmails(@RequestBody List<ClientEmailPostDTO> clientEmails){
        return clientEmailService.updateClientEmails(clientEmails);
    }

    @DeleteMapping("{id}")
    public void deleteClientEmail(@PathVariable Long id){
        clientEmailService.deleteClientEmail(id);
    }
}
