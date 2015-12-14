package com.zemiak.movies.batch.service;

import com.zemiak.movies.batch.plex.PlexProcess;
import com.zemiak.movies.batch.plex.PlexRecreateTask;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
public class PlexAliveScheduler {
    private static final Logger LOG = Logger.getLogger(PlexAliveScheduler.class.getName());

    @Resource(name = "java:jboss/mail/Default")
    private Session mailSession;

    @Inject PlexProcess process;
    @Inject String plexUrl;
    @Inject PlexRecreateTask recreator;
    @Inject private String mailTo;
    @Inject private String mailFrom;

    @Schedule(dayOfWeek="*", hour="5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,1,2", minute="*/5")
    public void checkPlexAlive() {
        if (!isAlive()) {
            try {
                process.stop();
            } catch (RuntimeException ex) {
                sendMail("Error stopping Plex", ex.getMessage());
                return;
            }

            try {
                process.start();
            } catch (RuntimeException ex) {
                sendMail("Error starting Plex", ex.getMessage());
                return;
            }

            sendMail("Plex has been restarted", "Successfuly");
        }
    }

    private boolean isAlive() {
        Client client = ClientBuilder.newClient();
        client.property("jersey.config.client.connectTimeout", 2000);
        client.property("jersey.config.client.readTimeout", 1000);

        WebTarget target = client.target(plexUrl);
        Response response;

        try {
            response = target.request(MediaType.APPLICATION_JSON).get();
        } catch (ProcessingException ex) {
            LOG.log(Level.SEVERE, "Plex not alive (cannot fetch)");
            return false;
        }

        if (! response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            String answer = response.readEntity(String.class);
            LOG.log(Level.SEVERE, "Plex not alive - status {0}", response.getStatus());
            return false;
        }

        return true;
    }

    private void sendMail(String subject, String body) {
        try {
            sendMail0(subject, body);
        } catch (MessagingException ex) {
            LOG.log(Level.SEVERE, "Cannot send email", ex);
        }
    }

    private void sendMail0(String subject, String body) throws MessagingException {
        final Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(mailFrom));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(subject);
        message.setText("Batch run ended on " + new Date());
        message.setText(body);

        Transport.send(message);

        LOG.log(Level.INFO, "Sent LOG file to {0}", mailTo);
    }
}
