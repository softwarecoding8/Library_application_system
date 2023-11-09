	package library.view;
	
	import java.awt.MenuItem;
	import java.awt.event.ActionEvent;
    import java.io.File;
    import java.io.IOException;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ResourceBundle;
	import java.util.Calendar;
	
	import library.util.LibraryAppUtil;
	import library.Main;
	import library.DBConnection.DBHandler;
	import library.alert.AlertMaker;
	import library.view.LoginController;
	import net.sf.jasperreports.engine.JasperReport;
	import net.sf.jasperreports.engine.design.JasperDesign;
	import net.sf.jasperreports.engine.xml.JRXmlLoader;
	import net.sf.jasperreports.view.JasperViewer;
	import net.sf.jasperreports.engine.JasperCompileManager;
	import net.sf.jasperreports.engine.JasperFillManager;
	import net.sf.jasperreports.engine.JasperPrint;
	import net.sf.jasperreports.engine.JREmptyDataSource;
import javafx.application.Platform;
import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.image.Image;
	import javafx.scene.image.ImageView;
	import javafx.scene.layout.AnchorPane;
	import javafx.scene.layout.BorderPane;
	import javafx.scene.layout.StackPane;
	import javafx.scene.text.Text;
	import javafx.stage.Stage;
	
	public class RootLayoutController implements Initializable {
		private DBHandler handler =  DBHandler.getInstance();
	    private final Connection con;
		@FXML
		private BorderPane rootLayout;
		@FXML
		private Text footer;
		@FXML
		private Button dashboard;
		
		private String username;
		private String password;
		private int employee_id;
		
		
	public RootLayoutController() {
		con = handler.getConnection();
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		footer();
		loadDashboard();
	}
		
	public void footer() {
		Calendar now = Calendar.getInstance();
		int yr = now.get(Calendar.YEAR);
		footer.setText("\u00A9" + Integer.toString(yr) +" softwarecoding8@gmail.com");
	
	}

	public void setData(String usernameIn,String passwordIn)throws  SQLException {
		PreparedStatement ps = null;
        ResultSet rs = null;
		this.username = usernameIn;
		this.password = passwordIn;
		String query = "select * from users WHERE user_name = ? and password = ?";
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            
            if (rs.next()) {
            	employee_id = rs.getInt("user_id");
            }
        }catch(SQLException ex) {
        	
        }finally {
        	ps.close();
        	rs.close();
        }

	}
	@FXML	
	public void handleShowMember() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Member.fxml"));
			MemberController memberController= new MemberController();
     	    loader.setController(memberController);
     	    memberController.setData(employee_id);
     	    AnchorPane memberOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(memberOverview);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void loadDashboard() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Dashboard.fxml"));
			BorderPane dashboard = (BorderPane) loader.load();
			rootLayout.setCenter(dashboard);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}	
	
	@FXML	
	public void handleShowAddNewMember() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/AddNewMember.fxml"));
			AnchorPane newMember = (AnchorPane) loader.load();
			rootLayout.setCenter(newMember);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleMemberOverview() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Member.fxml"));
			MemberController memberController= new MemberController();
     	    loader.setController(memberController);
     	    memberController.setData(employee_id);
     	    
			AnchorPane memberOverview = (AnchorPane) loader.load();
			//set the member overview into the center of root layout
			rootLayout.setCenter(memberOverview);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleSupplier() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Supplier.fxml"));
			SupplierController supplierController= new SupplierController();
     	    loader.setController(supplierController);
     	    supplierController.setData(employee_id);
     	    
			AnchorPane supplier = (AnchorPane) loader.load();
			rootLayout.setCenter(supplier);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
			
		}
	}
	
	@FXML	
	public void handleLostBook() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/LostBook.fxml"));
			LostBookController lostBkController= new LostBookController();
     	    loader.setController(lostBkController);
     	    lostBkController.setData(employee_id);
     	    
			AnchorPane lostbook = (AnchorPane) loader.load();
			rootLayout.setCenter(lostbook);
			
		}catch (IOException e) {
			//e.printStackTrace()
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML	
	public void handleSendOrderList() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/SendOrderList.fxml"));
			AnchorPane order = (AnchorPane) loader.load();
			rootLayout.setCenter(order);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleOrderList() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Order.fxml"));
			AnchorPane order22 = (AnchorPane) loader.load();
			rootLayout.setCenter(order22);
			
		}catch (IOException e) {
			e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleBookOverview() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Book.fxml"));
			BookOverviewController bkOverviewController= new BookOverviewController();
     	    loader.setController(bkOverviewController);
     	    bkOverviewController.setData(employee_id);
     	    
			
			AnchorPane bookOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(bookOverview);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleIssue() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/IssueBook.fxml"));
			IssueBookController issueBkController= new IssueBookController();
     	    loader.setController(issueBkController);
     	    issueBkController.setData(employee_id);
     	    
     	    AnchorPane bookIssue = (AnchorPane) loader.load();
			rootLayout.setCenter(bookIssue);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleIssuedBookStatistics() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/IssuedBooksStatistics.fxml"));
			AnchorPane bookIssued = (AnchorPane) loader.load();
			rootLayout.setCenter(bookIssued);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML	
	public void handleReturn() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/ReturnBook.fxml"));
			ReturnBookController returnBkController= new ReturnBookController();
     	    loader.setController(returnBkController);
     	    returnBkController.setData(employee_id);
     	    
     	    AnchorPane bookReturn = (AnchorPane) loader.load();
			rootLayout.setCenter(bookReturn);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML
	private void handleNotification() {
	   try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/overdue_notification.fxml"));
			StackPane overdue = (StackPane) loader.load();
			rootLayout.setCenter(overdue);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML
	private void handleSettings() {
	   try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Settings.fxml"));
			SettingsController settingController= new SettingsController();
     	    loader.setController(settingController);
     	    settingController.setData(employee_id);
     	    
			StackPane settings = (StackPane) loader.load();
			rootLayout.setCenter(settings);
			
		}catch (IOException e) {
			e.printStackTrace();
			//AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML
	public void handlePurchase() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/Purchase.fxml"));
			AnchorPane purchase = (AnchorPane) loader.load();
			rootLayout.setCenter(purchase);
			
		}catch (IOException e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML
	public void handleBooks() {
		try {
			//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
			JasperDesign design = JRXmlLoader.load("src/library/report/Book.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, null,con);
			JasperViewer.viewReport(jprint,false);
			//connection.close();
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML
	public void handleOverdueFineCollected() {
		try {
			
			JasperDesign design = JRXmlLoader.load("src/library/report/OverdueFine.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, null,con);
			JasperViewer.viewReport(jprint,false);
			//connection.close();
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML
	public void handleLostBookFineCollected() {
		try {
			JasperDesign design = JRXmlLoader.load("src/library/report/LostBookFineCollected.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, null,con);
			JasperViewer.viewReport(jprint,false);
			//connection.close();
			
			/*JasperDesign design = JRXmlLoader.load("src/application/reports/top5Customers.jrxml");
	        JasperReport report = JasperCompileManager.compileReport(design);
	        JasperPrint jprint = JasperFillManager.fillReport(report, null, this.connection);
	        JasperViewer.viewReport(jprint, false);
	        return true;*/
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		
	@FXML
	public void handleIssuedBooks() {
		try {
			//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
			JasperDesign design = JRXmlLoader.load("src/library/report/LendBookReport.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, null,con);
			JasperViewer.viewReport(jprint,false);
			//connection.close();
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML
	public void handleRegisteredMembers() {
		try {
			//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
			JasperDesign design = JRXmlLoader.load("src/library/report/Member.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, null,con);
			JasperViewer.viewReport(jprint,false);
			//connection.close();
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	@FXML
	private void logoutAction() throws IOException {
	    Stage stage = (Stage) dashboard.getScene().getWindow();
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
	private void closeApp(){
		Platform.exit();
	}
}
