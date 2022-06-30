package com.rocinante.monitor.service;

import com.rocinante.monitor.entity.Client;
import com.rocinante.monitor.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {
    static int x = 3;

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
                        sendEmail(client.getEmail(), client.getStatus());
//                        System.out.println("Is " + clients.get(i).getOwner() + " host reachable? " + reach);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            x--;
}
        return "Done";
    }

    @Override
    public String stopCheck(String action) {
        x = 0;
        return "Stopped";
    }

    @Override
    public Client updateClient(Long id, String status) {
        Client client;
        Optional<Client> optional = clientRepository.findById(id);
        if (optional.isPresent()) {
            client = optional.get();
            client.setStatus(status);
            clientRepository.save(client);

        }
        return optional.get();
    }

    private void sendEmail(String email, String status) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("berealanko@gmail.com", "hfmucszqckgltzfx");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("berealanko@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("AP email");
        msg.setContent("You device is " + status, "text/html");
        msg.setDescription("You device is " + status);
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent("Tutorials point email", "text/html");

//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        MimeBodyPart attachPart = new MimeBodyPart();

//        attachPart.attachFile("/var/tmp/image19.png");
//        multipart.addBodyPart(attachPart);
//        msg.setContent(multipart);
        Transport.send(msg);
        System.out.println("Email sent because of status change");
    }

}
