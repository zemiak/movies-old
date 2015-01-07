package com.zemiak.movies.batch.service.log;

import com.zemiak.movies.service.configuration.Configuration;
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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author vasko
 */
@Named("SendLogFile")
public class SendLogFile implements Batchlet {
    private static final Logger LOG = Logger.getLogger(SendLogFile.class.getName());

    @Inject
    JobContext jobCtx;

    @Resource(name = "java:/movies/mail/default")
    private Session mailSession;

    @Inject
    private Configuration conf;

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
        message.setFrom(new InternetAddress(conf.getMailFrom()));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(conf.getMailTo()));
        message.setSubject(conf.getMailSubject());
        message.setText("Batch run ended on " + new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();

        DataSource source = new FileDataSource(BatchLogger.getLogFileName());
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName("batch.log");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport.send(message);

        LOG.log(Level.INFO, "Sent LOG file to {0}", conf.getMailTo());

        //BatchLogger.deleteLogFile();
    }

    @Override
    public void stop() throws Exception {
    }
}
