package library.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.internet.MimeBodyPart;

import javafx.beans.property.SimpleStringProperty;
public class EmailMessage {
    private SimpleStringProperty supplierEmail;
    private SimpleStringProperty subject;
    private Message message;
    private List<MimeBodyPart> attachmentList = new ArrayList<MimeBodyPart>();
    private boolean hasAttachments = false;

    public EmailMessage(String supplierEmail, String subject, Message message){
        this.supplierEmail = new SimpleStringProperty(supplierEmail);
        this.subject = new SimpleStringProperty(subject);
        this.message = message;
    }

    public boolean hasAttachments(){
        return hasAttachments;
    }

    public String getSubject(){
        return this.subject.get();
    }
    public  String getSupplierEmail(){
        return this.supplierEmail.get();
    }
  
    public Message getMessage(){
        return this.message;
    }

    public List<MimeBodyPart> getAttachmentList(){
        return attachmentList;
    }

    public void addAttachment(MimeBodyPart mbp) {
        hasAttachments = true;
        attachmentList.add(mbp);
        try {
            System.out.println("Added attach: " + mbp.getFileName());
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
