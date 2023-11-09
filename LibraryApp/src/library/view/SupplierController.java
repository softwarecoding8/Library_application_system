	package library.view;
	
	import java.io.IOException;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.Optional;
	import java.util.ResourceBundle;
	import java.util.regex.Pattern;
	
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.collections.transformation.FilteredList;
	import javafx.collections.transformation.SortedList;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.FXMLLoader;
	import javafx.fxml.Initializable;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.Button;
	import javafx.scene.control.ButtonType;
	import javafx.scene.control.Label;
	import javafx.scene.control.TableColumn;
	import javafx.scene.control.TableView;
	import javafx.scene.control.TextField;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.image.Image;
	import javafx.scene.input.MouseEvent;
	import javafx.scene.layout.AnchorPane;
	import javafx.stage.Modality;
	import javafx.stage.Stage;
	import javafx.stage.StageStyle;
	import library.Main;
	import library.DBConnection.DBHandler;
	import library.alert.AlertMaker;
	import library.model.Member;
	import library.model.Order;
	import library.model.Supplier;
	
	public class SupplierController implements Initializable {
		@FXML
		private TableView<Supplier> supplierTable;
		@FXML
		private TableColumn<Supplier,Number> serialNumberColumn;
		@FXML
		private TableColumn<Supplier,String> nameColumn;
		@FXML
		private TableColumn<Supplier,String> emailColumn;
		@FXML
		private TableColumn<Supplier,String> locationColumn;
		@FXML
		private TableColumn<Supplier,String> mobileNumberColumn;
	
		
		@FXML
		private TextField idField;
		@FXML
		private TextField nameField;
		@FXML
		private TextField mobileNoField;
		@FXML
		private TextField locationField;
		@FXML
		private TextField emailField,search;
		@FXML
		private Button update_button;
		@FXML
		private Button delete_button;
		@FXML
		private Button refresh_button;
		@FXML
		private Button add_button;
		
		private ObservableList<Supplier> list =FXCollections.observableArrayList(); 
		private DBHandler handler =  DBHandler.getInstance();
	    private final Connection con;
	    
	    private int employee_id;
		
	//The constructor is called before the initialize() method
	public SupplierController() {
		con = handler.getConnection();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			if(supplierTable!=null) {
				supplierTableView();
			}
		}catch(SQLException ex) {
			
		}
		add_button.setDisable(false);
		idField.setDisable(true);
		filterData();
	}
	
	public void setData(int employeeId) {
		this.employee_id = employeeId;
	}
	
	@FXML
	private void tablehandleButtonAction(MouseEvent event) {
		Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
		if(selectedSupplier!=null) {
		idField.setText(String.valueOf(selectedSupplier.getId()));
		nameField.setText(selectedSupplier.getName());
		mobileNoField.setText(selectedSupplier.getMobileNumber());
		locationField.setText(selectedSupplier.getLocation());
		//mField.setText(String.valueOf(selectedMember.getMobileNumber()));
		emailField.setText(selectedSupplier.getEmail());
		
		add_button.setDisable(true);
		}
	}
		
	public void delete() throws SQLException {
		PreparedStatement pst = null;
		String delete ="DELETE FROM suppliers where supplier_name = ?";
		try {
			pst = con.prepareStatement(delete);
			pst.setString(1, nameField.getText());
			//pst.executeUpdate();
			showConfirm(pst);
			
		}catch(SQLException ex) {
			//ex.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
		
	}
	
	private void showConfirm(PreparedStatement pst) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Book");
		alert.setHeaderText("Are you sure want to delete this supplier?");
		alert.setContentText(null);
		//
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
			pst.executeUpdate();
			AlertMaker.showSimpleAlert("Success","This " + nameField.getText() + " supplier deleted successfully!!.");
			clear();
			}catch (SQLException e1) {
				
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}
	}
	
	private void update() throws SQLException{
		PreparedStatement pst = null;
		String update = "UPDATE suppliers SET supplier_name=?,supplier_email=?,supplier_mobile_number=?,supplier_location=?,employee_id=? where supplier_id=?";
		try {
			pst = con.prepareStatement(update);
			pst.setString(1, nameField.getText());
			pst.setString(2, emailField.getText());
			pst.setString(3, mobileNoField.getText());
			pst.setString(4, locationField.getText());
			pst.setInt(5, employee_id);
			pst.setString(6, idField.getText());
			//pst.executeUpdate();
			showConfirmation(pst);
			
		}catch(SQLException ex) {
			//ex.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}
	
	private void showConfirmation(PreparedStatement pst) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Update");
		alert.setHeaderText("Are you sure want to update this supplier details?");
		alert.setContentText(null);
		//
		Optional<ButtonType>option = alert.showAndWait();
		if(option.get()==ButtonType.OK) {
			try {
				
			pst.executeUpdate();
			AlertMaker.showSimpleAlert("Success","This " + nameField.getText() + " supplier details updated successfully!!.");
			clear();
			
			}catch (SQLException e1) {
				
				AlertMaker.showErrorMessage("Error","Something went wrong.");
			}
		}else {
			clear();
		}
	}
	
	void clear() {
		idField.clear();
		nameField.clear();
		mobileNoField.clear();
		locationField.clear();
		//mobileNumberField.setText(null);
		emailField.clear();
		
	}
	
	@FXML 
	private void updateEvent(ActionEvent event) throws SQLException {
		Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
		if(selectedSupplier!=null) {
			update();
			supplierTableView();
		}else {
			//System.out.println("Warning error!");
			//Nothing selected
			AlertMaker.showWarningAlert("Warning","Please select  a supplier in the table to update");
		}
		supplierTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
	}
	
	@FXML
	private void deleteEvent(ActionEvent event) throws SQLException{
		Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
		if(selectedSupplier!=null) {
			delete();
			supplierTableView();
		}else {
			//System.out.println("Warning error!");
			//Nothing selected
			AlertMaker.showWarningAlert("Warning","Please select a supplier in the table to delete");
		}
		supplierTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
	}
	
	@FXML
	private void clearEvent(ActionEvent event) {
		clear();
		supplierTable.getSelectionModel().clearSelection();
		add_button.setDisable(false);
	}
	
	public ObservableList<Supplier> getSupplier() throws SQLException{
		PreparedStatement pst = null;
		ResultSet rs = null;
		String select = "select * from suppliers";
		try {
			pst = con.prepareStatement(select);
			rs = pst.executeQuery();
			while(rs.next()) {
				list.add(new Supplier(list.size() + 1,rs.getInt("supplier_Id"),rs.getString("supplier_name"),rs.getString("supplier_email"),rs.getString("supplier_mobile_number"),rs.getString("supplier_location")));
			}
		}catch(SQLException ex) {
			//ex.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
			rs.close();
		}
		return list;
	}
	public void supplierTableView() throws SQLException{
		list.clear();
		ObservableList<Supplier> list =getSupplier();
		serialNumberColumn.setCellValueFactory(data -> data.getValue().serialNumberProperty());
		nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
		emailColumn.setCellValueFactory(data -> data.getValue().emailProperty());;
		mobileNumberColumn.setCellValueFactory(data -> data.getValue().mobileNumberProperty());
		locationColumn.setCellValueFactory(data -> data.getValue().locationProperty());
	
		supplierTable.setItems(null);
		supplierTable.setItems(list);
	}
	@FXML
	private void handleAddSupplierAction(ActionEvent event) throws SQLException{
		PreparedStatement pst = null;
		if(validateInput()) {
		String insert = "INSERT INTO suppliers(supplier_name,supplier_email,supplier_mobile_number,supplier_location,employee_id) VALUES(?,?,?,?,?)";
		try {
			pst = con.prepareStatement(insert);
			pst.setString(1,nameField.getText());
			pst.setString(2,emailField.getText());
			pst.setString(3,mobileNoField.getText());
			pst.setString(4,locationField.getText());
			pst.setInt(5,employee_id);
			pst.executeUpdate();
			
			AlertMaker.showSimpleAlert("Success","This " + nameField.getText() + " supplier added successfully!!.");
			clear();
			supplierTableView();
			
		} catch (SQLException e1) {
			//e1.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}finally {
			pst.close();
		}
	}
	}
	
	private boolean validateInput() {
	    try {
	    	String errorMessage = "";
	        if((nameField.getText().isEmpty()|| mobileNoField.getText().isEmpty() || locationField.getText().isEmpty() || emailField.getText().isEmpty() )){
	        	errorMessage += "Please fill blank fields!!\n";
	          
	        } 
	        if(!((nameField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid name format!!\n";
	           
	        }
	        if(!((locationField.getText()).matches("[a-zA-Z]+"))){
	        	errorMessage += "invalid name format!!\n";
	        }
	       
	        
	        if(!(mobileNoField.getText().matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))){
	        	errorMessage += "invalid mobile number!!\n";
	        }
	        if(!(isValid(emailField.getText().toLowerCase()))){
	        	errorMessage += "invalid email!!\n";
	        }
	        
	        if(isEmail(emailField.getText().toLowerCase())){
				   errorMessage += "email already present!!\n";
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
	  }return true;
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
		    String query="select * from suppliers where supplier_email=?";
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
		        AlertMaker.showErrorMessage("Error","Something went wrong.");
		        return false;
		    }finally{
		        preparedStatement.close();
		        resultSet.close();
		    }
		}
		
		private void filterData() {
	
		    FilteredList<Supplier> searchedData = new FilteredList<>(list, e -> true);
		    search.setOnKeyReleased(e -> {
		        search.textProperty().addListener((observable, oldValue, newValue) -> {
		            searchedData.setPredicate(supplier -> {
		                if (newValue == null || newValue.isEmpty()) {
		                    return true;
		                }
		                String lowerCaseFilter = newValue.toLowerCase();
		                if (supplier.getName().toLowerCase().contains(lowerCaseFilter)) {
		                    return true;
		                } else if (supplier.getLocation().toLowerCase().contains(lowerCaseFilter)) {
		                    return true;
		                }
		                return false;
		            });
		        });
	
		        SortedList<Supplier> sortedData = new SortedList<>(searchedData);
		        sortedData.comparatorProperty().bind(supplierTable.comparatorProperty());
		        supplierTable.setItems(sortedData);
		    });
		}
	}
	
