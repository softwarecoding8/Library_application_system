package library.view;

import com.jfoenix.controls.JFXProgressBar;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.alert.AlertMaker;
import library.model.MailServerInfo;
import library.DBConnection.DataHelper;
import library.util.EmailUtil;
import library.model.NotificationItem;
import library.util.Preferences;
import library.util.LibraryAppUtil;

/**
 * FXML Controller class
 *
 * @author Josphat
 */
public class EmailSenderController implements Initializable {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Text text;

    private List<NotificationItem> list;
    private StringBuilder emailText = new StringBuilder();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Scanner scanner = new Scanner(getClass().getResourceAsStream(LibraryAppUtil.MAIL_CONTENT_LOC));
            while (scanner.hasNext()) {
                emailText.append(scanner.nextLine()).append("\n");
            }
            
        } catch (Exception ex) {
           
            AlertMaker.showErrorMessage("Error","Something went wrong.");
        }
    }

    public void setNotifRequestData(List<NotificationItem> list) {
        this.list = list;
    }

    public Stage getStage() {
        return (Stage) progressBar.getScene().getWindow();
    }

    public void start() {
        if (emailText == null || emailText.toString().isEmpty()) {
            AlertMaker.showErrorMessage("Failed", "Failed to parse email format");
            getStage().close();
        }
        new EmailSenderHelper().start();
    }

    class EmailSenderHelper extends Thread  {

        private final MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        Preferences pref = new Preferences();
        
        @Override
        public void run() {
            final int size = list.size();
            int count = 0;

            Iterator iterator = list.iterator();
            while (iterator.hasNext()){
                count++;
                NotificationItem item = (NotificationItem) iterator.next();
                String reportDate = LibraryAppUtil.getDateString(new Date());
                String bookName = item.getBookTitle();
                String issueDate = item.getIssueDate();
                Integer daysUsed = item.getDayCount();
                String finePerDay = String.valueOf(pref.getFinePerDay());
                String amount = item.getFineAmount();
                String emailContent = String.format(emailText.toString(), reportDate, bookName, issueDate, daysUsed,finePerDay, amount);
              
                EmailUtil.sendMail(mailServerInfo, item.getMemberEmail(), emailContent, "Library Applicatin System Overdue Notification");
                //flag.set(false);
                updateUI(size, count);
            }
            Platform.runLater(() -> {
                text.setText("Email sent successfully!!.");
                progressBar.setProgress(1);
            });
        }

     

        private void updateUI(int size, int count) {
            Platform.runLater(() -> {
                text.setText(String.format("Notifying %d/%d", count, size));
                progressBar.setProgress((double) count / (double) size);
            });
        }
    }

}

