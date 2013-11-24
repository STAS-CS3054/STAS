import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// connection to the database
public class DBConnection {

	// sets up the connection to the database
	public Connection setConnection(){
		
		// gets driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver found");
		}
		catch (ClassNotFoundException e){
			System.out.println("Driver not found: " + e);
		}
		
		// stats for the mysql database we are using
		String url = "jdbc:mysql://daw20.host.cs.st-andrews.ac.uk/daw20_db";
		String user = "daw20";
		String password = "Yu!1VrG1";
		
		Connection con = null;
		
		// tries to connect to the database
		try {
			con = DriverManager.getConnection(url, user,password);
			System.out.println("Connected successfully");
		}
		catch (SQLException e){
			System.out.println("Something went wrong in the connection string");
		}
	
		// returns the connection
		return con;
		
	}
	
}
