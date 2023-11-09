package library.view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import library.DBConnection.DBHandler;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class LostBkPaymentController implements Initializable{
	@FXML
	private TextField cash;
	@FXML
	private TextField balance;
	@FXML 
	private TextField total;
	private float fine;
	private String member_id;
	private String isbn;
	private String title;
	private float price;
	private int elapdays;
	private int employee_id;
	private Connection con;
	private DBHandler handler =  DBHandler.getInstance();
	
//The constructor is called before the initialize() method
public LostBkPaymentController() {
	con = handler.getConnection();
}

 @Override
 public void initialize(URL url, ResourceBundle rb) {
    total.setText(String.valueOf(fine));
 }
	  
 public void setData(String memberIdIn,String isbnIn,String titleIn,float priceIn,int elapdaysIn,float fineIn,int employeeId) {
  	this.member_id = memberIdIn;
    this.isbn = isbnIn;
    this.title = titleIn;
    this.price = priceIn;
    this.elapdays = elapdaysIn;
    this.fine = fineIn;
    this.employee_id = employeeId;
 }
	 
 @FXML
 private void calculateBal(MouseEvent event) throws IOException{
	String pay = cash.getText();
	float f = Float.parseFloat(pay);
	float bal = f - fine;
	balance.setText(String.valueOf(bal));
 }
 
 @FXML
 private void clearAction(ActionEvent event) {
	balance.setText("");
	cash.setText("");
 }
 
 @FXML
 private void handleReceipt(ActionEvent event) throws SQLException {
	float fine = Float.parseFloat(total.getText());
	float pay = Float.parseFloat(cash.getText());
	float bal = Float.parseFloat(balance.getText());
		
	String insert = "INSERT INTO lostbook(member_id,isbn,title,price,elapdays,fine,paid,balance,employee_id) VALUES(?,?,?,?,?,?,?,?,?)";
	PreparedStatement pst = null;
	PreparedStatement ps = null;
	try {
		pst = con.prepareStatement(insert);
		pst.setString(1, member_id);
		pst.setString(2, isbn);
		pst.setString(3, title);
		pst.setFloat(4, price);
		pst.setInt(5, elapdays);
		pst.setFloat(6, fine);
		pst.setFloat(7, pay);
		pst.setFloat(8, bal);
		pst.setInt(9, employee_id);
		pst.executeUpdate();
		
		try {
			ps = con.prepareStatement("delete from lendbook where member_id =? AND book_title =?");
			ps.setString(1,member_id);
			ps.setString(2,title);
			ps.executeUpdate();
			
			generateReceipt();
			total.clear();
			balance.clear();
			cash.clear();
		}catch (SQLException s) {
			s.printStackTrace();
		}finally {
			ps.close();
		}
		
	}catch (SQLException s1) {
		s1.printStackTrace();
	}finally {
		pst.close();
	}
}


 
 private void generateReceipt() {
		try {
			//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
			java.util.Map params = new java.util.HashMap();
			params.put("para_id", member_id);
			params.put("para_isbn", isbn);
			JasperDesign design = JRXmlLoader.load("src/library/receipt/LostBkReceipt.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, params,con);
			JasperViewer.viewReport(jprint,false);
			//con.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
 }
}
