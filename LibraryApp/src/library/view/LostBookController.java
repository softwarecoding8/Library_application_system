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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;
import library.model.LostBook;
import library.util.Preferences;
import library.view.ReturnBookController.BookItem;


public class LostBookController implements Initializable{
	@FXML
	private TextField idField;
	@FXML
	private ComboBox<BookItem> isbn;
	@FXML
	private TextField titleField;
	@FXML
	private TextField priceField;
	@FXML
	private TextField elapField;
	@FXML
	private TextField fineField;
	
	@FXML
	private TableView<LostBook> lostBookView;
	@FXML
	private TableColumn<LostBook,Number> serialNumberColumn;
	@FXML
	private TableColumn<LostBook,String> idColumn;
	@FXML
	private TableColumn<LostBook,String> isbnColumn;
	@FXML
	private TableColumn<LostBook,String> titleColumn;
	@FXML
	private TableColumn<LostBook,Number> priceColumn;
	@FXML
	private TableColumn<LostBook,Number> elapColumn;
	@FXML
	private TableColumn<LostBook,Number> fineColumn;
	@FXML
	private TableColumn<LostBook,Number> paidColumn;
	@FXML
	private TableColumn<LostBook,Number> balanceColumn;
	
	@FXML
	private Button payButton;
	
	private final Connection con;
	private DBHandler handler =  DBHandler.getInstance();
	private LostBkPaymentController lostBkPaymentController;
	
	private float fine_per_day;
	private int employee_id;
	
	private double xOffset = 0;
    private double yOffset = 0;
	
	private ObservableList<LostBook> lostBookViewData =FXCollections.observableArrayList();
	private ObservableList<BookItem> bookData = FXCollections.observableArrayList();
	public  LostBookController() {
		con = handler.getConnection();
	}
		
	public void initialize(URL arg0, ResourceBundle arg1) {
		payButton.setDisable(false);
		if(lostBookView!=null) {
			try {
				lostBookTableView();
			}catch(SQLException ex) {
				
			}
		}	
	}
	
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
	
	@FXML
	private void generateDetails(MouseEvent event)  throws SQLException{
		String id = idField.getText();
		String select = "select * from lendbook where member_id=?";
		PreparedStatement ps =null;
		ResultSet res = null;
		if(id!="") {
		try {
			ps = con.prepareStatement(select);
			ps.setString(1, id);
			res = ps.executeQuery();
		    if(res.next() == true) {
		    	comboBookData(id);
		    	titleField.clear();
				priceField.clear();
			    elapField.clear();
			    fineField.clear();
			}else{
				AlertMaker.showErrorMessage("Error","This member " + idField.getText() +"  have not borrowed a book");
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
	
	@FXML
	private void generateFineDetails(ActionEvent event) throws SQLException{
		String id = idField.getText();
		if(isbn.getValue()!=null) {
		String bk_isbn = isbn.getValue().toString();
		String select = "select b.book_title,b.price,DATEDIFF(now(),l.returndate) as elap from lendbook l JOIN members m  ON l.member_id = m.member_id JOIN books b ON l.book_isbn = b.book_isbn where l.member_id=? and l.book_isbn=?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(select);
			pst.setString(1, id);
			pst.setString(2, bk_isbn);
			rs = pst.executeQuery();
			
			if(rs.next() == true) {
				if(isFinePerDay()) {
					String title = rs.getString("b.book_title");
					String price = rs.getString("b.price");
					String elp = rs.getString("elap");
					titleField.setText(title.trim());
					priceField.setText(String.valueOf(price));
					float priced = Float.parseFloat(price);
					int elaped = Integer.parseInt(elp);
						if(elaped>0) {
							elapField.setText(elp);
							float fined = elaped * fine_per_day;
							float total  = priced + fined;
							fineField.setText(String.valueOf(total));
							
						}else {
							elapField.setText("0");
							fineField.setText(String.valueOf(priced));
						}
					}else {
						
						AlertMaker.showWarningAlert("Warning","Please set fine per day first. It's available under settings");
					}
				}else {
					AlertMaker.showErrorMessage("Error","Something went wrong");
				}
			}catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
				//AlertMaker.showErrorMessage("Error","Something went wrong");
			}finally {
				pst.close();
				rs.close();
			}
		}
	}
	
	public class BookItem{
		String isbn;
		public BookItem(String isbnIn) {
			this.isbn = isbnIn;
		}
		public String toString() {
			return isbn;
		}
	}
	
	void comboBookData(String idIn) throws SQLException {
		String select = "select * from lendbook where member_id=?";
		PreparedStatement ps =null;
		ResultSet res = null;
		try {
			ps = con.prepareStatement(select);
			ps.setString(1, idIn);
			res = ps.executeQuery();
			bookData.clear();
		    while(res.next()) {
					bookData.add(new BookItem(res.getString("book_isbn")));
				}
			}catch (SQLException e1) {
				//AlertMaker.showErrorMessage("Error","Something went wrong");
				e1.printStackTrace();
			}finally{
	            ps.close();
	            res.close();
	        }
			isbn.setItems(bookData);
	}
	
	public boolean isFinePerDay(){
		Preferences pref = Preferences.getPreferences();
		if(pref.getFinePerDay()!=0){
			fine_per_day = pref.getFinePerDay();
			return true;
				
		}else {
			return false;
		}
	}
		
	@FXML	
	public void showPaymentStage(ActionEvent event) throws Exception{
	 
		FXMLLoader loader = new FXMLLoader((getClass().getResource("/library/view/LostBkPayment.fxml")));
		LostBkPaymentController lostBkPaymentController = new LostBkPaymentController();
	    loader.setController(lostBkPaymentController);
	    lostBkPaymentController.setData(idField.getText(),isbn.getValue().toString(),titleField.getText(),Float.parseFloat(priceField.getText()),Integer.parseInt(elapField.getText()),Float.parseFloat(fineField.getText()),employee_id);
	    
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
	    stage.getIcons().add(new Image("file:icon/book.png")) ;
	    stage.setScene(scene);
	    stage.showAndWait();
	    
	    lostBookTableView();
	    reset();
	}
		
	private void reset() {
		idField.clear();
		lostBookView.getSelectionModel().clearSelection();
		isbn.getSelectionModel().clearSelection();
		titleField.clear();
		priceField.clear();
	    elapField.clear();
	    fineField.clear();
		
		idField.requestFocus();
		
	}
		
	@FXML
	private void clearAction(ActionEvent event) {
		reset();
	}
		
	public void lostBookTableView() throws SQLException{
		lostBookViewData.clear();
		String select =  "select * from lostbook";
		PreparedStatement pst = null;
		ResultSet rs = null;
	
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
		
			while(rs.next()) {
			lostBookViewData.add(new LostBook(lostBookViewData.size() + 1,rs.getString("member_id"),rs.getString("isbn"),rs.getString("title"),rs.getFloat("price"),rs.getInt("elapdays"),rs.getFloat("fine"),rs.getFloat("paid"),rs.getFloat("balance")));
			}
		}catch (SQLException e1) {
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
	}
	
	//set cell value factory to tableview
	//NB.PROPERTYvalue factory must be the same with the one set in model class
		serialNumberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		idColumn.setCellValueFactory(data -> data.getValue().idProperty());
		isbnColumn.setCellValueFactory(data -> data.getValue().isbnProperty());
		titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());
		priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
		elapColumn.setCellValueFactory(data -> data.getValue().elapDaysProperty());
		fineColumn.setCellValueFactory(data -> data.getValue().fineProperty());
		paidColumn.setCellValueFactory(data -> data.getValue().paidProperty());
		balanceColumn.setCellValueFactory(data -> data.getValue().balanceProperty());
		
		lostBookView.setItems(null);
		lostBookView.setItems(lostBookViewData);
	}
}
