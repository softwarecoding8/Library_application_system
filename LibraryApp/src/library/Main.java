package library;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
	//constructor
	public Main() {
		
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(Main.class.getResource("view/Login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			//set the application icon
			primaryStage.getIcons().add(new Image("file:icon/book.png")) ;
			primaryStage.show();
			primaryStage.setResizable(false);
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
