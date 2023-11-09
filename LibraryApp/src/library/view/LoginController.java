package library.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import library.DBConnection.*;
import library.alert.AlertMaker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;

public class LoginController implements Initializable{
	@FXML
	private TextField username;
	
	@FXML
	private PasswordField password;
	
	@FXML
	private Button loginButton;
	
	private RootLayoutController rootController;
	private DBHandler handler =  DBHandler.getInstance();
	private final Connection con;
	    
	public LoginController() {
	    con = handler.getConnection();
	}
	    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//handler = new DBHandler();
    }
	    
	@FXML
	private void handleLoginButtonAction(ActionEvent event) throws Exception{
		PreparedStatement ps = null;
        ResultSet rs = null;
        if (this.isValidated()) {
            String query = "select * from users WHERE user_name = ? and password = ?";
            try {
                ps = con.prepareStatement(query);
                ps.setString(1, username.getText());
                ps.setString(2, password.getText());
                rs = ps.executeQuery();

                if (rs.next()) {
                	Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.close();

                    
                    //BorderPane rootLayout = FXMLLoader.load(getClass().getResource("/library/view/RootLayout.fxml"));
            		//Stage stage = new Stage(StageStyle.DECORATED);
                    FXMLLoader rootLayout = new FXMLLoader((getClass().getResource("/library/view/RootLayout.fxml")));
                    RootLayoutController rootController= new RootLayoutController();
            	    rootLayout.setController(rootController);
            	    rootController.setData(username.getText(),password.getText());
            	    Parent root = rootLayout.load();
            	    stage.setScene(new Scene(root));
            		stage.setTitle("Library Application System");
            		//set the application icon
            		stage.getIcons().add(new Image("file:icon/book.png")) ;
            		stage.show();
            		stage.setResizable(false);

                } else {
                	
                	AlertMaker.showErrorMessage("Error","The username or password is incorrect.");
                    username.requestFocus();
                    password.clear();
                }
            } catch (SQLException ex) {
                //System.out.println(ex);
            }finally {
            	ps.close();
            	rs.close();
            }
        }
	}
	
    private boolean isValidated() {

        if (username.getText().equals("")) {
        	
        	AlertMaker.showErrorMessage("Error","The username text field cannot be blank.");
           username.requestFocus();
           
        } else if (username.getText().length() < 5 || username.getText().length() > 25) {
        	
        	AlertMaker.showErrorMessage("Error","Username text field cannot be less than 5 and greator than 25 characters.");
            username.requestFocus();
            
        } else if (password.getText().equals("")) {
            
        	AlertMaker.showErrorMessage("Error","Password text field cannot be blank.");
        	password.requestFocus();
        	
        } else if (password.getText().length() < 5 || password.getText().length() > 25) {
        	
        	AlertMaker.showErrorMessage("Error","Password text field cannot be less than 5 and greator than 25 characters.");
            password.requestFocus();
            
        } else {
            return true;
        }
        return false;
    }
        
    @FXML
    private void showRegisterStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();

        Parent root = FXMLLoader.load(getClass().getResource("/library/view/Register.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.getIcons().add(new Image("file:icon/book.png"));
        stage.show();
        stage.setResizable(false);
    }    
		
   @FXML
   private void clearAction() {
	   username.clear();
	   password.clear();
   }
		
   @FXML
   private void showForgotPasswordStage() throws IOException{
       Stage stage = (Stage) loginButton.getScene().getWindow();
       stage.close();

       Parent root = FXMLLoader.load(getClass().getResource("/library/view/ForgotPassword.fxml"));

       Scene scene = new Scene(root);

       stage.setScene(scene);
       stage.setTitle("Forgot Password");
       stage.getIcons().add(new Image("file:icon/book.png"));
       stage.show();
       stage.setResizable(false);
   }	

}
