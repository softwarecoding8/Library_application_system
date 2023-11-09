	package library.view;
	
	import com.jfoenix.controls.JFXCheckBox;
	import com.jfoenix.controls.JFXPasswordField;
	import com.jfoenix.controls.JFXSpinner;
	import com.jfoenix.controls.JFXTextField;
	
	import javafx.scene.control.Alert;
	import javafx.scene.control.CheckBox;
	import javafx.scene.control.PasswordField;
	import javafx.scene.control.ProgressIndicator;
	import javafx.scene.control.TextField;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.control.Spinner;
	import java.io.File;
	import java.net.URL;
	import java.security.InvalidParameterException;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.sql.Timestamp;
	import java.util.ResourceBundle;
	import java.util.concurrent.TimeUnit;
	import java.util.regex.Pattern;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.stage.DirectoryChooser;
	import javafx.stage.Stage;
	import library.alert.AlertMaker;
	import library.util.Preferences;
	import library.model.MailServerInfo;
	import library.model.NotificationItem;
	import library.DBConnection.DBHandler;
	import library.DBConnection.DataHelper;
	import library.DBConnection.DBHandler;
	import library.util.LibraryAppUtil;

	public class SettingsController implements Initializable {

    @FXML
    private TextField nDaysWithoutFine,email,old_password,new_password,confirm_password;
    @FXML
    private TextField finePerDay;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField serverName;
    @FXML
    private TextField smtpPort;
    @FXML
    private TextField emailAddress;
    @FXML
    private PasswordField emailPassword;
    @FXML
    private CheckBox sslCheckbox;
    @FXML
    private ProgressIndicator progressIndicator;
    
    private DBHandler handler =  DBHandler.getInstance();
    private final Connection con;
    private int employee_id;
   
   //The constructor is called before the initialize() method
   public SettingsController() {
  		con = handler.getConnection();
  	}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       initDefaultValues();
    }
    
    public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
    
    @FXML
    private void handleSaveButtonAction(ActionEvent event) throws SQLException {
      int ndays = Integer.parseInt(nDaysWithoutFine.getText());
      float fine = Float.parseFloat(finePerDay.getText());
      
      Preferences preferences = Preferences.getPreferences();
      preferences.setDaysWithNoFine(ndays);
      preferences.setFinePerDay(fine);
      
      Preferences.writePreferenceToFile(preferences);
      
        
    }

    void clear() {
    	nDaysWithoutFine.setText(null);
    	finePerDay.setText(null);
    }
    
    @FXML
	private void resetAction(ActionEvent event) {
    	clear();
    }
    
    private Stage getStage() {
        return ((Stage) nDaysWithoutFine.getScene().getWindow());
    }

    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        nDaysWithoutFine.setText(String.valueOf(preferences.getDaysWithNoFine()));
	    finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
	    
        loadMailServerConfigurations();
    }

    @FXML
    private void handleTestMailAction(ActionEvent event) {
        MailServerInfo mailServerInfo = readMailSererInfo();
        if (mailServerInfo != null) {
            TestMailController controller = (TestMailController) LibraryAppUtil.loadWindow(getClass().getResource("/library/view/Test_email.fxml"), "Test Email", null);
            controller.setMailServerInfo(mailServerInfo);
        }
    }

    @FXML
    private void saveMailServerConfiguration(ActionEvent event) {
        MailServerInfo mailServerInfo = readMailSererInfo();
        if (mailServerInfo != null) {
            if (DataHelper.updateMailServerInfo(mailServerInfo,employee_id)) {
                
               AlertMaker.showSimpleAlert("Success","Mail server information saved successfully!!!");
				
            } else {
               
              AlertMaker.showErrorMessage("Error","Something went wrong.");
    			
            }
        }
    }

    private MailServerInfo readMailSererInfo() {
        try {
            MailServerInfo mailServerInfo
                    = new MailServerInfo(serverName.getText(), Integer.parseInt(smtpPort.getText()), emailAddress.getText(), emailPassword.getText(), sslCheckbox.isSelected());
            if (!mailServerInfo.validate() || !LibraryAppUtil.validateEmailAddress(emailAddress.getText())) {
                throw new InvalidParameterException();
            }
            return mailServerInfo;
        } catch (Exception exp) {
            
        	 AlertMaker.showErrorMessage("Error","Invalid entries found, correct input and try again");
        	
        }
        return null;
    }

    private void loadMailServerConfigurations() {
        MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        if (mailServerInfo != null) {
            //LOGGER.log(Level.INFO, "Mail server info loaded from DB");
            serverName.setText(mailServerInfo.getMailServer());
            smtpPort.setText(String.valueOf(mailServerInfo.getPort()));
            emailAddress.setText(mailServerInfo.getEmailID());
            emailPassword.setText(mailServerInfo.getPassword());
            sslCheckbox.setSelected(mailServerInfo.getSslEnabled());
        }
    }

    @FXML
	private void clearAction(ActionEvent event) {
    	 serverName.setText("");
         smtpPort.setText("");
         emailAddress.setText("");
         emailPassword.setText("");
         sslCheckbox.setSelected(false);
	}
    
    private boolean isEmailRegistered() throws SQLException {
	    PreparedStatement ps = null;
        ResultSet rs = null;
        boolean emailExist = false;

        String query = "select * from users WHERE email = ?";
        try {
        	
        	if(!email.getText().equals("")) {
	            ps = con.prepareStatement(query);
	            ps.setString(1, email.getText());
	            rs = ps.executeQuery();
	            if (rs.next()) {
	                emailExist = true;
	            }
        	}else {
        		
        		AlertMaker.showErrorMessage("Error","Email text field cannot be blank.");
        		email.requestFocus();
        	}
        } catch (SQLException ex) {
            //System.out.println(ex);
            AlertMaker.showErrorMessage("Error","Something went wrong.");
        }finally {
        	ps.close();
        	rs.close();
        }
        return emailExist;
	  }
    
    @FXML
	private void verifyEmail(MouseEvent event) {
    	
    	if(email.getText()!="") {
	    	if(isValid(email.getText())){
	    		
	    		try {
					if(!(isEmailRegistered())) {
					
					AlertMaker.showErrorMessage("Error","This email do not exist in our records");
					
					email.requestFocus();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
	    	}else {
	    		AlertMaker.showErrorMessage("Error","Invalid email");
	    		email.requestFocus();
	    	}
    	}
    }
    
    public static boolean isValid(String iemail) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (iemail == null) 
            return false; 
        return pat.matcher(iemail).matches(); 
    }	
    
    private boolean isOldPasswordMatch() throws SQLException {
	    PreparedStatement ps = null;
        ResultSet rs = null;
        boolean oldPasswordMatch = false;

        String query = "select * from users WHERE email = ? AND password = ?";
        try {
        	
        	if(!old_password.getText().equals("")) {
	            ps = con.prepareStatement(query);
	            ps.setString(1, email.getText());
	            ps.setString(2, old_password.getText());
	            rs = ps.executeQuery();
	            if (rs.next()) {
	            	oldPasswordMatch = true;
	            }
        	}else {
        		 	
        		AlertMaker.showErrorMessage("Error","Old password field cannot be blank.");
        		old_password.requestFocus();
        	}
        } catch (SQLException ex) {
           // System.out.println(ex);
           AlertMaker.showErrorMessage("Error","Something went wrong.");
        }
        return oldPasswordMatch;
	  }
    
    @FXML
	private void verifyOldPassword(MouseEvent event) {
    	
    	try {
			if(!(isOldPasswordMatch())) {
  
					AlertMaker.showErrorMessage("Error","Your email and old password do not match our records");
					old_password.requestFocus();
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    @FXML
    private void handleChangePasswordAction() throws SQLException {
        PreparedStatement ps = null;
    	Statement stmt = null;
        if (this.isValidated()) {
            try {
                stmt = con.createStatement();
                String query = "update  users SET password=? where email=?";
                ps = con.prepareStatement(query);
                ps.setString(1, new_password.getText());
                ps.setString(2, email.getText());
          
                if (ps.executeUpdate() > 0) {
                   this.clearForm();
                   
                   AlertMaker.showSimpleAlert("Success","Password changed successfully!!.");
                   
                } else {
                    
                	AlertMaker.showErrorMessage("Error","Something went wrong.");
                    
                }

            } catch (SQLException ex) {
            	AlertMaker.showErrorMessage("Error","Something went wrong.");
            }finally {
            	stmt.close();
            	ps.close();
            }
        }
    }
    
    private boolean clearForm() {
        email.clear();
        old_password.clear();
        new_password.clear();
        confirm_password.clear();
        return true;
    }
    
    private boolean isValidated() { 
    	if(!(isValid(email.getText()))){
    		AlertMaker.showErrorMessage("Error","Invalid email");
    		email.requestFocus();
    	} else
			try {
				if(!(isEmailRegistered())) {
					
					AlertMaker.showErrorMessage("Error","This email do not exist in our records");
					
					email.requestFocus();
				}
				else if(!(isOldPasswordMatch())) {
					  
					AlertMaker.showErrorMessage("Error","Your email and old password do not match our records");
					old_password.requestFocus();
				}
				else if (new_password.getText().equals("")) {
				   
				 	AlertMaker.showErrorMessage("Error","Password field cannot be blank.");
				    new_password.requestFocus();
				    
				} else if (new_password.getText().length() < 5 || new_password.getText().length() > 25) {
					
					AlertMaker.showErrorMessage("Error","Password  field cannot be less than 5 and greator than 25 characters.");
					new_password.requestFocus();
					
				} else if (confirm_password.getText().equals("")) {
					
					AlertMaker.showErrorMessage("Error","Confirm password  field cannot be blank.");
				    confirm_password.requestFocus();
				    
				} else if (confirm_password.getText().length() < 5 || confirm_password.getText().length() > 25) {
				   
					AlertMaker.showErrorMessage("Error","Confirm password  field cannot be less than 5 and greator than 25 characters");
				    confirm_password.requestFocus();
				    
				} else if (!new_password.getText().equals(confirm_password.getText())) {
				    
					AlertMaker.showErrorMessage("Error","The password and confirm password  fields do not match.");
					new_password.requestFocus();
				    confirm_password.clear();
				    
				} else {
				    return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return false;
   }
    
    @FXML
    private void handleClearAction() {
    	email.clear();
        old_password.clear();
        new_password.clear();
        confirm_password.clear();
    }
    

}

