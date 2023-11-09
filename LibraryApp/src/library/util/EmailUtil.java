package library.util;


import com.sun.mail.util.MailSSLSocketFactory;

import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;


import library.alert.AlertMaker;
import library.model.MailServerInfo;



public class EmailUtil {
	
	private List<File> attachments;
	
public static void sendTestMail(MailServerInfo mailServerInfo, String recepient) {

        Runnable emailSendTask = () -> {
        	Properties props = new Properties();
            try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                props.put("mail.imap.ssl.trust", "*");
                props.put("mail.imap.ssl.socketFactory", sf);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", mailServerInfo.getSslEnabled() ? "true" : "false");
                props.put("mail.smtp.host", mailServerInfo.getMailServer());
                props.put("mail.smtp.port", mailServerInfo.getPort());

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailServerInfo.getEmailID(), mailServerInfo.getPassword());
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailServerInfo.getEmailID()));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recepient));
                message.setSubject("Test Mail");
                message.setText("Hi,"
                        + "\n\n This is a test mail from Library Application System!");

                Transport.send(message);
                JOptionPane.showMessageDialog(null,"Email sent successfully!!.");
            } catch (Throwable exp) {
                //JOptionPane.showMessageDialog(null,"Error occurred during sending email" );
                JOptionPane.showMessageDialog(null,exp);
            }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }
    //List<File>attachments
    public static void sendAttachMail(MailServerInfo mailServerInfo, String recepient, String content, String title,String attachment_path1, List a1,List a2) {
    	 
    		Properties props = new Properties();
      try {
             MailSSLSocketFactory sf = new MailSSLSocketFactory();
             sf.setTrustAllHosts(true);
             props.put("mail.imap.ssl.trust", "*");
             props.put("mail.imap.ssl.socketFactory", sf);
             props.put("mail.smtp.auth", "true");
             props.put("mail.smtp.starttls.enable", mailServerInfo.getSslEnabled() ? "true" : "false");
             props.put("mail.smtp.host", mailServerInfo.getMailServer());
             props.put("mail.smtp.port", mailServerInfo.getPort());

             Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                 @Override
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(mailServerInfo.getEmailID(), mailServerInfo.getPassword());
                 }
             });
      
             try {
            	 if(attachment_path1.isEmpty()) {
            		 Message message = new MimeMessage(session);
            		 Multipart multipart = new MimeMultipart();
                     message.setSubject(title);
                     
                     MimeBodyPart messageBodyPart = new MimeBodyPart();
                     messageBodyPart.setContent(content,"text/html");
                     multipart.addBodyPart(messageBodyPart);
                     
                     message.setFrom(new InternetAddress(mailServerInfo.getEmailID()));
                     message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
                     message.setContent(multipart);
                     Transport.send(message);
                     }else {
            	 		Message message = new MimeMessage(session);
               		    Multipart multipart = new MimeMultipart();
                        message.setSubject(title);
                        
                        MimeBodyPart messageBodyPart = new MimeBodyPart();
                        messageBodyPart.setContent(content,"text/html");
                        multipart.addBodyPart(messageBodyPart);
                        
                                              
                        for(int i=0;i<a1.size();i++) {
 
                        	message.setFrom(new InternetAddress(mailServerInfo.getEmailID()));
                        	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
                        	
                        	DataSource source = new FileDataSource((String)a1.get(i));
                        	MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                        	messageBodyPart2.setDataHandler(new DataHandler(source));
                        	messageBodyPart2.setFileName((String)a2.get(i));
                        	
                            multipart.addBodyPart(messageBodyPart2);
                            message.setContent(multipart);
                        }
                        Transport.send(message);
                       }
             }catch(Exception e) {
            	 JOptionPane.showMessageDialog(null,"Error occurred during sending email");
             }
        
      }  catch (Throwable exp) {
        
    	  JOptionPane.showMessageDialog(null,"Error occurred during sending email");
      }   

    }
    
  
   public static void sendMail(MailServerInfo mailServerInfo, String recepient, String content, String title) {

        Runnable emailSendTask = () -> {
            
            Properties props = new Properties();
            try {
                MailSSLSocketFactory sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                props.put("mail.imap.ssl.trust", "*");
                props.put("mail.imap.ssl.socketFactory", sf);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", mailServerInfo.getSslEnabled() ? "true" : "false");
                props.put("mail.smtp.host", mailServerInfo.getMailServer());
                props.put("mail.smtp.port", mailServerInfo.getPort());

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailServerInfo.getEmailID(), mailServerInfo.getPassword());
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailServerInfo.getEmailID()));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
                message.setSubject(title);
                message.setContent(content, "text/html");
              
                Transport.send(message);
            } catch (Throwable exp) {
                JOptionPane.showMessageDialog(null,"Error occurred during sending email");
            }
        };
        Thread mailSender = new Thread(emailSendTask, "EMAIL-SENDER");
        mailSender.start();
    }
}
