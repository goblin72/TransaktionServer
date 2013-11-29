package com.transactions.common;

import com.sigma.email.EMail;
import com.sigma.email.EMailServer;
import com.transactions.input.protocol.Errors;
import org.apache.log4j.Logger;

import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.SocketException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: mtaranyuk
 * Date: 26.04.13
 * Time: 10:40
 */
public class EMailManager
{
    private static final Logger logger = Logger.getLogger(EMailManager.class);

    private static EMailServer inputServer;
    private static EMailServer outputServer;
    private static String emailOutputHost;
    private static String emailOutputProtocol;
    private static String emailUser;
    private static String emailPassword;
    private static final String _CONTENT_HTML = "text/html;  charset=utf-8";
    private static boolean inited=false;
    private static int emailPort;
    private static int numberOftryes=0;
    private static Transport transport;
    private static Session session;

    public static boolean isInited() {
        return inited;
    }

    public static void init(String emailOutputProtocol, String emailOutputHost, int emailPort,String user, String password) throws MessagingException {
        inited=true;
        EMailManager.emailOutputHost=emailOutputHost;
        EMailManager.emailOutputProtocol=emailOutputProtocol;
        EMailManager.emailUser=user;
        EMailManager.emailPassword=password;
        EMailManager.emailPort=emailPort;

        Properties properties = new Properties();
        session = Session.getDefaultInstance(properties);
        transport=session.getTransport(emailOutputProtocol);
        transport.connect(emailOutputHost, emailPort, emailUser, emailPassword);

    }
    
    private static EMailServer getInputMailServer() throws TSException{
        if (inputServer == null) {
            throw new UnsupportedOperationException();
            /*inputServer = EMailServer.getInstance(config.getConfiguration(ModuleCode).getStringParam("EMAIL.INPUT_PROTOCOL"),
                    config.getConfiguration(ModuleCode).getStringParam("EMAIL.INPUT_HOST"),
                    config.getConfiguration(ModuleCode).getStringParam("EMAIL.INPUT_USER"),
                    config.getConfiguration(ModuleCode).getStringParam("EMAIL.INPUT_PASSWORD"));   */
        }
        return inputServer;
    }

    private static EMailServer getOutputMailServer() throws TSException {
        if (emailOutputHost==null || emailOutputHost.isEmpty())
            throw new TSException(Errors.UNKNOWN,"Не задан исходящий почтовый сервер.");
        if (outputServer == null) {
            if (emailUser!=null && !emailUser.isEmpty())
                outputServer = EMailServer.getInstance(emailOutputHost,emailUser,emailPassword);
            else
                outputServer = EMailServer.getInstance(emailOutputHost);
        }
        return outputServer;
    }

    public static void sendHTMLEmail(String subject, String text, String from, String recipients,List<File> files) throws TSException {
        try{
            EMailServer inpuServer = getOutputMailServer();
            EMail email = new EMail(inpuServer.newMessage());

            //email.setContent(message, "text/html; charset=utf-8");
            email.setSubject(subject);
//            email.setText(text);

            email.setFrom(from);
            email.addRecipient(recipients.split("[,;]"));
            if (files!=null)
                for (File f:files)
                {
                    email=email.addAttachment(f);
                }
            email.getMessage().setContent(text,"text/html; charset=utf-8");
            inpuServer.send(email);
        }catch (Exception e)
        {
            e.printStackTrace();
            throw new TSException(Errors.UNKNOWN,"Ошибка отправки письма эл. почтой "+e.getMessage());
        }
    }

    public static void sendHTMLEmail2(String subject, String text, String from, String recipients,List<File> files)throws TSException{
        try{
            Message message = new MimeMessage(session);

            for (String recipient : recipients.split("[,;]")) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            message.addFrom(new InternetAddress[] { new InternetAddress(from) });
            message.setSubject(subject);
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(text);
            mbp1.setContent(text, _CONTENT_HTML);
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            for (File file: files){
                MimeBodyPart mbp = new MimeBodyPart();
                mbp.attachFile(file);
                mbp.setHeader("Content-ID", file.getName());
                mp.addBodyPart(mbp);
            }
            message.setContent(mp);
            message.setSentDate(new Date());
            transport.sendMessage(message, message.getAllRecipients());
            numberOftryes=0;
        }catch (MessagingException e)
        {
            logger.error("numberOftryes="+numberOftryes+" "+e,e);
            if (numberOftryes>1)
            {
                numberOftryes=0;
                inited=false;
                throw new TSException(Errors.UNKNOWN,"Ошибка отправки письма эл. почтой "+e.getMessage());
            }
            numberOftryes++;
            inited=false;
            try {
                init(emailOutputProtocol,emailOutputHost,emailPort,emailUser,emailPassword);
            } catch (MessagingException e1) {
                logger.error(e,e);
                throw new TSException(Errors.UNKNOWN,"Ошибка отправки письма эл. почтой "+e.getMessage());
            }
            sendHTMLEmail2(subject, text, from, recipients, files);
        }
        catch (Exception e)
        {
            logger.error(e,e);
            throw new TSException(Errors.UNKNOWN,"Ошибка отправки письма эл. почтой "+e.getMessage());
        }

    }
}
