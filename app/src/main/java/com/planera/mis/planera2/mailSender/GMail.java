package com.planera.mis.planera2.mailSender;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMail extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return  new PasswordAuthentication(fromEmail, fromPassword);
    }

    final String emailPort = "465";// gmail's smtp port
    final String smtpAuth = "true";
    final String starttls = "true";
    final String emailHost = "smtp.gmail.com";

    private String fromEmail;
    private String fromPassword;
    private String toEmail;
    private String emailSubject;
    private String emailBody;
    private File fileName;

    private Properties emailProperties;
    private Session mailSession;
    private MimeMessage emailMessage;

    public GMail() {

    }

    public GMail(String fromEmail, String fromPassword,
                 String toEmail, String emailSubject, String emailBody, File fileName) {
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmail = toEmail;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.fileName = fileName;

        emailProperties = new Properties();
        emailProperties.put("mail.smtp.host", emailHost);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.socketFactory.port", emailPort);
        emailProperties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        mailSession = Session.getDefaultInstance(emailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, fromPassword);
            }
        });
    }




   public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
            Log.i("GMail","toEmail: "+toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));

        emailMessage.setSubject(emailSubject);
//        emailMessage.setContent(emailBody, "text/html; charset=UTF-8");// for a html email
//
//        BodyPart messageBodyPart = new MimeBodyPart();
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(messageBodyPart);
//        DataSource source = new FileDataSource(fileName);
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName(fileName);
//        multipart.addBodyPart(messageBodyPart);
        BodyPart messageBodyPart = new MimeBodyPart();

        messageBodyPart.setText(emailBody);

        Multipart multipart = new MimeMultipart();

        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();

        DataSource source = new FileDataSource(fileName);

        messageBodyPart.setDataHandler(new DataHandler(source));

        messageBodyPart.setFileName(fileName.getName());

        multipart.addBodyPart(messageBodyPart);




        // Send the complete message parts
        emailMessage.setContent(multipart);

        // Send message
        Transport.send(emailMessage);
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail","allrecipients: "+ Arrays.toString(emailMessage.getAllRecipients()));
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}