	package library.view;
	
	import java.io.File;
	import java.net.URL;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Iterator;
	import java.util.List;
	import java.util.ResourceBundle;
	
	import javax.swing.DefaultListModel;
	import javax.swing.JOptionPane;
	import javax.swing.ListSelectionModel;
	
	import library.DBConnection.DBHandler;
	import library.alert.AlertMaker;
	import javafx.collections.FXCollections;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.geometry.Orientation;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.FocusModel;
	import javafx.scene.control.Label;
	import javafx.scene.control.ListView;
	import javafx.scene.control.SelectionMode;
	import javafx.scene.control.SelectionModel;
	import javafx.scene.control.TextField;
	import javafx.scene.layout.Pane;
	import javafx.scene.web.HTMLEditor;
	import javafx.stage.FileChooser;
	import library.DBConnection.DataHelper;
	import library.model.MailServerInfo;
	import library.util.EmailUtil;
	import javafx.collections.ObservableList;
	import javafx.event.ActionEvent;
	
	public class SendOrderListController implements Initializable{
	
	    private List<File> attachments = new ArrayList<File>();
	    @FXML
	    private HTMLEditor htmlEditor;
	    @FXML
	    private TextField subjectTextField;
	    @FXML
	    private ListView<String> list1;
	    String attachment_path1 = null;
	    String attachment_path2 = null;
	    
	    List a1 = new ArrayList();
	    List a2 = new ArrayList();
	    
	    @FXML
		private ComboBox<String> supplierComboBox;
		
		private ObservableList<String> emailData = FXCollections.observableArrayList();
	    private final MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
	    final static ObservableList<String> str = FXCollections.observableArrayList();
	    
	    private DBHandler handler =  DBHandler.getInstance();
	    private final Connection con;
	    
	public SendOrderListController() {
		con = handler.getConnection();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
	   PreparedStatement pst = null;
	   ResultSet rs = null;
		try {
			pst = con.prepareStatement("select supplier_email from suppliers");
			rs = pst.executeQuery();
			emailData.clear();
			while(rs.next()) {
				emailData.add(rs.getString(1));
			}
		
		}catch(SQLException s) {
			//s.printStackTrace();
			AlertMaker.showErrorMessage("Error","Something went wrong.");
		}
		supplierComboBox.setItems(emailData);
	}
	    
	@FXML
	void attachBtnAction() {
	   try {     
	        FileChooser fileChooser = new FileChooser();
	        File selectedFile = fileChooser.showOpenDialog(null);
	        attachment_path1 = selectedFile.getAbsolutePath();
	        attachment_path2 = selectedFile.getName();
	        JOptionPane.showMessageDialog(null,"Attached");
	    
	     for(int i=0;i<1;i++) {
	    	a1.add(attachment_path1.toString());
	     }
	    
	     for(int i=0;i<1;i++) {
	    	a2.add(attachment_path2.toString());
	     }
	    
	    String[] stringarr = new String[a2.size()];
	    stringarr =(String[])a2.toArray(stringarr);
	    
	    list1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	  
	    for(int j=0;j<a1.size();j++) {
	    	str.add(stringarr[j]);
		}
	    
	    for (int i=0; i<str.size();i++) {
	    	for(int j=i+1; j<str.size();j++) {
	    		if(str.get(i).equals(str.get(j))) {
	    			str.remove(j);
	    			i=0;
	    		}
	    	}
	    }
	    
	    list1.setItems(str);
	   
	    }catch(Exception e) {
	        AlertMaker.showErrorMessage("Error","Something went wrong.");
	    }
	}
	
	@FXML
	void deleteBtnAction() {
	    	try {
	    	list1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	
	    		int count = list1.getSelectionModel().getSelectedIndices().size();
	    		System.out.println("Selected items : " + count);
	    		for(int i=0;i<count;i++) {
	    			a1.remove(i);
	    			a2.remove(i);
	    			str.remove(i);
	    		}
	    		list1.getSelectionModel().clearSelection();
	    	}catch(Exception e) {}
	}
	
	@FXML
	 void sendButtonAction() {
		if((supplierComboBox.getValue()!=null) && (htmlEditor.getHtmlText()!="") && (subjectTextField.getText()!="")) {
			EmailUtil.sendAttachMail(mailServerInfo, supplierComboBox.getValue(), htmlEditor.getHtmlText(), subjectTextField.getText(), attachment_path1,a1,a2);
		    clearContent();
		}else {
			 AlertMaker.showErrorMessage("Error","Please fill blank fields.");
		}
	}
	    

	void clearContent() {
		supplierComboBox.setValue(null);
		htmlEditor.setHtmlText("");
		subjectTextField.setText(null);
		a1.clear();
		a2.clear();
		list1.getItems().clear();
	}
	
	@FXML
	private void clearEvent(ActionEvent event) {
		clearContent();
	}
}