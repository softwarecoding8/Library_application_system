package library.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import library.model.MailServerInfo;
public class DataHelper {
	public static boolean updateMailServerInfo(MailServerInfo mailServerInfo,int employeeId) {
    		try {
           PreparedStatement statement = DBHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO mail_server_info(server_name,server_port,user_email,user_password,ssl_enabled,employee_id) VALUES(?,?,?,?,?,?)");
            statement.setString(1, mailServerInfo.getMailServer());
            statement.setInt(2, mailServerInfo.getPort());
            statement.setString(3, mailServerInfo.getEmailID());
            statement.setString(4, mailServerInfo.getPassword());
            statement.setBoolean(5, mailServerInfo.getSslEnabled());
            statement.setInt(6, employeeId);
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
        	//
        }
        return false;
    }

    public static MailServerInfo loadMailServerInfo() {
        try {
            String checkstmt = "SELECT * FROM MAIL_SERVER_INFO";
            //PreparedStatement stmt
            PreparedStatement stmt = DBHandler.getInstance().getConnection().prepareStatement(checkstmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String mailServer = rs.getString("server_name");
                Integer port = rs.getInt("server_port");
                String emailID = rs.getString("user_email");
                String userPassword = rs.getString("user_password");
                Boolean sslEnabled = rs.getBoolean("ssl_enabled");
                
                return new MailServerInfo(mailServer, port, emailID, userPassword, sslEnabled);
                

            }
            
        } catch (SQLException ex) {
        }
        return null;
    }
}
