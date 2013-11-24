import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.security.*;
import java.util.Arrays;

public class AccountHandler {
	
	private static final int SALT_SIZE = 128 / 8;
	private static final String IDUSER = "iduser";
	private static final String USER = "user";
	private static final String PASSWORD_HASH = "password_hash";
	private static final String PASSWORD_SALT = "password_salt";
	private static final String EMAIL = "email";
	private static final String FORENAME = "forename";
	private static final String SURNAME = "surname";
	private static final String GENDER = "gender";
	private static SecureRandom rand = new SecureRandom();
	
	public enum success_value {
		SUCCESS, INCORRECT_PASSWORD, INCORRECT_EMAIL, SQL_ERROR;
	}
	
	DBConnection db = new DBConnection();
	Connection conn;
	
	public boolean createUser(String email, String password, String forename, String surname, String gender) {
		conn = db.setConnection();
		ResultSet rs;
		byte[] salt = generateSalt();
		byte[] password_hash = hashPassword(password, salt);
		try {
			Statement stmt = conn.createStatement();
			// Get the user with the email provided.
			rs = stmt.executeQuery("SELECT u." + EMAIL + " FROM " + USER + " AS u WHERE u." + EMAIL + "=\'" + email.toLowerCase() + "\';");
			// If no user exists with this email, create one.
			if (!rs.next()) {
				String query = "INSERT INTO "+ USER + " (" + PASSWORD_HASH + "," + PASSWORD_SALT + "," + EMAIL + "," + FORENAME + "," + SURNAME + "," + GENDER + ") "
						+ "VALUES (?,?,?,?,?,?);";
				PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(query);
				pstmt.setBytes(1, password_hash);
				pstmt.setBytes(2, salt);
				pstmt.setString(3, email.toLowerCase());
				pstmt.setString(4, forename);
				pstmt.setString(5, surname);
				pstmt.setString(6, gender);
				pstmt.execute();
				return true;
			} else {
				// If a user already exists with this email, return false.
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public success_value logIn(String email, String password) {
		conn = db.setConnection();
		ResultSet rs;
		try {
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT u." + PASSWORD_HASH + ", u." + PASSWORD_SALT 
					+ " FROM " + USER + " AS u WHERE u." + EMAIL + "=\'" + email.toLowerCase() + "\';");
			// Check if an account with that username exists
			if (rs.next()) {
				// If it does, retrieve the salt and the password hash
				byte[] actual_hash = rs.getBytes(PASSWORD_HASH);
				byte[] salt = rs.getBytes(PASSWORD_SALT);
				// Hash the password with the salt and check if they match
				return (Arrays.equals(hashPassword(password, salt), actual_hash) ? success_value.SUCCESS : success_value.INCORRECT_PASSWORD);
			} else {
				// No user existed
				return success_value.INCORRECT_EMAIL;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success_value.SQL_ERROR;
	}
	
	public success_value removeUser(String email, String password) {
		conn = db.setConnection();
		ResultSet rs;
		try {
			// Check that the username and password is correct
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT u." + IDUSER + ", u." + PASSWORD_HASH + ", u." + PASSWORD_SALT + " FROM " + USER + " AS u WHERE " + EMAIL + "=\'" + email.toLowerCase() + "\';");
			if (rs.next()) {
				byte[] actual_hash = rs.getBytes(PASSWORD_HASH);
				byte[] salt = rs.getBytes(PASSWORD_SALT);
				if (Arrays.equals(hashPassword(password, salt), actual_hash)) {
					// If the username and password are correct, delete the user with the user ID that was found.
					stmt.executeUpdate("DELETE FROM " + USER + " WHERE " + IDUSER + "=" + rs.getInt(IDUSER) + ";");
					return success_value.SUCCESS;
				} else {
					return success_value.INCORRECT_PASSWORD;
				}
			} else {
				return success_value.INCORRECT_EMAIL;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success_value.SQL_ERROR;
	}
	
	public byte[] hashPassword(String password, byte[] salt) {
		try {
			// Get the password as a byte array
			byte[] password_bytes = password.getBytes("UTF-8");
			// Add the password and salt to the MessageDigest, and then hash them.
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password_bytes);
			return md.digest(salt);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] generateSalt() {
		byte[] salt = new byte[SALT_SIZE];
		rand.nextBytes(salt);
		return salt;
	}
}
