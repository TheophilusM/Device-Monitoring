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
    public Client saveDevice(@RequestBody Client client){
        return clientService.saveClient(client);
    }

    @PostMapping("/update/{id}")
    public Client updateDevice(@PathVariable("id") Long id, @RequestBody String status){
        return clientService.updateClient(id, status);
    }

    @PostMapping("/stats/start")
    public String startDeviceCheck(@RequestBody String action){
        if (!action.equals("start")) return "Invalid action";
        return clientService.checkClients(action);
    }

    @PostMapping("/stats/stop")
    public String stopDeviceCheck(@RequestBody String action){
        if (!action.equals("stop")) return "Invalid action";
        return clientService.stopCheck(action);
    }
}
