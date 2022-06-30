package com.rocinante.monitor.controller;

import com.rocinante.monitor.entity.Client;
import com.rocinante.monitor.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/devices")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public List<Client> getAllDevices(){
        return clientService.getAllClients();
    }

    @PostMapping("/save")
    public String saveDevice(){
        return "Successful";
    }
}
