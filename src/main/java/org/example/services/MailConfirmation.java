package org.example.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Transport;

public class MailConfirmation {


    public MailConfirmation() {    }

    public void sendEmail(String toEmailAddress, String fromEmailAddress, String appPassword, String subject,
                          String bodyText) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.host", "smtp.mail.ru");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getDefaultInstance(prop);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress)); // setting "TO" email address
            message.setSubject(subject); // setting subject
            message.setContent(bodyText, "text/html"); // setting body
            Transport t = session.getTransport("smtp");
            t.connect(fromEmailAddress,  appPassword);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
