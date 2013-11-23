
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class UserProfileServlet
 */
@WebServlet("/all.html")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	PrintWriter out;
	DBConnection db_connect = new DBConnection();

    /**
     * Default constructor. 
     */
    public UserProfileServlet() {
        // TODO Auto-generated constructor stub
    	db_connect.setConnection();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * @produces(MediaType.APPLICATION_JSON)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// do some filter here for user profile/yahoo query
		
		response.setContentType("application/json");
	    out = response.getWriter();

	    String email = request.getParameter("email");
	    String password = request.getParameter("password");

	    responseUser(request, email, password);
	    
	   /** String first_name = request.getParameter("first name");
	    String surname = request.getParameter("surname");
	    String gender = request.getParameter("gender");
	    
	    JSONObject check = new JSONObject();
	    int ok = 1;
	    
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
	    	createUser(res, email, password, first_name, surname, gender);
	    }
	    */

	}
	
	private void responseUser(HttpServletRequest request, String email,
			  String password) throws IOException{
		  BufferedReader test = new BufferedReader(new FileReader("test.txt"));
		  String test_email = test.readLine();
		  String test_password = test.readLine();

		  JSONObject result = new JSONObject();
		  
		  if (email.equalsIgnoreCase(test_email)){
			  	if (password.equals(test_password)){
		    		result.put("email", email);
		    		result.put("password", "correct");
		    		out.print(result);
		    		out.flush();
		    	}
		    	else {
		    		result.put("email", email);
		    		result.put("password", "wrong");
		    	}
		    
		    }
		    else {
		    	result.put("email", "invalid");
		    	result.put("password", "wrong");
		    	out.print(result);
		    	out.flush();
		    }
		  
	  }
	
	private void createUser(HttpServletResponse resp, String email, String password, String first_name,
			String surname, String gender) throws IOException{
		
		// make sure you remember to actually connect to the db
		
		JSONObject result = new JSONObject();
		result.put("email", email);
		out.print(result);
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("We get here");
	}

}
