package library.util;

import com.jfoenix.controls.JFXButton;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;
import library.util.Preferences;
import library.view.RootLayoutController;

public class LibraryAppUtil {

    public static final String ICON_IMAGE_LOC = "/resources/icon.png";
    public static final String MAIL_CONTENT_LOC = "/resources/mail_content.html";
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    
    private DBHandler handler =  DBHandler.getInstance();
    private Connection con = handler.getConnection();
    
    public static void setStageIcon(Stage stage) {
    	stage.getIcons().add(new Image("file:icon/book.png"));
    }

    public static Object loadWindow(URL loc, String title, Stage parentStage) {
        Object controller = null;
        try {
            FXMLLoader loader = new FXMLLoader(loc);
            Parent parent = loader.load();
            controller = loader.getController();
            Stage stage = null;
            if (parentStage != null) {
                stage = parentStage;
            } else {
                stage = new Stage(StageStyle.DECORATED);
            }
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
            stage.setResizable(false);
            setStageIcon(stage);
        } catch (IOException ex) {
           
        }
        return controller;
    }
    
  public static  Float getFineAmount(int totalDays){
	  Preferences pref = Preferences.getPreferences();
        Integer fineDays = totalDays - (pref.getDaysWithNoFine());
        Float fine = 0f;
        if (fineDays > 0) {
            fine = fineDays * (pref.getFinePerDay());
        }
        return fine;
  }
    
 

   public static String formatDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

   public static String formatDateTimeString(Long time) {
        return DATE_TIME_FORMAT.format(new Date(time));
    }
    
    public static String getDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

 
    
    public static boolean validateEmailAddress(String emailID) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(emailID).matches();
    }


}
