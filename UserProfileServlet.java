import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.*;

import org.json.simple.JSONObject;
import java.sql.*;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet("/all.html")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PrintWriter out;
	Connection db_connect;
	DBConnection to_connect = new DBConnection();
	HttpSession session;
	AccountHandler user_processing = new AccountHandler();

    /**
     * Default constructor. 
     * Sets up a database connection
     */
    public UserProfileServlet() {
    	db_connect = to_connect.setConnection();
    }

	/**
	 * Processes user requests, e.g. user log in or create user
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @produces(MediaType.APPLICATION_JSON)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// do some filter here for user profile/yahoo query
		// was to work using unique form values
				
		session = request.getSession();
				
		// always returns a JSON
		response.setContentType("application/json");
		out = response.getWriter();

		// gets form data
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		// log in attempt
		responseUser(request, email, password);
			    
			    
		// gets other form data (for create user)
		/** String first_name = request.getParameter("first name");
		String surname = request.getParameter("surname");
		String gender = request.getParameter("gender");
			    
		// test to make sure the form has all the data needed
		JSONObject check = new JSONObject();
		int ok = 1;
			    
		// if a field is left empty, sends an error back
		// also done client-side
		if (email == null){
		 	check.put("email", "error");
		 	ok = 0;
		}
		if (password == null){
			check.put("password", "error");
			ok = 0;
		}
		if (first_name == null){
			check.put("first name", "error");
			ok = 0;
		}
		if (surname == null){
			check.put("surname", "error");
			ok = 0;
		}
		if (gender == null){
			check.put("gender", "error");
			ok = 0;	
		}
		
	    if (ok == 0){
			out.print(check);
			out.flush();
		}
		else {
			createUser(request, email, password, first_name, surname, gender);
		}
		*/
			    
		// logs out a user
		//logOut(request);
	}

	// tests whether log in details are valid
	private void responseUser(HttpServletRequest request, String email,
			String password) throws IOException {

		AccountHandler.success_value result = user_processing.logIn(email, password);

		JSONObject to_return = new JSONObject();
		
		// if logged in
		if (result == AccountHandler.success_value.SUCCESS) {
			// sets up a logged in session
			if (session.isNew()) {
				session.setAttribute("loggedin", email);
			} else {
				session.invalidate();
				session.setAttribute("loggedin", email);
			}
			// sends back a success JSON
			to_return.put("email", "success");
			to_return.put("password", "success");
			out.print(to_return);
			out.flush();
		}
		// if failed to log in
		else {
			// if incorrect email sends back email failure JSON
			if (result == AccountHandler.success_value.INCORRECT_EMAIL){
				to_return.put("email", "incorrect");
				to_return.put("password", "N/A");
				out.print(to_return);
				out.flush();
			}
			// if incorrect password sends back password failure JSON
			if (result == AccountHandler.success_value.INCORRECT_PASSWORD){
				to_return.put("email", "success");
				to_return.put("password", "incorrect");
				out.print(to_return);
				out.flush();
			}
			// if sql error sends back sql failure JSON (N/A means SQL error here)
			if (result == AccountHandler.success_value.SQL_ERROR){
				to_return.put("email", "N/A");
				to_return.put("password", "N/A");
				out.print(to_return);
				out.flush();
			}
		}

	}

	// creates a new user
	private void createUser(HttpServletResponse resp, String email,
			String password, String first_name, String surname, String gender)
			throws IOException {

		boolean result = user_processing.createUser(email, password, first_name, surname, gender);

		if (result) {
			// sets up a logged in session using the new user
			if (session.isNew()) {
				session.setAttribute("loggedin", email);
			} else {
				session.invalidate();
				session.setAttribute("loggedin", email);
			}

			// sends back a success JSON
			JSONObject to_return = new JSONObject();
			to_return.put("creation", "success");
			out.print(to_return);
			out.flush();
		}
		//
		else {
			JSONObject to_return = new JSONObject();
			to_return.put("creation", "failure");
			out.print(to_return);
			out.flush();
		}
	}

	
	// logs out the user (deletes the session)
	private void logOut(HttpServletResponse resp) {
		session.invalidate();
	}
	
	// removes a user completely from the database
	private void removeUser(HttpServletResponse resp, String email, String password){
		
		AccountHandler.success_value result = user_processing.removeUser(email, password);
		
		JSONObject to_return = new JSONObject();
		
		// if user was deleted
		if (result == AccountHandler.success_value.SUCCESS) {
			// invalidates user session
			session.invalidate();
			to_return.put("deleted", "success");
			out.print(to_return);
			out.flush();
		}
		// if failed to delete user
		else {
			// if incorrect email sends back email failure JSON
			if (result == AccountHandler.success_value.INCORRECT_EMAIL){
				to_return.put("deleted", "email");
				out.print(to_return);
				out.flush();
			}
			// if incorrect password sends back password failure JSON
			if (result == AccountHandler.success_value.INCORRECT_PASSWORD){
				to_return.put("deleted", "password");
				out.print(to_return);
				out.flush();
			}
			// if sql error sends back sql failure JSON (N/A means SQL error here)
			if (result == AccountHandler.success_value.SQL_ERROR){
				to_return.put("deleted", "sql");
				out.print(to_return);
				out.flush();
			}
		}
		
	}
	
}
