package library.view;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import library.DBConnection.DBHandler;
import library.model.Book;
import library.model.IssuedBook;

public class IssuedBooksStatisticsController implements Initializable {
	@FXML
	private BarChart<String,Integer> barChart;
	@FXML
	private CategoryAxis xAxis;
	private ObservableList<String> monthNames = FXCollections.observableArrayList();
	ObservableList<IssuedBook> list =FXCollections.observableArrayList();
	
	private final Connection con;
	private DBHandler handler =  DBHandler.getInstance();
	
public IssuedBooksStatisticsController() {
		con = handler.getConnection();
}
/**
 * Initializes  the controller class.This method is automatically called after the fxml file has been loaded
 */
//@FXML
//private void initialize() {
@Override
public void initialize(URL arg0, ResourceBundle arg1) {	
	//Get an array with the English month names
	String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
	//Convert it to a list and add it to our ObservableList of months
	monthNames.addAll(Arrays.asList(months));
	
	//Assign the month names as categories for the horizontal axis
	xAxis.setCategories(monthNames);
	try {
		getIssuedBookBarChart();
	}catch(SQLException ex) {
		
	}
}
	
public ObservableList<IssuedBook> getIssuedBook() throws SQLException{
	PreparedStatement pst = null;
	ResultSet rs = null;
	String select = "select issuedate from lendbook union select issuedate from returnbook";
	try {
		pst = con.prepareStatement(select);
		rs = pst.executeQuery();
	
		while(rs.next()) {
				list.add(new IssuedBook(rs.getDate("issuedate").toLocalDate()));
		}
	}catch(SQLException ex) {
		
		//ex.printStackTrace();
	}finally {
		pst.close();
		rs.close();
	}
	return list;
}




public void getIssuedBookBarChart() throws SQLException{
	ObservableList<IssuedBook> list = getIssuedBook();
	//count the number of people was issued book in a specific month
	int[] monthCounter = new int[12];
	for(IssuedBook bk : list) {
		int month = bk.getIssuedDate().getMonthValue()-1;
		monthCounter[month]++;
	}
	
	XYChart.Series<String, Integer> series = new XYChart.Series<>();
	
	//create a XYChart.Data object for each month.Add it to the series
	for(int i = 0; i < monthCounter.length; i++) {
		series.getData().add(new XYChart.Data<>(monthNames.get(i),monthCounter[i]));
	}
	
	barChart.getData().add(series);
}
}
