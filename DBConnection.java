import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public Connection setConnection(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver found");
		}
		catch (ClassNotFoundException e){
			System.out.println("Driver not found: " + e);
		}
		String url = "jdbc:mysql://daw20.host.cs.st-andrews.ac.uk/daw20_db";
		String user = "daw20";
		String password = "Yu!1VrG1";
		
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(url, user,password);
			System.out.println("Connected successfully");
		}
		catch (SQLException e){
			System.out.println("Something went wrong in the connection string");
		}
	
		return con;
		
	}
	
}
