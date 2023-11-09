package library.view;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import library.DBConnection.DBHandler;

public class DashboardController implements Initializable{
	private DBHandler handler =  DBHandler.getInstance();
    private final Connection con;
    @FXML
	private ImageView bksView;
	@FXML
	private ImageView lendedbksView;
	@FXML
	private ImageView memView;
	@FXML
	private ImageView fineView;
	@FXML
	private ImageView supView;
	@FXML
	private ImageView bksCopiesView;
    @FXML
	private Text total_books;
	@FXML
	private Text lended_books;
	@FXML
	private Text books_copies;
	@FXML
	private Text members;
	@FXML
	private Text total_fine;
	@FXML
	private Text suppliers;
	private int book_count = 0;
	private int lended_bk_count = 0;
	private int member_count = 0;
	private int supplier_count = 0;
	private int bksCopies_count = 0;
	private float fine_count = 0;
	
	public DashboardController() {
		con = handler.getConnection();
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dashboardViewer();
		try {
			dashboardValues();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void dashboardViewer() {
		File bks = new File("src/resources/books.jpg");
		File lended = new File("src/resources/issued_books.png");
		File membs = new File("src/resources/members.jpg");
		File fin = new File("src/resources/fine_collected.png");
		File supplier = new File("src/resources/supplier.png");
		File bks_copies = new File("src/resources/book_copies.png");
		
		Image image1 = new Image(bks.toURI().toString());
		bksView.setImage(image1);
		Image image2 = new Image(lended.toURI().toString());
		lendedbksView.setImage(image2);
		Image image3 = new Image(membs.toURI().toString());
		memView.setImage(image3);
		Image image4 = new Image(fin.toURI().toString());
		fineView.setImage(image4);
		Image image5 = new Image(supplier.toURI().toString());
		supView.setImage(image5);
		Image image6 = new Image(bks_copies.toURI().toString());
		bksCopiesView.setImage(image6);
		
	}
	
	public void dashboardValues() throws SQLException{
		PreparedStatement ps = null;
        ResultSet rs = null;
         try { 
	        String qu1 = "SELECT COUNT(*) FROM BOOKS";
	        ps = con.prepareStatement(qu1);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            book_count = rs.getInt(1);
	            total_books.setText(Integer.toString(book_count));
	        }
	        
	        String qu2 = "SELECT COUNT(*) FROM LENDBOOK";
	        ps = con.prepareStatement(qu2);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	           lended_bk_count = rs.getInt(1);
	           lended_books.setText(Integer.toString(lended_bk_count));
	        }
	       
	        String qu3 = "SELECT COUNT(*) FROM MEMBERS";
	        ps = con.prepareStatement(qu3);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	           member_count = rs.getInt(1);
	           members.setText(Integer.toString(member_count));
	        }
	        
	        String qu4 = "SELECT fine FROM RETURNBOOK";
	        ps = con.prepareStatement(qu4);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	           fine_count += rs.getFloat(1);
	           total_fine.setText(Float.toString(fine_count));
	        }
	        
	        String qu5 = "SELECT COUNT(*) FROM SUPPLIERS";
	        ps = con.prepareStatement(qu5);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            supplier_count = rs.getInt(1);
	            suppliers.setText(Integer.toString(supplier_count));
	        }
	        
	        String qu6 = "SELECT stock FROM BOOKS";
	        ps = con.prepareStatement(qu6);
	        rs = ps.executeQuery();
	        while (rs.next()) {
	        	bksCopies_count += rs.getInt(1);
	        	books_copies.setText(Integer.toString(bksCopies_count));
	        }
	       
       }catch(SQLException ex) {
    	   
       }finally {
	       	ps.close();
	       	rs.close();
       }
	}
}
