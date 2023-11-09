package library.view;

import com.google.common.base.Functions;
import com.pdfjet.A4;
import com.pdfjet.Cell;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Table;
import library.DBConnection.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;
import library.model.Order;
import library.util.LibraryAppUtil;
import library.util.Preferences;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
	
public class IssueBookController implements Initializable{
	@FXML
	private TextField member_id;
	@FXML
	private TextField member_fname;
	@FXML
	private TextField book_isbn;
	@FXML
	private TextField book_title;
	@FXML
	private DatePicker issuedate;
	@FXML
	private DatePicker returndate;
	
	Date sqlDate = null;
	Date sqlDate2 = null;   
	String errorMessage = "";
	int days_without_fine = 0;
	private int employee_id;
	
	
	@FXML
	private TableView<IssueBook> issueBookView;
	@FXML
	private TableColumn<IssueBook,Number> columnSerialNumber;
	@FXML
	private TableColumn<IssueBook,Number> columnMemberID;
	@FXML
	private TableColumn<IssueBook,String> columnFName;
	@FXML
	private TableColumn<IssueBook,String> columnBookISBN;
	@FXML
	private TableColumn<IssueBook,String> columnBookTitle;
	@FXML
	private TableColumn<IssueBook,String> columnBook;
	@FXML
	private TableColumn<IssueBook,Date> columnIssueDate;
	@FXML
	private TableColumn<IssueBook,Date> columnReturnDate;

	@FXML
	private ComboBox<BookItem> book;
	@FXML
	private AnchorPane contentPane;
	 
	 @FXML
	 private Button btnPdfExport;
	 @FXML
	 private Button issueBookBtn;
	 //issueBookBtn.setDisable(false);
	 private FileChooser fileChooser = new FileChooser();
	 private ObservableList<BookItem> bookData = FXCollections.observableArrayList();
	 private ObservableList<IssueBook> issueBookViewData =FXCollections.observableArrayList(); 
	 private final Connection con;
	 private DBHandler handler = DBHandler.getInstance();
	
	//The constructor is called before the initialize() method
	public  IssueBookController	()	{
		con = handler.getConnection();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		try {
			if(issueBookView!=null) {
				issueBookTableView();
			}
		}catch(SQLException ex) {
			
		}
		issueBookBtn.setDisable(false);
		member_fname.setEditable(false);
		book_title.setEditable(false);
		member_id.setEditable(true);
	}
	
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
	@FXML
	private void generateName(MouseEvent event) throws SQLException{
		String mid = member_id.getText();
		String select = "select * from members where member_id=?";
		PreparedStatement pst=null;
		ResultSet rs=null;
		if(mid!="") {
		try {
			pst = con.prepareStatement(select);
			pst.setString(1, mid);
			rs = pst.executeQuery();
			if(rs.next() == false) {
				AlertMaker.showErrorMessage("Error","This Id number " + member_id.getText() + " not found in our records");
			}else {
				String memberfname = rs.getString("fname");
				member_fname.setText(memberfname.trim());
			}
			}catch (SQLException e1) {
			
				AlertMaker.showErrorMessage("Error","Something went wrong");
			}finally {
				pst.close();
				rs.close();
			}
		 }
	}
			
	@FXML
	private void generateBkTitle(MouseEvent event)  throws SQLException{
		String b_isbn = book_isbn.getText();
		String select = "select * from books where book_isbn=?";
		PreparedStatement ps =null;
		ResultSet res = null;
		if(b_isbn!="") {
		try {
			ps = con.prepareStatement(select);
			ps.setString(1, b_isbn);
			res = ps.executeQuery();
			if(res.next() == false) {
				AlertMaker.showErrorMessage("Error","This book ISBN " + book_isbn.getText() +"  is not availabe in our library");
				book_isbn.requestFocus();
			}else{
				int qty =res.getInt("stock");
				if(qty>0) {
					String bookname = res.getString("book_title");
					book_title.setText(bookname.trim());
				
				 }else {
					 
					 AlertMaker.showWarningAlert("Warning","This book ISBN " + book_isbn.getText() +"  has been issued to someone else,please check again some days later!!");
				}
			}
			}catch (SQLException e1) {
				AlertMaker.showErrorMessage("Error","Something went wrong");
			}finally{
	            ps.close();
	            res.close();
	        }
		}
	}
			
	public class BookItem{
		int book_id;
		String title;
		public BookItem(int book_id,String title) {
			this.book_id = book_id;
			this.title = title;
		}
		public String toString() {
			return title;
		}
	}
			
	public class IssueBook{
		private final IntegerProperty serial_number;
		private final IntegerProperty member_id;
		private final StringProperty member_fname;
		private final StringProperty book_isbn;
		private final StringProperty book_title;
		private final ObjectProperty<Date> issuedate;
		private final ObjectProperty<Date> returndate;
		
		public IssueBook(Integer serial_numberIn,Integer member_idIn,String member_fnameIn,String book_isbnIn,String book_titleIn,Date issuedateIn,Date returndateIn) {
			this.serial_number = new SimpleIntegerProperty(serial_numberIn);
			this.member_id = new SimpleIntegerProperty(member_idIn);
			this.member_fname = new SimpleStringProperty(member_fnameIn);
			this.book_isbn = new SimpleStringProperty(book_isbnIn); 
			this.book_title = new SimpleStringProperty(book_titleIn);
			this.issuedate = new SimpleObjectProperty(issuedateIn);
			this.returndate = new SimpleObjectProperty(returndateIn);
		}
		
		//getters
		public Integer getSerialNumber(){
			return this.serial_number.get();
		}
		
		public Integer getMemberId(){
			return this.member_id.get();
		}
		
		public String getMemberFName() {
			
			return this.member_fname.get();
		}
		public String getBookISBN() {
			return this.book_isbn.get();
		}
		public String getBookTitle() {
			return this.book_title.get();
		}
		public Date getIssueDate() {
			return this.issuedate.get();
		}
		public Date getReturnDate() {
			return this.returndate.get();
		}
		
		//setters
		public void setSerialNumber(Integer value) {
			this.serial_number.set(value);
		}
		public void setMemberId(Integer value) {
			this.member_id.set(value);
		}
		public void setMemberFName(String value) {
			this.member_fname.set(value);
		}
		public void setBookISBN(String value) {
			this.book_isbn.set(value);
		}
		public void setBookTitle(String value) {
			this.book_title.set(value);
		}
		public void setIssueDate(Date value) {
			this.issuedate.set(value);
		}
		public void setReturnDate(Date value) {
			this.returndate.set(value);
		}
		//property values
		public IntegerProperty serialNumberProperty() {
			return this.serial_number;
		}
		public IntegerProperty memberIdProperty() {
			return this.member_id;
		}
		public StringProperty memberFNameProperty() {
			return this.member_fname;
		}
		public StringProperty bookISBNProperty() {
			return this.book_isbn;
		}
		public StringProperty bookTitleProperty() {
			return this.book_title;
		}
		public ObjectProperty<Date> issuedateProperty(){
			return this.issuedate;
		}
		public ObjectProperty<Date> returndateProperty(){
			return this.returndate;
		}
	
	}
			
	@FXML
	private void bookActionComboBox(ActionEvent event) throws SQLException{
		if(event.getSource() == book) {
			PreparedStatement pss = null;
			ResultSet rss = null;
		try {
			
			pss = con.prepareStatement("select * from books");
			rss = pss.executeQuery();
			 // bk = book.getItems();
			bookData.clear();
			while(rss.next()) {
				bookData.add(new BookItem(rss.getInt(1),rss.getString(3)));
			}
		
			}catch(SQLException s) {
			// TODO Auto-generated catch block
			//s.printStackTrace();
			}finally{
		        pss.close();
		        rss.close();
		    }
			book.setItems(bookData);
		}else {
			
			AlertMaker.showErrorMessage("Error","Something went wrong");
		}
	}
			
	public  boolean isDaysWithoutFine()throws SQLException {
			Preferences pref = Preferences.getPreferences();
			if(pref!=null){
				days_without_fine = pref.getDaysWithNoFine();
				return true;
					
			}else {
				return false;
			}
	}
			
	@FXML
	private void addIssueBkAction(ActionEvent event) throws SQLException{
		String memb_id = member_id.getText();
		String memb_fname = member_fname.getText();
		String bk_isbn = book_isbn.getText();
		String bk_title = book_title.getText();
		
		PreparedStatement pst = null;
			if(validateInput()) {
			String insert = "INSERT INTO lendbook(member_id,member_fname,book_isbn,book_title,issuedate,returndate,employee_id) VALUES(?,?,?,?,?,?,?)";
			pst = con.prepareStatement(insert);
			
			try {
				pst.setString(1, memb_id);
				pst.setString(2, memb_fname);
				pst.setString(3, bk_isbn);
				pst.setString(4, bk_title);
				pst.setDate(5, sqlDate);
				pst.setDate(6, sqlDate2);
				pst.setInt(7, employee_id);
				int k = pst.executeUpdate();
				if(k==1) {
					
					AlertMaker.showSimpleAlert("Success","This " + book_title.getText() + " book issued successfully!!!." );
					issueBookTableView();
					updateBooksQty(bk_isbn);
					clear();
					
				}
				}catch(SQLException s) {
					AlertMaker.showErrorMessage("Error","Something went wrong.");
				}finally{
		            pst.close();
		        }
			}
		issueBookBtn.setDisable(false);
		member_id.setEditable(true);
	}
	
	private void updateBooksQty(String bk_isbnIn) throws SQLException{
		PreparedStatement ps =null;
		ResultSet rs = null;
		String select = "update books set stock = stock -1  where book_isbn=?";
		ps = con.prepareStatement(select);
		ps.setString(1, bk_isbnIn);
		ps.executeUpdate();
		
	}
	private boolean validateInput() {
	try {	
	    if(member_id.getText().isEmpty()|| member_fname.getText().isEmpty() || book_isbn.getText().isEmpty() || book_title.getText().isEmpty() ){
	    	errorMessage += "Please fill blank fields!!\n";
	    }
	   
	   	if(!(isDaysWithoutFine())){
			errorMessage += "Please set days without fine first. It is available under Settings!!\n";
		}
	   	
	   	if(bookLoanLimit()){
			errorMessage += "You can borrowed a maximum of 3 books only!!\n";
		}
	   	
	   	if(issuedate.getValue()!= null) {
	   		LocalDate date = issuedate.getValue();
			sqlDate = Date.valueOf(date);
			
	   		LocalDate date2 = issuedate.getValue().plusDays(days_without_fine);
			sqlDate2 = Date.valueOf(date2);
	   	}else {
	        errorMessage += "Please fill issue date!!\n";
	    }
	   if (errorMessage.length() == 0) {
	        return true;
	    } else {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Invalid Fields");
	        alert.setHeaderText("Please correct invalid fields");
	        alert.setContentText(errorMessage);
	        alert.showAndWait();
	        
	        errorMessage = "";
	        return false;
	       
	    }
	} catch (SQLException e) {
		AlertMaker.showErrorMessage("Error","Something went wrong.");
	}
	    return true;
	}
	
	private boolean bookLoanLimit() throws SQLException{
		String memb_id = member_id.getText();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String select =  "select count(*) from lendbook where member_id =?";
		//int count = 0;
		try {
			pst = con.prepareStatement(select);
			pst.setString(1, memb_id);
			rs = pst.executeQuery();
			if (rs.next()) {
                int count = rs.getInt(1);
                //System.out.println(count);
                return (count>=3);
            }
		}catch (SQLException e1) {
				
		}finally {
			pst.close();
			rs.close();
		}
		return false;
	}
	public void issueBookTableView() throws SQLException{
		issueBookViewData.clear();
		String select =  "select * from lendbook";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
			while(rs.next()) {
			
			  issueBookViewData.add(new IssueBook(issueBookViewData.size() + 1,rs.getInt("member_id"),rs.getString("member_fname"),rs.getString("book_isbn"),rs.getString("book_title"),rs.getDate("issuedate"),rs.getDate("returndate")));
			
			}
		}catch (SQLException e1) {
		
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
		
		//set cell value factory to tableview
		//NB.PROPERTYvalue factory must be the same with the one set in model class
		//columnId.setCellValueFactory(data -> data.getValue().idProperty());
		columnSerialNumber.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		columnMemberID.setCellValueFactory(data -> data.getValue().memberIdProperty());
		columnFName.setCellValueFactory(data -> data.getValue().memberFNameProperty());
		columnBookISBN.setCellValueFactory(data -> data.getValue().bookISBNProperty());
		columnBookTitle.setCellValueFactory(data -> data.getValue().bookTitleProperty());
		columnIssueDate.setCellValueFactory(data -> data.getValue().issuedateProperty());
		columnReturnDate.setCellValueFactory(data -> data.getValue().returndateProperty());
		
		issueBookView.setItems(null);
		issueBookView.setItems(issueBookViewData);
	}
	
	@FXML
	private void tablehandleButtonAction(MouseEvent event) {
		IssueBook selectedBook = issueBookView.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		member_id.setText(String.valueOf(selectedBook.getMemberId()));
		member_fname.setText(selectedBook.getMemberFName());
		book_isbn.setText(selectedBook.getBookISBN());
		book_title.setText(selectedBook.getBookTitle());
		issuedate.setValue(selectedBook.getIssueDate().toLocalDate());
		
		issueBookBtn.setDisable(true);
		member_id.setEditable(false);
		}
		//issueBookBtn.setDisable(true);
		//member_id.setEditable(false);
	}
			
	@FXML
	private void deleteEvent(ActionEvent event) throws SQLException{
		IssueBook selectedBook = issueBookView.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		delete();
		}else {
			AlertMaker.showWarningAlert("Warning","Please select  issued book in the table to remove.");
		}
		issueBookView.getSelectionModel().clearSelection();
		issueBookBtn.setDisable(false);
		member_id.setEditable(true);
		
		
	}
			
	void clear() {
		member_id.clear();
		member_fname.clear();
		book_isbn.clear();
		book_title.clear();
		issuedate.setValue(null);
		
		issueBookBtn.setDisable(false);
		member_id.setEditable(true);
	}
			
	public void delete() throws SQLException{
		PreparedStatement pst = null;
		String delete ="DELETE FROM lendbook where book_isbn =? and member_id =?";
		try {
			pst = con.prepareStatement(delete);
			pst.setString(1, book_isbn.getText());
			pst.setInt(2, Integer.parseInt(member_id.getText()));
			showConfirm(pst);
		}catch(SQLException ex) {
			
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}
			
	private void showConfirm(PreparedStatement pstIn) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText("Are you sure want to remove this book from issued book list?");
		alert.setContentText(null);
		//
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
			pstIn.executeUpdate();
			AlertMaker.showSimpleAlert("Success","Selected issued book deleted successfully!!.");
			clear();
			issueBookTableView();
			}catch (SQLException e1) {
				
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}
	}
			
	private void update() throws SQLException{
		PreparedStatement pst  = null;
		if(validateInput()) {
		String update = "UPDATE lendbook SET member_fname=?,book_isbn=?,book_title=?,issuedate=?,returndate=?,employee_id=? where member_id=?";
		try {
			pst = con.prepareStatement(update);
			pst.setString(1, member_fname.getText());
			pst.setString(2, book_isbn.getText());
			pst.setString(3, book_title.getText());
			pst.setDate(4, sqlDate);
			pst.setDate(5, sqlDate2);
			pst.setInt(6, employee_id);
			pst.setInt(7, Integer.parseInt(member_id.getText()));
			showConfirmation(pst);
			//pst.executeUpdate();
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
		alert.setHeaderText("Are you sure want to update this issued book details?");
		alert.setContentText(null);
		//
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
				
			pst.executeUpdate();
			AlertMaker.showSimpleAlert("Success","Issued book details updated successfully!!.");
			clear();
			issueBookTableView();
			}catch (SQLException e1) {
				
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}
	}
			
	@FXML 
	private void updateEvent(ActionEvent event) throws SQLException{
		IssueBook selectedBook = issueBookView.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		update();
		}else {
		
			AlertMaker.showWarningAlert("Warning","Please select issued book in the table to update");
		}
		issueBookView.getSelectionModel().clearSelection();
		issueBookBtn.setDisable(false);
		member_id.setEditable(true);
		
	}
			
	@FXML
	private void clearEvent(ActionEvent event) {
		clear();
		issueBookView.getSelectionModel().clearSelection();
		issueBookBtn.setDisable(false);
		member_id.setEditable(true);
	}
			
	@FXML
	private void issueBookExportAsPDFAction(ActionEvent event) {
		try {
			 
		    File file = new File("");
			Window stage = btnPdfExport.getScene().getWindow();
			fileChooser.setTitle("Save PDF File");
			FileChooser.ExtensionFilter extFilter
	        = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().add(extFilter);
			file = fileChooser.showSaveDialog(stage);
	
			if (file != null) {
				fileChooser.setInitialDirectory(file.getParentFile());
				FileOutputStream fos = new FileOutputStream(file);
				PDF pdf = new PDF(fos);
				Page page = new Page(pdf, A4.PORTRAIT);
	
				Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
				Font f2 = new Font(pdf, CoreFont.HELVETICA);
	
				Table tablePDF = new Table();
				List<List<Cell>> tableData = new ArrayList<List<Cell>>();
				List<Cell> tableRow = new ArrayList<Cell>();
	
				//Cell cell = new Cell(f1, columnId.getText());
				//tableRow.add(cell);
	
				Cell cell = new Cell(f1, columnSerialNumber.getText());
				tableRow.add(cell);
	
				cell = new Cell(f1, columnFName.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, columnBookISBN.getText());
				tableRow.add(cell);
	
				cell = new Cell(f1, columnBookTitle.getText());
				tableRow.add(cell);
	
				cell = new Cell(f1, columnIssueDate.getText());
				tableRow.add(cell);
				
				cell = new Cell(f1, columnReturnDate.getText());
				tableRow.add(cell);
	
				tableData.add(tableRow);
	
				List<IssueBook> items = issueBookView.getItems();
	
				for (IssueBook a : items) {
					//Cell id = new Cell(f2, String.valueOf(a.getId()));
					Cell serial_id = new Cell(f2,String.valueOf(a.getSerialNumber()));
					Cell name = new Cell(f2, a.getMemberFName());
					Cell isbn = new Cell(f2, a.getBookISBN());
					Cell title = new Cell(f2, a.getBookTitle());
					Cell issue_date =  new  Cell(f2, LibraryAppUtil.getDateString(a.getIssueDate()));
					Cell return_date = new Cell(f2, LibraryAppUtil.getDateString(a.getReturnDate()));
					//Cell return_date = new Cell(f2, SimpleDateFormat.(a.getReturnDate()));
					
					tableRow = new ArrayList<Cell>();
					//tableRow.add(id);
					tableRow.add(serial_id);
					tableRow.add(name);
					tableRow.add(isbn);
					tableRow.add(title);
					tableRow.add(issue_date);
					tableRow.add(return_date);
					//tableData.add(return_date);
					tableData.add(tableRow);
				}
				//tableData.add(tableRow);
				if (tableData.size() <= 1) {
					//function.message("Table is empty!");
					System.out.println("Table is empty!");
				} else {
					tablePDF.setData(tableData);
					tablePDF.setPosition(20f, 60f);
					tablePDF.setColumnWidth(0, 40f); // ID
					tablePDF.setColumnWidth(1, 65f); // member id
					tablePDF.setColumnWidth(2, 90f); // NAME
					tablePDF.setColumnWidth(3, 90f); // isbn
					tablePDF.setColumnWidth(4, 90f); // title
					tablePDF.setColumnWidth(5, 90f); // issuedate
					tablePDF.setColumnWidth(6, 90f); // returndate
	
					while (true) {
						tablePDF.drawOn(page);
						if (!tablePDF.hasMoreData()) {
							tablePDF.resetRenderedPagesCount();
							break;
						}
						page = new Page(pdf, A4.PORTRAIT);
					}
					AlertMaker.showSimpleAlert("Success","File saved to : " + file.getAbsolutePath() + " successfully." );
				}
	
				pdf.close();
				fos.close();
			} else {
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
			
	@FXML
	private void closeStage(ActionEvent event) {
	    getStage().close();
	}
	
	private Stage getStage() {
	    return (Stage) issueBookView.getScene().getWindow();
	}
}


