package com.rocinante.monitor.service;

import com.rocinante.monitor.entity.Client;
import com.rocinante.monitor.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public String checkClients(String action) {
        int x = 3;
        if (action.equals("start")) {
            while (x > 0) {
                List<Client> clients = clientRepository.findAll();
                for (int i = 0; i < clients.size(); i++) {
                    try{
                        InetAddress address = InetAddress.getByName(clients.get(i).getIp());
                        boolean reachable = address.isReachable(10000);
                        String reach = reachable ? "yes" : "no";
                        if ((Objects.equals(clients.get(i).getStatus(), "up") && reachable) ||
                                ((Objects.equals(clients.get(i).getStatus(), "down")) && !reachable)
                        ) {
                            System.out.println("Is " + clients.get(i).getOwner() + " host reachable? " + reach);
                        } else {
                            // update database
                            Client client = clients.get(i);
                            client.setStatus(reachable ? "up" : "down");

                            clientRepository.save(client);

                            // send email for status change
                            sendEmail();
                            System.out.println("Is " + clients.get(i).getOwner() + " host reachable? " + reach);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                x--;
            }
        }
        else if (action.equals("stop")){
            return "stopped";
        } else {
            return "No action stated";
        }
        return "Done";
    }

    private void sendEmail() {
        System.out.println("Email sent because of status change");
    }
}
