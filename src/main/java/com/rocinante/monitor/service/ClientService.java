package com.rocinante.monitor.service;

import com.rocinante.monitor.entity.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    Client saveClient(Client client);

    String checkClients(String action);
}
