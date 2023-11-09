package library.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

//import com.rafsan.inventory.controller.employee.EditController;
//import com.rafsan.inventory.entity.Employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import library.DBConnection.DBHandler;
import library.alert.AlertMaker;
import library.model.Book;
import library.model.Member;
import library.model.Order;
import library.util.LibraryAppUtil;
import library.view.IssueBookController.IssueBook;

public class BookOverviewController implements Initializable {
	@FXML
	private TableView<Book> bookTable;
	@FXML
	private TextField isbnField,titleField,authorField,editionField,publisherField,priceField,qtyField,searchField;
	@FXML
	private TableColumn<Book,Number> serialNumberColumn,qtyColumn;
	@FXML
	private TableColumn<Book,String> isbnColumn,titleColumn,authorColumn,editionColumn,publisherColumn,categoryColumn,bookNoColumn;
	@FXML
	private TableColumn<Book,Number> priceColumn;
	
	private DBHandler handler =  DBHandler.getInstance();
    private final Connection con;
    private int employee_id;
	
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Button addButton;
    @FXML
    private Button exportBtn;
    
    private FileChooser fileChooser = new FileChooser();
    
    ObservableList<Book> list =FXCollections.observableArrayList();

	//The constructor is called before the initialize() method
	public BookOverviewController() {
		con = handler.getConnection();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(bookTable!=null) {
			bookTableView();
		}
		addButton.setDisable(false);
		isbnField.setDisable(false);
		filterData();

	}
	
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
	
	public ObservableList<Book> getBook() throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		String select = "select * from books";
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
			while(rs.next()) {
					list.add(new Book(list.size() + 1,rs.getString("book_isbn"),rs.getString("book_title"),rs.getString("author"),rs.getString("edition"),rs.getString("publisher"),rs.getFloat("price"),rs.getInt("stock")));
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
		return list;
	}
	
	public void bookTableView() {
		list.clear();
		ObservableList<Book> list = null;
		try {
			list = getBook();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		serialNumberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		isbnColumn.setCellValueFactory(data -> data.getValue().isbnProperty());
		titleColumn.setCellValueFactory(data -> data.getValue().titleProperty());;
		authorColumn.setCellValueFactory(data -> data.getValue().authorProperty());
		editionColumn.setCellValueFactory(data -> data.getValue().editionProperty());
		publisherColumn.setCellValueFactory(data -> data.getValue().publisherProperty());
		priceColumn.setCellValueFactory(data -> data.getValue().priceProperty());
		qtyColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
		
		bookTable.setItems(null);
		bookTable.setItems(list);
	}
	@FXML
	private void addAction(ActionEvent event) throws SQLException{
		PreparedStatement pst = null;
		if(validateInput()) {
		String insert = "INSERT INTO books(book_isbn,book_title,author,edition,publisher,price,stock,employee_id) VALUES(?,?,?,?,?,?,?,?)";
		try {
			pst = con.prepareStatement(insert);
			pst.setString(1,isbnField.getText());
			pst.setString(2,titleField.getText());
			pst.setString(3,authorField.getText());
			pst.setString(4,editionField.getText());
			pst.setString(5,publisherField.getText());
			pst.setFloat(6,Float.parseFloat((priceField.getText())));
			pst.setInt(7,Integer.parseInt((qtyField.getText())));
			pst.setInt(8,employee_id);
			pst.executeUpdate();
			
			AlertMaker.showSimpleAlert("Success"," New book " + titleField.getText() + " added successfully!!.");
			clear();
			bookTableView();
			
		} catch (SQLException e1) {
			
			 AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	 }
	}
	
	void clear() {
		isbnField.clear();
		titleField.clear();
		authorField.clear();
		editionField.clear();
		publisherField.clear();
		priceField.clear();
		qtyField.clear();
		
		isbnField.requestFocus();
		
	}
	
	private boolean validateInput() {
	try {
        String errorMessage = "";

        if (isbnField.getText() == null || isbnField.getText().length() == 0) {
            errorMessage += "No valid ISBN!\n";
        }
        if(isISBN(isbnField.getText())){
			   errorMessage += "cannot add this book ISBN "+ isbnField.getText() +" because already present!!\n";
		 }
        if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }

        if (authorField.getText() == null || authorField.getText().length() == 0) {
            errorMessage += "No valid author!\n";
        }

        if (editionField.getText() == null || editionField.getText().length() == 0) {
            errorMessage += "No valid edition!\n";
        }

        if (publisherField.getText() == null || publisherField.getText().length() == 0) {
            errorMessage += "No valid email!\n";
        }
        
        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "No valid price!\n";
        }

        if (qtyField.getText() == null || qtyField.getText().length() == 0) {
            errorMessage += "No valid stock!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
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
	
	public  boolean isISBN(String isbnIn) throws SQLException{
	    PreparedStatement preparedStatement = null ;
	    ResultSet resultSet =null;
	    String query="select * from books where book_isbn=?";
	    try{
	        preparedStatement =con.prepareStatement(query);
	        preparedStatement.setString(1,isbnIn);
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
	
	private boolean validateInputUpdate() {
		String errorMessage = "";

        if (isbnField.getText() == null || isbnField.getText().length() == 0) {
            errorMessage += "No valid ISBN!\n";
        }
       if (titleField.getText() == null || titleField.getText().length() == 0) {
            errorMessage += "No valid title!\n";
        }

        if (authorField.getText() == null || authorField.getText().length() == 0) {
            errorMessage += "No valid author!\n";
        }

        if (editionField.getText() == null || editionField.getText().length() == 0) {
            errorMessage += "No valid edition!\n";
        }

        if (publisherField.getText() == null || publisherField.getText().length() == 0) {
            errorMessage += "No valid email!\n";
        }
        
        if (priceField.getText() == null || priceField.getText().length() == 0) {
            errorMessage += "No valid price!\n";
        }

        if (qtyField.getText() == null || qtyField.getText().length() == 0) {
            errorMessage += "No valid stock!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
	
    }
	
	@FXML
	private void tablehandleButtonAction(MouseEvent event) {
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		isbnField.setText(selectedBook.getISBN());
		titleField.setText(selectedBook.getTitle());
		authorField.setText(selectedBook.getAuthor());
		editionField.setText(selectedBook.getEdition());
		publisherField.setText(selectedBook.getPublisher());
		priceField.setText( String.valueOf(selectedBook.getPrice()));
		qtyField.setText( String.valueOf(selectedBook.getQuantity()));
		
		addButton.setDisable(true);
		isbnField.setDisable(true);
		
		}
	}
	
   	@FXML 
	private void updateAction(ActionEvent event) throws SQLException {
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		update();
		}else {
			AlertMaker.showWarningAlert("Warning","Please select  book in the table to update");
		}
		bookTable.getSelectionModel().clearSelection();
		addButton.setDisable(false);
		isbnField.setDisable(false);
	}
	
	private void update() throws SQLException {
		PreparedStatement pst = null;
		if(validateInputUpdate()) {
		String update = "UPDATE books SET book_title=?,author=?,edition=?,publisher=?,price=?,stock=?, employee_id=? where book_isbn=?";
		try {
			pst = con.prepareStatement(update);
			pst.setString(1, titleField.getText());
			pst.setString(2, authorField.getText());
			pst.setString(3, editionField.getText());
			pst.setString(4, publisherField.getText());
			pst.setFloat(5,  Float.parseFloat(priceField.getText()));
			pst.setInt(6, Integer.parseInt(qtyField.getText()));
			pst.setInt(7, employee_id);
			pst.setString(8, isbnField.getText());
			
			showConfirmation(pst);
			bookTableView();
			
		}catch(SQLException ex) {
			
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	
	   }
	}
    

	@FXML
	private void deleteAction(ActionEvent event) throws SQLException{
		Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
		if(selectedBook!=null) {
		delete();
		}else {
			
			AlertMaker.showWarningAlert("Warning","Please select  book in the table to delete");
		}
		bookTable.getSelectionModel().clearSelection();
		addButton.setDisable(false);
		isbnField.setDisable(false);
	}
	
	public void delete() throws SQLException {
		PreparedStatement pst = null;
		String delete ="DELETE FROM books where book_isbn = ?";
		try {
			pst = con.prepareStatement(delete);
			pst.setString(1, isbnField.getText());
			showConfirm(pst);
			bookTableView();
		}catch(SQLException ex) {
			
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}

    
    private void showConfirmation(PreparedStatement pst) {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Update Book");
    	alert.setHeaderText("Are you sure want to update this book?");
    	alert.setContentText(null);
    	//
    	Optional<ButtonType>option = alert.showAndWait();
    	if(option.get()==ButtonType.OK) {
    		try {
    			
    		pst.executeUpdate();
    		AlertMaker.showSimpleAlert("Success"," Book updated successfully!!.");
    		clear();
    		}catch (SQLException e1) {
    			
    			AlertMaker.showErrorMessage("Error","Something went wrong.");
    		}
    	}
    }
    
    private void showConfirm(PreparedStatement pst) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Delete Book");
    	alert.setHeaderText("Are you sure want to delete this book?");
    	alert.setContentText(null);
    	//
    	Optional<ButtonType>option = alert.showAndWait();
    	if(option.get()==ButtonType.OK) {
    		try {
    		pst.executeUpdate();
    		AlertMaker.showSimpleAlert("Success","This " + titleField.getText() + " book deleted successfully!!.");
    		clear();
    		}catch (SQLException e1) {
    			AlertMaker.showErrorMessage("Error","Something went wrong.");
    		}
    	}
    }
    
    @FXML 
	private void clearAction(ActionEvent event) {
    	clear();
    	bookTable.getSelectionModel().clearSelection();
		addButton.setDisable(false);
		isbnField.setDisable(false);
    }
    
    private void filterData() {

        FilteredList<Book> searchedData = new FilteredList<>(list, e -> true);
        searchField.setOnKeyReleased(e -> {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                searchedData.setPredicate(book -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });
            });

            SortedList<Book> sortedData = new SortedList<>(searchedData);
            sortedData.comparatorProperty().bind(bookTable.comparatorProperty());
            bookTable.setItems(sortedData);
        });
    }
    
    
    @FXML 
	public void exportAsPDFAction(ActionEvent event) throws FileNotFoundException, DocumentException {
    	File file = new File("");
		Window stage = exportBtn.getScene().getWindow();
		fileChooser.setTitle("Save PDF File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);
		file = fileChooser.showSaveDialog(stage);

		if (file != null) {
			// create document
	        Document document = new Document(PageSize.A4, 36, 36, 90, 50);
	       // Document document = new Document(PageSize.A4, 20, 20, 20, 20);
	        //Document document = new Document();
	        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

	        
	        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN,8,Font.BOLD);
	        Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,8);
			//Font f2 = new Font(pdf, CoreFont.HELVETICA);
	        
	        PdfPTable table = new PdfPTable(8);
	        
	        PdfPCell table_cell = new PdfPCell(new Phrase(serialNumberColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(isbnColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(titleColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(authorColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(editionColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(publisherColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(priceColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table_cell = new PdfPCell(new Phrase(qtyColumn.getText(),boldFont));
	        table_cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(table_cell);
	        
	        table.setHeaderRows(1);
	        
	        List<Book> items = bookTable.getItems();
	        //PdfPCell table_cell;
	        for (Book a : items) {
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getSerialNumber()),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(a.getISBN(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase( a.getTitle(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(a.getAuthor(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase( a.getEdition(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(a.getPublisher(),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getPrice()),f1));
				table.addCell(table_cell);
				
				table_cell = new PdfPCell(new Phrase(String.valueOf(a.getQuantity()),f1));
				table.addCell(table_cell);
				
			}
	      
	        
	        // add header and footer
	        HeaderFooterBookPageEvent evt = new HeaderFooterBookPageEvent();
	        writer.setPageEvent(evt);

	        // write to document
	        document.open();
	        //document.addTitle("test");
	        //document.add(new Paragraph("Adding a header to PDF Document using iText."));
	        document.add(table);
	        //document.add(new Paragraph("Adding a footer to PDF Document using iText."));
	        document.close();
			
		}
    	
    }
    	
}

