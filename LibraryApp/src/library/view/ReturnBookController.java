package library.view;

import java.io.IOException;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ResourceBundle;

import library.DBConnection.DBHandler;
import library.alert.AlertMaker;
import library.model.ReturnBook;
import library.util.Preferences;
import library.view.PaymentController;
import library.view.IssueBookController.BookItem;
import library.view.IssueBookController.IssueBook;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
public class ReturnBookController implements Initializable{
	@FXML
	private TextField member_id;
	@FXML
	private TextField name;
	@FXML
	private ComboBox<BookItem> title;
	@FXML
	private DatePicker issued_date;
	@FXML
	private TextField elapdays;
	@FXML
	private TextField fine;
	@FXML
	private TextField pay;
	@FXML
	private TextField balance;
	@FXML
	private Button payButton;
	@FXML
	private Button returnBookBtn;
	private int employee_id;
	
	@FXML
	private TableView<ReturnBook> returnBookView;
	@FXML
	private TableColumn<ReturnBook,Number> serialNumberColumn;
	@FXML
	private TableColumn<ReturnBook,String> nameColumn;
	@FXML
	private TableColumn<ReturnBook,String> titleColumn;
	@FXML
	private TableColumn<ReturnBook,Date> issueDateColumn;
	@FXML
	private TableColumn<ReturnBook,Date> reportDateColumn;
	@FXML
	private TableColumn<ReturnBook,Number> elapDaysColumn;
	@FXML
	private TableColumn<ReturnBook,Number> fineColumn;
	@FXML
	private TableColumn<ReturnBook,Number> paidColumn;
	@FXML
	private TableColumn<ReturnBook,Number> balanceColumn;
	
	Date sqlDate = null;
	
	private Connection con;
	private DBHandler handler =  DBHandler.getInstance();
	private PaymentController payController;
	
	private float finee;
	private float fine_per_day;
	
	private double xOffset = 0;
    private double yOffset = 0;
    private ObservableList<ReturnBook> returnBookViewData =FXCollections.observableArrayList();
    private ObservableList<BookItem> bookData = FXCollections.observableArrayList();
	//The constructor is called before the initialize() method
	public ReturnBookController() {
		con = handler.getConnection();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		payButton.setDisable(false);
		returnBookBtn.setDisable(false);
		try {
			if(returnBookView!=null) {
				returnBookTableView();
			}
		}catch(SQLException ex) {
			
		}
	}
	
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
	
	public class BookItem{
		String title;
		public BookItem(String title) {
			this.title = title;
		}
		public String toString() {
			return title;
		}
	}
	
	public boolean isFinePerDay(){
		Preferences pref = Preferences.getPreferences();
		if(pref!=null){
			fine_per_day = pref.getFinePerDay();
			return true;
				
		}else {
			return false;
		}
	}
		
	public void paySet() {
		String pay2 = pay.getText();
		float f = Float.parseFloat(pay2);
		float bal = f - finee;
		balance.setText(String.valueOf(bal));
	}
	
	@FXML
	private void generateDetails(MouseEvent event)  throws SQLException{
		String mid = member_id.getText();
		String select = "select * from lendbook where member_id=?";
		PreparedStatement ps =null;
		ResultSet res = null;
		if(mid!="") {
		try {
			ps = con.prepareStatement(select);
			ps.setString(1, mid);
			res = ps.executeQuery();
		    if(res.next() == true) {
		    	String fname = res.getString("member_fname");
				name.setText(fname.trim());
				comboBookData(mid);
				issued_date.setValue(null);
			    elapdays.clear();
			    fine.clear();
			}else{
				AlertMaker.showErrorMessage("Error","This Member ID " + member_id.getText() +"  have not borrowed a book");
				reset();
			}
			
			}catch (SQLException e1) {
				AlertMaker.showErrorMessage("Error","Something went wrong");
			}finally{
	            ps.close();
	            res.close();
	        }
		}
			
	}
	
	void comboBookData(String midIn) throws SQLException {
		String select = "select * from lendbook where member_id=?";
		PreparedStatement ps =null;
		ResultSet res = null;
		try {
			ps = con.prepareStatement(select);
			ps.setString(1, midIn);
			res = ps.executeQuery();
			bookData.clear();
		    while(res.next()) {
					bookData.add(new BookItem(res.getString("book_title")));
				}
			}catch (SQLException e1) {
				AlertMaker.showErrorMessage("Error","Something went wrong");
			}finally{
	            ps.close();
	            res.close();
	        }
			title.setItems(bookData);
	}
	
	@FXML
	private void generateFineDetails(ActionEvent event) throws Exception{
		String mid = member_id.getText();
		if(title.getValue()!=null) {
		String book = title.getValue().toString();
		String select = "select l.issuedate,DATEDIFF(now(),l.returndate) as elap from lendbook l JOIN members m  ON l.member_id = m.member_id JOIN books b ON l.book_isbn = b.book_isbn where l.member_id=? and l.book_title =?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(select);
			pst.setString(1, mid);
			pst.setString(2, book);
			rs = pst.executeQuery();
			
			if(rs.next() == true) {
				if(isFinePerDay()){
					Date issuedatee =rs.getDate("l.issuedate");
					String elp = rs.getString("elap");
					issued_date.setValue(issuedatee.toLocalDate());
					int elaped = Integer.parseInt(elp);
					if(elaped>0) {
						elapdays.setText(elp);
						float finee = elaped * fine_per_day;
						fine.setText(String.valueOf(finee));
						returnBookBtn.setDisable(true);
						payButton.setDisable(false);
					}else {
						elapdays.setText("0");
						fine.setText("0");
						payButton.setDisable(true);
						returnBookBtn.setDisable(false);
					}
					}else {
					AlertMaker.showWarningAlert("Warning","Please set fine per day first. It's available under settings");
					}
				}
			}catch (SQLException e1) {
				AlertMaker.showErrorMessage("Error","Something went wrong.");
				//e1.printStackTrace();
			}finally {
				pst.close();
				rs.close();
			}
		}
	}
	
	public void showPaymentStage(ActionEvent event) throws Exception{
	
	    FXMLLoader loader = new FXMLLoader((getClass().getResource("/library/view/Payment.fxml")));
	    
	    //System.out.println(fine.getText().trim());
	  System.out.println(title.getValue().toString());
	    if(issued_date.getValue() != null) {
			java.util.Date issue_date = java.util.Date.from(issued_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
			java.sql.Date sqlDatee = new java.sql.Date(issue_date.getTime());
			sqlDate = sqlDatee;
		}
	    
	    PaymentController paymentController = new PaymentController();
	    loader.setController(paymentController);
	    paymentController.setData(Float.parseFloat(fine.getText().trim()),Integer.parseInt(member_id.getText().trim()),name.getText(),title.getValue().toString(),sqlDate,elapdays.getText(),employee_id);
	    Parent root = loader.load();
	    Stage stage = new Stage();
	    root.setOnMousePressed((MouseEvent e) -> {
	        xOffset = e.getSceneX();
	        yOffset = e.getSceneY();
	    });
	    root.setOnMouseDragged((MouseEvent e) -> {
	        stage.setX(e.getScreenX() - xOffset);
	        stage.setY(e.getScreenY() - yOffset);
	    });
	    Scene scene = new Scene(root);
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Payment");
	    stage.initStyle(StageStyle.DECORATED);
	    //stage.getIcons().add(new Image("/images/logo.png"));
	    stage.getIcons().add(new Image("file:icon/book.png"));
	    stage.setScene(scene);
	    stage.showAndWait();
	    stage.setResizable(false);
	    
	    returnBookTableView();
	    reset();
	}
		
	private void reset() {
		member_id.clear();
		name.clear();
		returnBookView.getSelectionModel().clearSelection();
		title.getSelectionModel().clearSelection();
		issued_date.setValue(null);
	    elapdays.clear();
	    fine.clear();
		
		member_id.requestFocus();
		
	}
		
	@FXML
	private void clearAction(ActionEvent event) {
		member_id.clear();
		name.clear();
		returnBookView.getSelectionModel().clearSelection();
		title.getSelectionModel().clearSelection();
		issued_date.setValue(null);
	    elapdays.clear();
	    fine.clear();
		
		member_id.requestFocus();
	}
		
	@FXML
	private void addReturnBkAction(ActionEvent event) throws SQLException{
	    PreparedStatement pst = null;
	    PreparedStatement ps = null;
		if(issued_date.getValue() != null) {
				java.util.Date issue_date = java.util.Date.from(issued_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				java.sql.Date sqlDatee = new java.sql.Date(issue_date.getTime());
				sqlDate = sqlDatee;
		}
		
		String memberId  = member_id.getText();
		String memberName = name.getText();
		String book = title.getValue().toString();
		int elapDays = Integer.parseInt(elapdays.getText());
		float fined = Float.parseFloat(fine.getText());
		float paidd = 0;
		float balancee = 0;
		
		String insert = "INSERT INTO returnbook(member_id,fname,title,issuedate,elapdays,fine,paid,balance,employee_id) VALUES(?,?,?,?,?,?,?,?,?)";
		try {
			pst = con.prepareStatement(insert);
			pst.setString(1, memberId);
			pst.setString(2, memberName);
			pst.setString(3, book);
			pst.setDate(4, sqlDate);
			pst.setInt(5, elapDays);
			pst.setFloat(6, fined);
			pst.setFloat(7, paidd);
			pst.setFloat(8, balancee);
			pst.setFloat(9, employee_id);
			pst.executeUpdate();
			
			try {
				ps = con.prepareStatement("delete from lendbook where member_id =?");
				ps.setString(1,memberId);
				int k =ps.executeUpdate();
			
				if(k==1) {
					AlertMaker.showSimpleAlert("Success","This " + book + " book returned successfully!!.");
					returnBookTableView();
					updateBooksQty(book);
					reset();
					
				}else {
					AlertMaker.showWarningAlert("Warning","Something went wrong");
				}
			}catch (SQLException s) {
				//s.printStackTrace();
				AlertMaker.showWarningAlert("Warning","Something went wrong");
			}finally {
				ps.close();
			}
		}catch (SQLException s) {
			//s.printStackTrace();
			AlertMaker.showWarningAlert("Warning","Something went wrong");
		}finally {
			pst.close();
		}
		
	}
	private void updateBooksQty(String bookIn) throws SQLException{
		PreparedStatement ps =null;
		ResultSet rs = null;
		String select = "update books set stock = stock +1  where book_title=?";
		ps = con.prepareStatement(select);
		ps.setString(1, bookIn);
		ps.executeUpdate();
		
	}	
	public void returnBookTableView() throws SQLException {
		returnBookViewData.clear();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String select =  "select * from returnbook";
		
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
			while(rs.next()) {
				
				returnBookViewData.add(new ReturnBook(returnBookViewData.size() + 1,rs.getString("fname"),rs.getString("title"),rs.getDate("issuedate"),rs.getDate("reportdate"),rs.getInt("elapdays"),rs.getInt("fine"),rs.getInt("paid"),rs.getInt("balance")));
			
			}
		}catch (SQLException e1) {
			//System.err.println("Error" +e1);
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
		
		//set cell value factory to tableview
		//NB.PROPERTYvalue factory must be the same with the one set in model class
		//columnId.setCellValueFactory(data -> data.getValue().idProperty());
		serialNumberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		nameColumn.setCellValueFactory(data -> data.getValue().memberFNameProperty());
		titleColumn.setCellValueFactory(data -> data.getValue().bookTitleProperty());
		issueDateColumn.setCellValueFactory(data -> data.getValue().issuedateProperty());
		reportDateColumn.setCellValueFactory(data -> data.getValue().reportdateProperty());
		elapDaysColumn.setCellValueFactory(data -> data.getValue().elapDaysProperty());
		fineColumn.setCellValueFactory(data -> data.getValue().fineProperty());
		paidColumn.setCellValueFactory(data -> data.getValue().paidProperty());
		balanceColumn.setCellValueFactory(data -> data.getValue().balanceProperty());
		
		returnBookView.setItems(null);
		returnBookView.setItems(returnBookViewData);
	}

}
