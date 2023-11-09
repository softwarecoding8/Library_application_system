package library.view;

import com.google.common.collect.ImmutableList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import library.alert.AlertMaker;
import library.model.MailServerInfo;
import library.DBConnection.DataHelper;
import library.DBConnection.DBHandler;
import library.view.EmailSenderController;
import library.util.Preferences;
import library.util.LibraryAppUtil;
import library.model.NotificationItem;

public class OverdueNotificationController implements Initializable {

    private ObservableList<NotificationItem> list = FXCollections.observableArrayList();
    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<NotificationItem> tableview;
    @FXML
    private TableColumn<NotificationItem, JFXCheckBox> colNotify;
    @FXML
    private TableColumn<NotificationItem, String> colMemID;
    @FXML
    private TableColumn<NotificationItem, String> colMemberFName;
    @FXML
    private TableColumn<NotificationItem, String> colEmail;
    @FXML
    private TableColumn<NotificationItem, String> colBookTitle;
    @FXML
    private TableColumn<NotificationItem, Integer> colDays;
    @FXML
    private TableColumn<NotificationItem, Float> colFineAmount;
    @FXML
    private AnchorPane contentPane;
    
    
    private DBHandler handler =  DBHandler.getInstance();
    private Connection con;
   
    public OverdueNotificationController() {
    	con = handler.getConnection();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	initialize();
        try{
        	loadData();
        }catch(SQLException ex) {
        	
        }
   }

    private void initialize() {
        colNotify.setCellValueFactory(new NotificationControlCellValueFactory());
        colMemID.setCellValueFactory(new PropertyValueFactory<>("memberID"));
        colMemberFName.setCellValueFactory(new PropertyValueFactory<>("memberFName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("memberEmail"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colDays.setCellValueFactory(new PropertyValueFactory<>("dayCount"));
        colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
        tableview.setItems(list);
    }

    private void loadData()throws SQLException {
    	list.clear();
    	Preferences pref = Preferences.getPreferences();
        Long overdueBegin = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(pref.getDaysWithNoFine());

        String select = "select l.book_isbn,l.member_id,l.issuedate,m.fname,m.member_id,m.email,b.book_title from lendbook l JOIN members m ON l.member_id = m.member_id JOIN books b ON l.book_isbn=b.book_isbn WHERE l.issuedate < ?;";
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = con.prepareStatement(select);
            statement.setTimestamp(1, new Timestamp(overdueBegin));
            rs = statement.executeQuery();
            int counter = 0;
            while (rs.next()) {
                counter += 1;
                String memberName = rs.getString("m.fname");
                String memberID = rs.getString("m.member_id");
                String email = rs.getString("m.email");
                String bookISBN = rs.getString("l.book_isbn");
                String bookTitle = rs.getString("b.book_title");
                Timestamp issueTime = rs.getTimestamp("l.issuedate");
                Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - issueTime.getTime())) + 1;
                Integer fineDays = days - (pref.getDaysWithNoFine());
                Float fine = LibraryAppUtil.getFineAmount(days);
                NotificationItem item = new NotificationItem(true, memberID, memberName, email, bookTitle, LibraryAppUtil.getDateString(issueTime), fineDays, fine);
                list.add(item);
               
            }
        } catch (SQLException ex) {
           AlertMaker.showWarningAlert("Warning","Something went wrong");
            
        }finally {
        	statement.close();
        	rs.close();
        }
       
    }
    


    @FXML
    private void handleSendNotificationAction(ActionEvent event) {
        if(checkForMailServerConfig()) {
        List<NotificationItem> selectedItems = list.stream().filter(item -> item.getNotify()).collect(Collectors.toList());
	        if (selectedItems.isEmpty()) {
	        	JOptionPane.showMessageDialog(null,"Nothing selected to notify");
	            return;
	        }
	        Object controller = LibraryAppUtil.loadWindow(getClass().getResource("/library/view/email_sender.fxml"), "Notify Overdue", null);
	        if (controller != null) {
	            EmailSenderController cont = (EmailSenderController) controller;
	            cont.setNotifRequestData(selectedItems);
	            cont.start();
	        }
       
        }
    }

    private boolean checkForMailServerConfig() {
      
        MailServerInfo mailServerInfo = DataHelper.loadMailServerInfo();
        if (mailServerInfo == null || !mailServerInfo.validate()) {
            
            AlertMaker.showWarningAlert("Warning","Mail server is not configured.Please configure mail server first.It is available under settings");
            return false;
        }else {
        	return true;
        }
    }

   public static class NotificationControlCellValueFactory implements Callback<CellDataFeatures<NotificationItem, JFXCheckBox>, ObservableValue<JFXCheckBox>> {

        @Override
        public ObservableValue<JFXCheckBox> call(TableColumn.CellDataFeatures<NotificationItem, JFXCheckBox> param) {
            NotificationItem item = param.getValue();
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().setValue(item.getNotify());
            checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                item.setNotify(new_val);
            });
            return new SimpleObjectProperty<>(checkBox);
        }
    }
}
