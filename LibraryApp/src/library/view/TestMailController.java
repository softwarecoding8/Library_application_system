	package library.view;
	
	import com.jfoenix.controls.JFXProgressBar;
	
	import com.jfoenix.controls.JFXTextField;
	import javafx.scene.control.TextField;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.control.Alert;
	import javafx.scene.control.ProgressBar;
	import java.net.URL;
	import java.util.ResourceBundle;
	import javafx.application.Platform;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import library.alert.AlertMaker;
	import library.model.MailServerInfo;
	import library.util.EmailUtil;
	import library.util.LibraryAppUtil;
	
	public class TestMailController implements Initializable {
		@FXML
	    private TextField recepientAddressInput;
	    @FXML
	    private ProgressBar progressBar;
	
	    private MailServerInfo mailServerInfo;
	
	    @Override
	    public void initialize(URL url, ResourceBundle rb) {
	        // TODO
	    }
	
	    public void setMailServerInfo(MailServerInfo mailServerInfo) {
	        this.mailServerInfo = mailServerInfo;
	    }
	
	    @FXML
	    private void handleStartAction(ActionEvent event) {
	        String toAddress = recepientAddressInput.getText();
	        if (LibraryAppUtil.validateEmailAddress(toAddress)) {
	            EmailUtil.sendTestMail(mailServerInfo, toAddress);
	            progressBar.setVisible(true);
	            recepientAddressInput.clear();
	            
	        } else {
	           
				AlertMaker.showWarningAlert("Warning","Invalid email address!");
	           
	        }
	    }
	
	
	}
	
	
