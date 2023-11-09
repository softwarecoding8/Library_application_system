	package library.view;
	
	import java.io.IOException;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.Date;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;
	import java.util.ResourceBundle;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.scene.control.Alert;
	import javafx.scene.control.TextField;
	import javafx.scene.input.KeyCode;
	import javafx.scene.input.KeyEvent;
	import javafx.scene.input.MouseEvent;
	import library.DBConnection.DBHandler;
	import library.alert.AlertMaker;
	import net.sf.jasperreports.engine.JasperCompileManager;
	import net.sf.jasperreports.engine.JasperFillManager;
	import net.sf.jasperreports.engine.JasperPrint;
	import net.sf.jasperreports.engine.JasperReport;
	import net.sf.jasperreports.engine.design.JasperDesign;
	import net.sf.jasperreports.engine.xml.JRXmlLoader;
	import net.sf.jasperreports.view.JasperViewer;
	
	public class PaymentController implements Initializable{
		
		@FXML
		private TextField cash;
		@FXML
		private TextField balance;
		@FXML 
		private TextField dueAmount;
		private float fined;
		private int member_id;
		private String name;
		private String title;
		private String elapdays;
		private Date sqlDate;
		private int employee_id;
		
		private DBHandler handler =  DBHandler.getInstance();
	    private final Connection con;
	    
	public PaymentController() {
		con = handler.getConnection();
	}
	    
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	     	dueAmount.setText(String.valueOf(fined));
	}
		  
	public void setData(float finedIn,int memberIdIn,String nameIn,String titleIn,Date sqlDateIn,String elapdaysIn,int employeeId) {
	
	    this.fined = finedIn;
	    this.member_id = memberIdIn;
	    this.name = nameIn;
	    this.title = titleIn;
	    this.elapdays = elapdaysIn;
	    this.sqlDate = sqlDateIn;
	    this.employee_id = employeeId;
	        
	}
		
	@FXML
	private void calculateBal(MouseEvent event) throws IOException{
			String pay = cash.getText();
			float f = Float.parseFloat(pay);
			float bal = f - fined;
			balance.setText(String.valueOf(bal));
	}
		
	@FXML
	private void clearAction(ActionEvent event) {
		balance.setText("");
		cash.setText("");
	}
		
	@FXML
	private void handleReceipt(ActionEvent event) throws SQLException{
		
		String fine = dueAmount.getText();
		String pay = cash.getText();
		String bal = balance.getText();
		
		String insert = "INSERT INTO returnbook(member_id,fname,title,issuedate,elapdays,fine,paid,balance,employee_id) VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pst = null;
		PreparedStatement ps = null;
			
		try {
			pst = con.prepareStatement(insert);
			pst.setInt(1, member_id);
			pst.setString(2, name);
			pst.setString(3, title);
			pst.setDate(4, sqlDate);
			pst.setString(5, elapdays);
			pst.setString(6, fine);
			pst.setString(7, pay);
			pst.setString(8, bal);
			pst.setInt(9, employee_id);
			pst.executeUpdate();
			
			
			//delete from lend book
			try{
				ps = con.prepareStatement("delete from lendbook where member_id =? AND book_title =?");
				ps.setInt(1,member_id);
				ps.setString(2,title);
				ps.executeUpdate();
			
				generateReceipt();
				dueAmount.setText("");
				balance.setText("");
				cash.setText("");
			}catch (SQLException s1) {
				s1.printStackTrace();
				//AlertMaker.showErrorMessage("Error","Something went wrong.");
			}finally {
				ps.close();
			}
		}catch (SQLException s) {
			s.printStackTrace();
			//AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}
		
	 private void generateReceipt() {
			try {
				//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
				java.util.Map params = new java.util.HashMap();
				params.put("para_id", member_id);
				params.put("para_title", title);
				JasperDesign design = JRXmlLoader.load("src/library/receipt/Receipt.jrxml");
				JasperReport report = JasperCompileManager.compileReport(design);
				JasperPrint jprint = JasperFillManager.fillReport(report, params,con);
				JasperViewer.viewReport(jprint,false);
				//con.close();
				
			}catch (Exception e) {
				e.printStackTrace();
				//AlertMaker.showErrorMessage("Error","Something went wrong3.");
			} 
	 }
	}