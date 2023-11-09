	package library.view;
	
	import library.alert.AlertMaker;
	
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
	import javafx.scene.image.Image;
	import javafx.stage.Stage;
	import javafx.stage.Window;
	import library.DBConnection.DBHandler;
	
	public class RegisterController implements Initializable {
		@FXML
	    private TextField firstName;
	
	    @FXML
	    private TextField lastName;
	
	    @FXML
	    private TextField email;
	
	    @FXML
	    private TextField username;
	
	    @FXML
	    private PasswordField password;
	
	    @FXML
	    private PasswordField confirmPassword;
	
	    @FXML
	    private Button registerButton;
	
	    private DBHandler handler =  DBHandler.getInstance();
	    private final Connection con;
	    
	
	public RegisterController() {
	    con = handler.getConnection();
	}
	    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
	}
	
	@FXML
	private void registerAction() throws SQLException {
		Statement stmt = null;
	    PreparedStatement ps = null;
	    if (this.isValidated()) {
	    	try {
	            stmt = con.createStatement();
	            String query = "insert into users (first_name,last_name,email,user_name,password)values (?,?,?,?,?)";
	            ps = con.prepareStatement(query);
	            ps.setString(1, firstName.getText());
	            ps.setString(2, lastName.getText());
	            ps.setString(3, email.getText());
	            ps.setString(4, username.getText());
	            ps.setString(5, password.getText());
	            if (ps.executeUpdate() > 0) {
	                this.clearForm();
	               
	                AlertMaker.showSimpleAlert("Success","You have registered successfully." );
	                
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
	
	private boolean isAlreadyRegistered() throws SQLException{
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    boolean emailExist = false;
	
	    String query = "select * from users WHERE email = ?";
	    try {
	        ps = con.prepareStatement(query);
	        ps.setString(1, email.getText());
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            emailExist = true;
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
	
	private boolean isValidated() {
	
	    if (firstName.getText().equals("")) {
	       
	    	AlertMaker.showErrorMessage("Error","First name text field cannot be blank.");
	    	firstName.requestFocus();
	        
	    } else if (firstName.getText().length() < 2 || firstName.getText().length() > 25) {
	       
	    	AlertMaker.showErrorMessage("Error","First name text field cannot be less than 2 and greator than 25 characters.");
	    	firstName.requestFocus();
	        
	    } else if (lastName.getText().equals("")) {
	       
	    	AlertMaker.showErrorMessage("Error","Last name text field cannot be blank.");
	    	lastName.requestFocus();
	        
	    } else if (lastName.getText().length() < 2 || lastName.getText().length() > 25) {
	      
	    	AlertMaker.showErrorMessage("Error","Last name text field cannot be less than 2 and greator than 25 characters.");
	    	lastName.requestFocus();
	       
	    } else if (email.getText().equals("")) {
	    	
	    	AlertMaker.showErrorMessage("Error","Email text field cannot be blank.");
	    	lastName.requestFocus();
	        
	    } else if(!(isValid(email.getText()))){
	    	
	    	AlertMaker.showErrorMessage("Error","Invalid email!!.");
	    	email.requestFocus();
	    	
	    } else if (email.getText().length() < 5 || email.getText().length() > 45) {
	    	
	    	AlertMaker.showErrorMessage("Error","Email text field cannot be less than 5 and greator than 45 characters.");
	    	email.requestFocus();
	      
	    } else if (username.getText().equals("")) {
	        
	    	AlertMaker.showErrorMessage("Error","Username text field cannot be blank.");
	    	username.requestFocus();
	        
	    } else if (username.getText().length() < 5 || username.getText().length() > 25) {
	       
	    	AlertMaker.showErrorMessage("Error","Username text field cannot be less than 5 and greator than 25 characters.");
	    	username.requestFocus();
	       
	    } else if (password.getText().equals("")) {
	       
	    	AlertMaker.showErrorMessage("Error","Password  field cannot be blank.");
	    	username.requestFocus();
	    
	    } else if (password.getText().length() < 5 || password.getText().length() > 25) {
	        
	    	AlertMaker.showErrorMessage("Error","Password field cannot be less than 5 and greator than 25 characters.");
	    	username.requestFocus();
	       
	    } else if (confirmPassword.getText().equals("")) {
	      
	    	AlertMaker.showErrorMessage("Error","Confirm password  field cannot be blank.");
	    	confirmPassword.requestFocus();
	      
	    } else if (confirmPassword.getText().length() < 5 || password.getText().length() > 25) {
	        
	    	AlertMaker.showErrorMessage("Error","Confirm password  field cannot be less than 5 and greator than 25 characters.");
	    	confirmPassword.requestFocus();
	        
	    } else if (!password.getText().equals(confirmPassword.getText())) {
	       
	    	AlertMaker.showErrorMessage("Error","The password and confirm password fields do not match.");
	  
	        } else
				try {
					if (isAlreadyRegistered()) {
						
						AlertMaker.showErrorMessage("Error","The email is already taken by someone else.");
					
				} else {
				    return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    return false;
	}
	
	private boolean clearForm() {
	    firstName.clear();
	    lastName.clear();
	    email.clear();
	    username.clear();
	    password.clear();
	    confirmPassword.clear();
	    return true;
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
	private void showLoginStage() throws IOException {
	    Stage stage = (Stage) registerButton.getScene().getWindow();
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
		firstName.clear();
	    lastName.clear();
	    email.clear();
	    username.clear();
	    password.clear();
	    confirmPassword.clear();
	}
	
	}
