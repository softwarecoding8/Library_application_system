package library.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;

public class ForgotPasswordController implements Initializable {
 	@FXML
    private TextField email;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField confirm_Password;
    
    @FXML
    private Button saveButton;

    Window window;
    
    private DBHandler handler =  DBHandler.getInstance();
    private final Connection con;
	
   //The constructor is called before the initialize() method
	public ForgotPasswordController() {
		con = handler.getConnection();
	}
    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	
	}

	
	  private boolean isEmailRegistered() {
		    PreparedStatement ps;
	        ResultSet rs;
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
	            System.out.println(ex);
	        }
	        return emailExist;
		  }
			 
		  
    @FXML
	private void verifyEmail(MouseEvent event) {
    	if(email.getText()!="") {
    	if(isValid(email.getText())){
    		if(!(isEmailRegistered())) {
	    	
			AlertMaker.showErrorMessage("Error","This email does not exist in our records");
			email.requestFocus();
    		}
    	}else {
    		AlertMaker.showErrorMessage("Error","Invalid Email");
    		email.requestFocus();
    	}
    	}
    }
    
    private boolean isValidated() { 
    	if(!(isValid(email.getText()))){
    		AlertMaker.showErrorMessage("Error","Invalid Email");
    		email.requestFocus();
    	}
    	else if(!(isEmailRegistered())) {
	    	
			AlertMaker.showErrorMessage("Error","This email does not exist in our records");
			email.requestFocus();
    	}
    	else  if (new_password.getText().equals("")) {
    		 
    		 AlertMaker.showErrorMessage("Error","Password field cannot be blank.");
    		 new_password.requestFocus();
    		 
		} else if (new_password.getText().length() < 5 || new_password.getText().length() > 25) {
		   
			 AlertMaker.showErrorMessage("Error","Password field cannot be less than 5 and greator than 25 characters.");
    		 new_password.requestFocus();
			
		} else if (confirm_Password.getText().equals("")) {
		    
			AlertMaker.showErrorMessage("Error","Confirm password field cannot be blank.");
    		confirm_Password.requestFocus();
			
		} else if (confirm_Password.getText().length() < 5 || confirm_Password.getText().length() > 25) {
		   
			AlertMaker.showErrorMessage("Error","Confirm password field cannot be less than 5 and greator than 25 characters.");
    		confirm_Password.requestFocus();
			
		} else if (!new_password.getText().equals(confirm_Password.getText())) {
		   
			AlertMaker.showErrorMessage("Error","The password and confirm password fields do not match.");
    		confirm_Password.requestFocus();
			
		} else {
		    return true;
		}
		return false;
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
		    
    @FXML
    private void updateLoginCredentials() {
       if (this.isValidated()) {
            Statement stmt;
            Connection connection;
            connection = handler.getConnection();
            try {
                PreparedStatement ps;
                stmt = connection.createStatement();
                String query = "update  users SET password=? where email=?";
                ps = connection.prepareStatement(query);
                ps.setString(1, new_password.getText());
                ps.setString(2, email.getText());
          
                if (ps.executeUpdate() > 0) {
                   this.clearForm();
                   AlertMaker.showSimpleAlert("Success","You have reset password successfully." );
                } else {
                	AlertMaker.showErrorMessage("Error","Something went wrong.");
                }

            } catch (SQLException ex) {
            	AlertMaker.showErrorMessage("Error","Something went wrong.");
            }
        }
    }
    
    private boolean clearForm() {
        email.clear();
        new_password.clear();
        confirm_Password.clear();
        return true;
    }
		    
    @FXML
    private void showLoginStage() throws IOException {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/library/view/Login.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.getIcons().add(new Image("file:icon/book.png"));
        stage.show();
        stage.setResizable(false);
    }
    
    @FXML
    private void clearAction() {
    	email.clear();
        new_password.clear();
        confirm_Password.clear();
    }
}

