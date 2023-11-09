	package library.view;
	
	import library.DBConnection.DBHandler;
	import library.alert.AlertMaker;
	import library.model.Member;
	import library.model.Order;
	import library.util.LibraryAppUtil;
	import library.view.IssueBookController.IssueBook;
	
	import java.io.BufferedOutputStream;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.List;
	import java.util.Map;
	import java.util.Optional;
	import java.util.ResourceBundle;
	import static java.util.Objects.requireNonNull;
	
	import com.pdfjet.A4;
	import com.pdfjet.Cell;
	import com.pdfjet.CoreFont;
	import com.pdfjet.PDF;
	import com.pdfjet.Page;
	import com.pdfjet.Table;
	
	import com.itextpdf.text.Document;
	import com.itextpdf.text.DocumentException;
	import com.itextpdf.text.Element;
	import com.itextpdf.text.Paragraph;
	import com.itextpdf.text.Phrase;
	import com.itextpdf.text.pdf.PdfPCell;
	import com.itextpdf.text.pdf.PdfPTable;
	import com.itextpdf.text.pdf.PdfWriter;
	import com.itextpdf.text.Font;
	import com.itextpdf.text.PageSize;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Button;
	import javafx.scene.control.ButtonType;
	import javafx.scene.control.Label;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.TextField;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.input.KeyCode;
	import javafx.scene.input.KeyEvent;
	import javafx.stage.FileChooser;
	import javafx.stage.Window;
	
	import javafx.scene.input.MouseEvent;
	import javafx.scene.text.Text;
	
	public class OrderController implements Initializable{
		@FXML
	 	private TextField isbn,title,quantity,subtotal,price;
	 	@FXML
	 	private Text total;
	 	private int serial_number;
	 	@FXML
		private TableView<Order> orderTable;
		@FXML
		private TableColumn<Order,Number> serial_numberColumn;
		@FXML
		private TableColumn<Order,String> isbnColumn;
		@FXML
		private TableColumn<Order,String> titleColumn;
		@FXML
		private TableColumn<Order,Number> priceColumn;
		@FXML
		private TableColumn<Order,Number> quantityColumn;
		@FXML
		private TableColumn<Order,Number> subtotalColumn;
		
		@FXML
		private Button btnPdfExport;
		private FileChooser fileChooser = new FileChooser();
		
		private Connection con;
		private DBHandler handler =  DBHandler.getInstance();
		
		
		Map<String,Order> orderll;
		private ObservableList<Order> orderData = FXCollections.observableArrayList();
		
	//The constructor is called before the initialize() method
	public OrderController() {
		con = handler.getConnection();
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
	}
		
	@FXML
	private void getDetails(MouseEvent event) throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		String bk = isbn.getText();
		String select = "select book_title,price from books where book_isbn=?";
		
		try {
			pst = con.prepareStatement(select);
			pst.setString(1, bk);
			rs = pst.executeQuery();
			
			if(rs.next() == false) {
				if(bk!="") {
					AlertMaker.showErrorMessage("Error","This book ISBN "+isbn.getText()+ " is not in our library.");
				}
			}else {
				String titlebk = rs.getString("book_title");
				String pricebk = rs.getString("price");
				title.setText(titlebk.trim());
				price.setText(pricebk.trim());
			}
		}catch (SQLException e1) {
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
	}
	
	@FXML
	private void calculateSubtotal(MouseEvent event) throws IOException{
		if((quantity.getText()!="")) {
			int qty = Integer.parseInt(quantity.getText());
			float pr = Float.parseFloat(price.getText());
			float sub = qty * pr;
			subtotal.setText(String.valueOf(sub));
		}
	}
	
	public void addToOrderListAction(ActionEvent event) {
	if(validateInput()) {
		if(add(orderData.size() + 1,isbn.getText(),title.getText(),Float.parseFloat(price.getText()),Integer.parseInt(quantity.getText()),Float.parseFloat(subtotal.getText()))){
			
			AlertMaker.showSimpleAlert("Success","Item added  successfully!!.");
			
		}else {
			
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
	}
		orderTableView();
		calculateTotal();
		clear();
	}
	
	public void clearAction(ActionEvent event) {
		clear();
	}
	
	public ObservableList<Order> getOrderData(){
			return orderData;
	}
		
	public void orderTableView() {
			ObservableList<Order> orderData = getOrderData();
			serial_numberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
			isbnColumn.setCellValueFactory(data -> data.getValue().bookISBNProperty());
			titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());;
			priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
			quantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
			subtotalColumn.setCellValueFactory(data -> data.getValue().subTotalProperty());
			
			orderTable.setItems(null);
			orderTable.setItems(orderData);
	}
		
	public void calculateTotal(){
		float tot =0;
		int i;
		for(i=0;i<orderData.size();i++) {
			tot += orderData.get(i).getSubTotal();
		}
		total.setText(String.valueOf(tot));
	}
		
		
	private boolean add(int sizeIn,String isbnIn,String titleIn,Float priceIn,int quantityIn,Float subtotalIn) {
		Order n = new Order(sizeIn,isbnIn,titleIn,priceIn,quantityIn,subtotalIn);
		for(int i=0;i<orderData.size();i++) {
			if(orderData.get(i).getBookISBN().equals(isbnIn)) {
				int quan =	 orderData.get(i).getQuantity();
				orderData.get(i).setQuantity(quan + quantityIn);
				float sub = orderData.get(i).getSubTotal();
				orderData.get(i).setSubTotal(sub + subtotalIn);
				
				return true;
			}	
		}
		
		orderData.add(n);
		return true;
	}
	
		
	@FXML
	private void tablehandleButtonAction(MouseEvent event) {
		Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
		if(selectedOrder!=null) {
		isbn.setText(selectedOrder.getBookISBN());
		title.setText(selectedOrder.getTitle());
		quantity.setText(String.valueOf(selectedOrder.getQuantity()));
		price.setText(String.valueOf(selectedOrder.getPrice()));
		subtotal.setText(String.valueOf(selectedOrder.getSubTotal()));
		}
	}
		
	@FXML
	private void deleteEvent(ActionEvent event) {
		Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
		if(selectedOrder!=null) {
			
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm");
		alert.setHeaderText("Are you sure want to remove this item?");
		alert.setContentText(null);
		//
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
				orderData.remove(selectedOrder);;
				clear();
				AlertMaker.showSimpleAlert("Success","Item remove  successfully!!.");
			}catch (Exception e1) {
				
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}
			
		}else {
			//Nothing selected
			AlertMaker.showWarningAlert("Warning","Please select an item in the table to remove.");
		}
		orderTable.getSelectionModel().clearSelection();
	}
		
	void clear() {
		isbn.clear();
		title.clear();
		price.clear();
		quantity.clear();
		subtotal.clear();
		
		isbn.requestFocus();
		
	}
		
	@FXML 
	private void updateEvent(ActionEvent event) {
		Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
		if(selectedOrder!=null) {
		//update();
			Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("Confirm");
	    	alert.setHeaderText("Are you sure want to update this item?");
	    	alert.setContentText(null);
	    	//
	    	Optional<ButtonType>option = alert.showAndWait();
	    	if(option.get()==ButtonType.OK) {
	    		try {
	    			if(validateInput()) {
	    				selectedOrder.setBookISBN(isbn.getText());
	    				selectedOrder.setTitle(title.getText());
	    				selectedOrder.setPrice(Float.parseFloat(price.getText()));
	    				selectedOrder.setQuantity(Integer.parseInt(quantity.getText()));
	    				selectedOrder.setSubTotal(Float.parseFloat(subtotal.getText()));
	    				}
	    			clear();
	    	    	AlertMaker.showSimpleAlert("Success","Item details updated  successfully!!.");
	    		}catch (Exception e1) {
	    			
	    			AlertMaker.showErrorMessage("Error","Something went wrong.");
	    		}
	    	}	
			
	    	
		}else {
			//System.out.println("Warning error!");
			//Nothing selected
			AlertMaker.showWarningAlert("Warning","Please select an item in the table to update.");			
		}
		orderTable.getSelectionModel().clearSelection();
	}
		
	private boolean validateInput() {
		try {
	    	String errorMessage = "";
	        if((isbn.getText().isEmpty()|| title.getText().isEmpty() || price.getText().isEmpty() || quantity.getText().isEmpty() || subtotal.getText().isEmpty() )){
	        	errorMessage += "Please fill blank fields!!\n"; 
	        }
	        
	        if(!quantity.getText().matches("\\d*")) {
	        	errorMessage += "Please enter valid quantity value!!!\n";
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
		 } catch (Exception ex) {
	            
	       }
		return true;
	}
		
	//orderTableView();
	public void generateOrderListInPDFAction(ActionEvent event) throws FileNotFoundException, DocumentException {
		if(orderData!=null) {
	       File file = new File("");
			Window stage = btnPdfExport.getScene().getWindow();
			fileChooser.setTitle("Save PDF File");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().add(extFilter);
			file = fileChooser.showSaveDialog(stage);
	
			if (file != null) {
				// create document
		        Document document = new Document(PageSize.A4, 36, 36, 90, 50);
		        //Document document = new Document();
		        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
	
		        
		        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
				//Font f2 = new Font(pdf, CoreFont.HELVETICA);
		        
		        PdfPTable table = new PdfPTable(6);
		        
		        PdfPCell table_cell = new PdfPCell(new Phrase(serial_numberColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        
		        table_cell = new PdfPCell(new Phrase(isbnColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        
		        table_cell = new PdfPCell(new Phrase(titleColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        
		        table_cell = new PdfPCell(new Phrase(priceColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        
		        table_cell = new PdfPCell(new Phrase(quantityColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        
		        table_cell = new PdfPCell(new Phrase(subtotalColumn.getText(),boldFont));
		        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(table_cell);
		        table.setHeaderRows(1);
		        
		        List<Order> items = orderTable.getItems();
		        //PdfPCell table_cell;
		        for (Order a : items) {
					
					table_cell = new PdfPCell(new Phrase(String.valueOf(a.getSerialNumber())));
					table.addCell(table_cell);
					
					table_cell = new PdfPCell(new Phrase(a.getBookISBN()));
					table.addCell(table_cell);
					
					table_cell = new PdfPCell(new Phrase( a.getTitle()));
					table.addCell(table_cell);
					
					table_cell = new PdfPCell(new Phrase(String.valueOf(a.getPrice())));
					table.addCell(table_cell);
					
					table_cell = new PdfPCell(new Phrase(String.valueOf(a.getQuantity())));
					table.addCell(table_cell);
					
					table_cell = new PdfPCell(new Phrase(String.valueOf(a.getSubTotal())));
					table.addCell(table_cell);
					
				}
		       PdfPCell cell = new PdfPCell(new Phrase("Total(Ksh)",boldFont));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setColspan(5);
		        
		        PdfPCell cell3 = new PdfPCell(new Phrase(total.getText(),boldFont));
		        cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
		        //cell3.setColspan(2);
		        
		        //cell.setRowspan(1);
		        table.addCell(cell);
		        table.addCell(cell3);
		        table.completeRow();
		        
		        // add header and footer
		        HeaderFooterOrderPageEvent evt = new HeaderFooterOrderPageEvent();
		        writer.setPageEvent(evt);
	
		        // write to document
		        document.open();
		        document.add(table);
		        document.close();
				
				//clear order table content
			        orderTable.setItems(null);
			        total.setText("");
				}
		}
	}
		
	}
	
	
	
	
	
	
	
	
	
	
