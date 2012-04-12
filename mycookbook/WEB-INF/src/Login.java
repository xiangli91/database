  
import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		//String name = request.getParameter("name");
		String password = request.getParameter("password");
		Connection connection = null;
		PrintWriter out = response.getWriter();
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
			Statement statement = connection.createStatement();
			
			out.println("<html><body>");
			out.println("<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css\">");
			//Check that member exists
			//String query = "S"
			
			//Check password
			String query = "Select password from members where email = '" + email + "'";
			ResultSet rs = statement.executeQuery(query);
			String correct_password = "";
			while (rs.next()){
				correct_password = rs.getString(1);
			}
			
			
			//out.println("done getting data");
			//out.println(correct_password);
			//out.println(password);
			//Direct to Wrong Password Page
			if (!correct_password.equals(password)){
				 out.println("<h3>Login Unsuccessful!</h3>");
				 
				 out.println("<form action='login.html	'>" + 
							"<input type ='submit' class='btn large primary' value='Click here to try again' />" +
						"</form>");
				 
				 out.println("<form action='join.html'>" + 
							"<input type ='submit' class='btn large primary' value='Click here to Join Today!' />" +
						"</form>");
				 
				 out.println("<form action='index.html'>" + 
							"<input type ='submit' class='btn large primary' value='Click here to continue browsing as a public user' />" +
						"</form>");
				
			}
			
			//ReDirect to Members Homepage 
			else{
				 out.println("<h3>Login Successful!</h3>");
				 
				 // get the session first!
				 HttpSession session = request.getSession(true);
				 synchronized (session) {  // synchronized to prevent concurrent updates
					   session.setAttribute("email", email);
					   session.setAttribute("login_check", true);
				}
				 
				 out.println("<form action='index'>" + 
					"<input type ='submit' class='btn large primary' value='Go to my Homepage now!' />" +
				"</form>");
				 
				 response.sendRedirect("/mycookbook/memberhome"); 
				 
		        // out.println("<p><a href='memberindex.html'>Go to my Homepage now!</a></p>");
			}
			
			out.println("</body></html>");
		}catch (SQLException e ) {
			out.println("SQL Exception!" + e);
			
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
