package library.view;

import library.model.Book;
import library.model.Member;
import library.util.LibraryAppUtil;
import library.view.IssueBookController.IssueBook;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;

import com.pdfjet.A4;
import com.pdfjet.Cell;
import com.pdfjet.CoreFont;
//import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Table;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class MemberController implements Initializable{
	@FXML
	private TableView<Member> memberTable;
	@FXML
	private TableColumn<Member,Number> serialNumberColumn;
	@FXML
	private TableColumn<Member,Number> idColumn;
	@FXML
	private TableColumn<Member,String> firstNameColumn;
	@FXML
	private TableColumn<Member,String> lastNameColumn;
	@FXML
	private TableColumn<Member,String> locationColumn;
	@FXML
	private TableColumn<Member,String> mobileNumberColumn;
	@FXML
	private TableColumn<Member,String> emailColumn;
	
	@FXML
	private TextField memberIdField;
	@FXML
	private TextField firstNameField;
	@FXML
	private TextField lastNameField;
	@FXML
	private TextField locationField;
	@FXML
	private TextField mobileNumberField;
	@FXML
	private TextField emailField,searchField;
	
	@FXML
	private Button update_button;
	@FXML
	private Button delete_button;
	@FXML
	private Button add_button;
	@FXML
	private Button exportButton;
	private FileChooser fileChooser = new FileChooser();
	private ObservableList<Member> list =FXCollections.observableArrayList(); 
	private final  Connection con;
	private DBHandler handler =  DBHandler.getInstance();
	
	private int employee_id;
	//The constructor is called before the initialize() method
	public MemberController() {
			con = handler.getConnection();
	}
		
	//initializes the controller class.this method is automatically called after the fxml file has been loaded
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			try {
				if(memberTable!=null) {
					memberTableView();
				}
			}catch(SQLException ex) {
				
			}
			filterData();
			add_button.setDisable(false);
			memberIdField.setEditable(true);
		}
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
		@FXML
		private void tablehandleButtonAction(MouseEvent event) {
			Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
			if(selectedMember!=null) {
			memberIdField.setText(String.valueOf(selectedMember.getMemberId()));
			firstNameField.setText(selectedMember.getFirstName());
			lastNameField.setText(selectedMember.getLastName());
			locationField.setText(selectedMember.getLocation());
			mobileNumberField.setText(String.valueOf(selectedMember.getMobileNumber()));
			emailField.setText(selectedMember.getEmail());
			
			add_button.setDisable(true);
			memberIdField.setEditable(false);
			}
	}
		
	public void delete() throws SQLException {
		PreparedStatement pst = null;
		String delete ="DELETE FROM members where member_id = ?";
		try {
			pst = con.prepareStatement(delete);
			pst.setInt(1, Integer.parseInt(memberIdField.getText()));
			showConfirm(pst);
			//pst.executeUpdate();
			memberTableView();
		}catch(SQLException ex) {
			//ex.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}
	
	private void showConfirm(PreparedStatement pst) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("Are you sure want to delete this member?");
		
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
			pst.executeUpdate();
			AlertMaker.showSimpleAlert("Success","Selected member deleted successfully!!.");
			clear();
			}catch (SQLException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}
	}
	
	private void update() throws SQLException {
		PreparedStatement pst = null;
		if(validateInputUpdate()) {
		String update = "UPDATE members SET fname=?,lname=?,location=?,mobile_number=?,email=?,employee_id=? where member_id=?";
		try {
			pst = con.prepareStatement(update);
			pst.setString(1, firstNameField.getText());
			pst.setString(2, lastNameField.getText());
			pst.setString(3, locationField.getText());
			pst.setString(4, mobileNumberField.getText());
			pst.setString(5, emailField.getText());
			pst.setInt(6, employee_id);
			pst.setInt(7, Integer.parseInt(memberIdField.getText()));
			
			showConfirmation(pst);
			//pst.executeUpdate();
			memberTableView();
			
		}catch(SQLException ex) {
			
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
		}
	
	}
	
	private void showConfirmation(PreparedStatement pst) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Update");
		alert.setHeaderText("Are you sure want to update this member details?");
		
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
			 pst.executeUpdate();
			 AlertMaker.showSimpleAlert("Success","Selected member updated successfully!!.");
			 clear();
			}catch (SQLException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				AlertMaker.showErrorMessage("Error","Something went wrong.");
				
			}
		}
	}
	
	void clear() {
		memberIdField.clear();
		firstNameField.clear();
		lastNameField.clear();
		locationField.clear();
		mobileNumberField.clear();
		emailField.clear();
		
		memberIdField.requestFocus();
		
	}
	
	@FXML 
	private void updateEvent(ActionEvent event) throws SQLException {
		Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
		if(selectedMember!=null) {
			update();
		}else {
			
			AlertMaker.showWarningAlert("Warning","Please select a member in the table to update.");
		}
		memberTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
		memberIdField.setEditable(true);
	}
	
	@FXML
	private void deleteEvent(ActionEvent event) throws SQLException{
		Member selectedMember = memberTable.getSelectionModel().getSelectedItem();
		if(selectedMember!=null) {
			delete();
		}else {
			
			AlertMaker.showWarningAlert("Warning","Please select a member in the table to delete.");
		}
		memberTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
		memberIdField.setEditable(true);
	}
	
	@FXML
	private void clearEvent(ActionEvent event) {
		clear();
		memberTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
		memberIdField.setEditable(true);
	}
	
	public ObservableList<Member> getMember() throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		String select = "select * from members";
		
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
			while(rs.next()) {
				list.add(new Member(list.size() + 1,rs.getInt("member_id"),rs.getString("fname"),rs.getString("lname"),rs.getString("location"),rs.getString("mobile_number"),rs.getString("email")));
			}
		}catch(SQLException ex) {
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
		return list;
	}
	
	public void memberTableView() throws SQLException {
		list.clear();
		ObservableList<Member> list =getMember();
		serialNumberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		idColumn.setCellValueFactory(data -> data.getValue().memberIdProperty());
		firstNameColumn.setCellValueFactory(data -> data.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(data -> data.getValue().lastNameProperty());;
		locationColumn.setCellValueFactory(data -> data.getValue().locationProperty());
		mobileNumberColumn.setCellValueFactory(data -> data.getValue().mobileNumberProperty());
		emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());
		
		memberTable.setItems(null);
		memberTable.setItems(list);
	}
	
	public void handleAddMemberAction() throws SQLException {
		if(validateInput()) {
		String insert = "INSERT INTO members(member_id,fname,lname,location,mobile_number,email,employee_id) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement pst = null;
		
		try {
			pst = con.prepareStatement(insert);
			pst.setInt(1,Integer.parseInt(memberIdField.getText()));
			pst.setString(2,firstNameField.getText());
			pst.setString(3,lastNameField.getText());
			pst.setString(4, locationField.getText());
			pst.setString(5, mobileNumberField.getText());
			pst.setString(6, emailField.getText());
			pst.setInt(7, employee_id);
			pst.executeUpdate();
			clear();
			memberTableView();
			AlertMaker.showSimpleAlert("Success","New member added successfully!!.");
			
			} catch (SQLException e1) {
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}finally {
				pst.close();
			}
		}
	}
	
	private boolean validateInput() {
		try {
	    	String errorMessage = "";
	        if((memberIdField.getText().isEmpty()||firstNameField.getText().isEmpty()|| lastNameField.getText().isEmpty() || locationField.getText().isEmpty() || mobileNumberField.getText().isEmpty() || emailField.getText().isEmpty())){
	        	errorMessage += "Please fill blank fields!!\n";
	        } 
	        if(!((firstNameField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid first name format!!\n";
	           
	        }
	        if(!((lastNameField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid last name format!!\n";
	        }
	        if(!((locationField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid location format!!\n";
	        }
	        
	        if(!(mobileNumberField.getText().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))){
	        	errorMessage += "invalid mobile number!!\n";
	        }
	        if(!(isValid(emailField.getText().toLowerCase()))){
	        	errorMessage += "invalid email!!\n";
	        }
	        
	        if(isEmail(emailField.getText().toLowerCase())){
			   errorMessage += "cannot add this email " + emailField.getText() + " because already present!!\n";
	        }
	        if(isIdNumber(memberIdField.getText())){
			   errorMessage += "cannot add this id number "+ memberIdField.getText() +" because already present!!\n";
		    }
	        
	        if(isPhoneNumber(mobileNumberField.getText())){
			   errorMessage += "cannot add this mobile number "+ mobileNumberField.getText() +" because already present!!\n";
		     }
	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("Invalid Fields");
	            alert.setHeaderText("Please correct invalid fields");
	            alert.setContentText(errorMessage);
	            alert.showAndWait();
	
	            return false;
	        }
	      
	    } catch (SQLException ex) {
	    	AlertMaker.showErrorMessage("Error","Something went wrong.");
	    	}
			return true;
	   }
	
	private boolean validateInputUpdate() {
		try {
	    	String errorMessage = "";
	        if((memberIdField.getText().isEmpty()||firstNameField.getText().isEmpty()|| lastNameField.getText().isEmpty() || locationField.getText().isEmpty() || mobileNumberField.getText().isEmpty() || emailField.getText().isEmpty() )){
	        	errorMessage += "Please fill blank fields!!\n";
	          
	        } 
	        if(!((firstNameField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid name format!!\n";
	           
	        }
	        if(!((lastNameField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid name format!!\n";
	        }
	        if(!((locationField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid location format!!\n";
	        }
	        
	        if(!(mobileNumberField.getText().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))){
	        	errorMessage += "invalid mobile number!!\n";
	        }
	        if(!(isValid(emailField.getText().toLowerCase()))){
	        	errorMessage += "invalid email!!\n";
	        }
	       if(isEmailUpdate(emailField.getText().toLowerCase(),memberIdField.getText())){
				   errorMessage += "cannot update this email "+ emailField.getText() + " because already present!!\n";
				}
			
	        if(isPhoneNumberUpdate(mobileNumberField.getText(),memberIdField.getText())){
				   errorMessage += "cannot update this mobile number "+ mobileNumberField.getText() + " because already present!!\n";
			     }
	        if (errorMessage.length() == 0) {
	            return true;
	        } else {
	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("Invalid Fields");
	            alert.setHeaderText("Please correct invalid fields");
	            alert.setContentText(errorMessage);
	            alert.showAndWait();
	
	            return false;
	        }
	        
	        } catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
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
	
	public  boolean isEmail(String email) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from members where email=?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,email);
	       resultSet=preparedStatement.executeQuery();
	        if(resultSet.next()){
	            return true;
	        }else{
	            return false;
	        }
	    }catch(SQLException e){
	        //System.out.println(" no!"+e);
	        return false;
	    }finally{
	        preparedStatement.close();
	        resultSet.close();
	    }
	}
	
	public  boolean isEmailUpdate(String email,String id) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from members where email=? and member_id !=?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,email);
	        preparedStatement.setInt(2, Integer.parseInt(id));
	        resultSet=preparedStatement.executeQuery();
	        if(resultSet.next()){
	            return true;
	        }else{
	            return false;
	        }
	    }catch(SQLException e){
	        //System.out.println(" no!"+e);
	        return false;
	    }finally{
	        preparedStatement.close();
	        resultSet.close();
	    }
	}
	
	public  boolean isPhoneNumberUpdate(String phoneNumber,String id) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from members where mobile_number=? and member_id !=?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,phoneNumber);
	        preparedStatement.setInt(2,Integer.parseInt(id));
	        resultSet=preparedStatement.executeQuery();
	        if(resultSet.next()){
	            return true;
	        }else{
	            return false;
	        }
	    }catch(SQLException e){
	        //System.out.println(" no!"+e);
	        return false;
	    }finally{
	        preparedStatement.close();
	        resultSet.close();
	    }
	}
	
	public  boolean isPhoneNumber(String phoneNumber) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from members where mobile_number=?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,phoneNumber);
	        resultSet=preparedStatement.executeQuery();
	        if(resultSet.next()){
	            return true;
	        }else{
	            return false;
	        }
	    }catch(SQLException e){
	        //System.out.println(" no!"+e);
	        return false;
	    }finally{
	        preparedStatement.close();
	        resultSet.close();
	    }
	}
	
	public  boolean isIdNumber(String idNumber) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from members where member_id =?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,idNumber);
	        resultSet=preparedStatement.executeQuery();
	        if(resultSet.next()){
	            return true;
	        }else{
	            return false;
	        }
	    }catch(SQLException e){
	        //System.out.println(" no!"+e);
	        return false;
	    }finally{
	        preparedStatement.close();
	        resultSet.close();
	    }
	}	
	@FXML
	public void printMember() {
		try {
			//String design = "C://Users/Josphat/eclipse-workspace/LibraryApp/src/library/report/Book.jrxml";
			java.util.Map params = new java.util.HashMap();
			params.put("para_id", memberIdField.getText());
			JasperDesign design = JRXmlLoader.load("src/library/report/Member.jrxml");
			JasperReport report = JasperCompileManager.compileReport(design);
			JasperPrint jprint = JasperFillManager.fillReport(report, params,con);
			JasperViewer.viewReport(jprint,false);
			con.close();
			
			
		}catch (Exception e) {
			//e.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
	
	private void filterData() {
	
	    FilteredList<Member> searchedData = new FilteredList<>(list, e -> true);
	    searchField.setOnKeyReleased(e -> {
	        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
	            searchedData.setPredicate(member -> {
	                if (newValue == null || newValue.isEmpty()) {
	                    return true;
	                }
	                String lowerCaseFilter = newValue.toLowerCase();
	                if (member.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                } else if (member.getLastName().toLowerCase().contains(lowerCaseFilter)) {
	                    return true;
	                }
	                return false;
	            });
	        });
	
	        SortedList<Member> sortedData = new SortedList<>(searchedData);
	        sortedData.comparatorProperty().bind(memberTable.comparatorProperty());
	        memberTable.setItems(sortedData);
	    });
	}
	
	@FXML
	private void exportAsPDF(ActionEvent event)  throws FileNotFoundException, DocumentException  {
		File file = new File("");
		Window stage = exportButton.getScene().getWindow();
		fileChooser.setTitle("Save PDF File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		file = fileChooser.showSaveDialog(stage);
	
		if (file != null) {
			// create document
	        Document document = new Document(PageSize.A4, 36, 36, 90, 50);
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
	
	        
	        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
	        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,8);
			//Font f2 = new Font(pdf, CoreFont.HELVETICA);
	        
	        PdfPTable table = new PdfPTable(7);
	        //table.setTotalWidth(500);
	        //table.setLockedWidth(true);
	        
	        PdfPCell table_cell = new PdfPCell(new Phrase(serialNumberColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(idColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(firstNameColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(lastNameColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(locationColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(mobileNumberColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(emailColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        //table.setTotalWidth(400);
	        //table.setLockedWidth(true);
	        
	        table.setWidths(new float[] {1,2,2,2,2,2,4});
	        
	        table.setHeaderRows(1);
	        
	        List<Member> items = memberTable.getItems();
	        //PdfPCell table_cell;
	        for (Member a : items) {
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getSerialNumber()),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getMemberId()),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase( a.getFirstName(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(a.getLastName(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase( a.getLocation(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(a.getMobileNumber(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getEmail()),f1));
				table.addCell(table_cell);
				
			}
	      
	        // add header and footer
	        HeaderFooterMemberPageEvent evt = new HeaderFooterMemberPageEvent();
	        writer.setPageEvent(evt);
	
	        // write to document
	        document.open();
	        document.add(table);
	        document.close();
			}
	
	}	
	
}
