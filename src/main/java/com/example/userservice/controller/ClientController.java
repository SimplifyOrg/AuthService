package com.example.userservice.controller;

import com.example.userservice.DTOs.ClientRegisterRequestDTO;
import com.example.userservice.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public HttpStatus createRole(@RequestBody ClientRegisterRequestDTO clientRegisterRequestDTO) {
        clientService.registerClient(clientRegisterRequestDTO);
        return HttpStatus.OK;
    }
}
