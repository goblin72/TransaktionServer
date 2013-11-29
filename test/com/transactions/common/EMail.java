package com.transactions.common;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Миша
 * Date: 28.04.13
 * Time: 17:53
 * To change this template use File | Settings | File Templates.
 */
public class EMail
{
    public static void main(String[] args) {

        Properties properties = new Properties();



//        properties.setProperty("mail.smtp.host", "smtp.mail.ru");
//
//        properties.setProperty("mail.user", "goblin2372@mail.ru");
//        properties.setProperty("mail.password", "bodiba90");
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("goblin2372@mail.ru"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("goblin2372@mail.ru"));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.mail.ru", 2525, "goblin2372", "bodiba90");
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("Sent message successfully....");
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }
        
        /*EMailManager.init("smtp.mail.ru","goblin2372@mail.ru","bodiba90");
        try {
            EMailManager.sendHTMLEmail("test","test","taranyuk@mail.ru","goblin2372@mail.ru", new ArrayList<File>());
        } catch (TSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } */
    }
