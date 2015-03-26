package com.zemiak.movies.batch.service.log;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.batch.api.Batchlet;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Named("SendLogFile")
public class SendLogFile implements Batchlet {
    private static final Logger LOG = Logger.getLogger(SendLogFile.class.getName());

    @Inject
    private JobContext jobCtx;

    @Resource(name = "java:/movies/mail/default")
    private Session mailSession;

    @Inject private String mailSubject;
    @Inject private String mailTo;
    @Inject private String mailFrom;

    public SendLogFile() {
    }

    @Override
    public String process() throws Exception {
        final File file = new File(BatchLogger.getLogFileName());
        if (! file.exists()) {
            LOG.log(Level.INFO, "Log file does not exist, not sending...");
            return "does-not-exist";
        }

        if (file.length() == 0) {
            LOG.log(Level.INFO, "Log file is empty, not sending...");
            return "zero-size";
        }

        sendLogFile();

        return "sent";
    }

    private void sendLogFile() throws MessagingException {
        final Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(mailFrom));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
        message.setSubject(mailSubject);
        message.setText("Batch run ended on " + new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        DataSource source = new FileDataSource(BatchLogger.getLogFileName());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("batch.log");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);

        LOG.log(Level.INFO, "Sent LOG file to {0}", mailTo);

        //BatchLogger.deleteLogFile();
    }

    @Override
    public void stop() throws Exception {
    }
}
