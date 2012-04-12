

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UpdateFridge
 */
@WebServlet("/updatefridge")
public class UpdateFridge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateFridge() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//get the parameters
		String ingredient = request.getParameter("ingredient");
		String update_type = request.getParameter("update_type");
		
		// check that user is logged in
		HttpSession session = request.getSession(true);
		Object result = session.getAttribute("login_check"); 
		Boolean logged_in = false; 
		if (result != null){ 
			logged_in = (Boolean) session.getAttribute("login_check");
		}
		if (!logged_in){	
			response.sendRedirect("/mycookbook/index.html");
		}
		
		String email = (String) session.getAttribute("email"); 
		
		//Update the user's fridge
		Connection connection = null;
		PrintWriter out = response.getWriter();
		session.setAttribute("updatefridge", "Fail");
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
			Statement statement = connection.createStatement();
			
			out.println("<html><body>");
			out.println("<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css\">");
			
			// Add or Delete Ingredient 
			String update = "";
			if (update_type.equals("Add")){
				update = "Insert into fridge (email, ingredient) values ('" + email + "','" + ingredient + "')";
			} else{
				update = "Delete from fridge where email='"
						+ email + "' AND ingredient='" + ingredient + "'";
			}
			int rs = statement.executeUpdate(update);
			if (rs == 1){
				session.setAttribute("updatefridge","Success");
			}
			
			response.sendRedirect("/mycookbook/myfridge"); 
			
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
