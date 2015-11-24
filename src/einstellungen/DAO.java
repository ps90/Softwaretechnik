package einstellungen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {
	
	public static Connection getDBCon() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String user = "root";//SYSTEM
		String passwd = "admin";
		String url = "jdbc:mysql://127.0.0.1:3306/wgkasse";
		return DriverManager.getConnection(url, user, passwd);
		
	}
	public static void closeConnection(Connection con) throws SQLException{
		if(con != null){
			con.close();
		} 
	}
}