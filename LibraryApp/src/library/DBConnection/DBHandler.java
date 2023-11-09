package library.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHandler extends Configs{
	
	 private static DBHandler handler = null;
	 private static Connection dbConnection = null;
	 private static Statement stmt = null;
	 
	 private DBHandler() {
	    
	 }

	 public static DBHandler getInstance() {
	    if (handler == null) {
	            handler = new DBHandler();
	     }
	     return handler;
	 }
	
	public Connection getConnection() {
		String connectionString = "jdbc:mysql://localhost:3306/library";
		try {
			//Class.forName("jdbc.mysql.Driver");
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
				dbConnection = DriverManager.getConnection(connectionString,dbuser,dbpass);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return dbConnection;
	}
}
