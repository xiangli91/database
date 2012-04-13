

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class MyRecipe
 */
@WebServlet("/myrecipe")
public class MyRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyRecipe() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		PrintWriter out = response.getWriter();
		//check if user has logged in or not
		
		HttpSession session = request.getSession(true);
		Object result = session.getAttribute("login_check"); 
		Boolean logged_in = false; 
		if (result != null){ 
			logged_in = (Boolean) session.getAttribute("login_check");
		}

		
		if (logged_in){		
			out.println("<html>");
			out.println("<head>");
			out.println("<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css\">");
			out.println("<title>My Recipes! </title></head>");
			out.println("<body>");  
		
			out.println("<div class=\"page-header\">");
			out.println("<h1>My very own cookbook</h1>");
			out.println("<span>");
			out.println("<form action=\"logout\" class=\"pull-right\">");
			out.println("<input type =\"submit\" class=\"span3 btn small primary\" value=\"Logout\" />");
			out.println("</form>");
			out.println("</span>");
			out.println("</div>");
			
			//Get what's in your receipe
			LinkedList<String> col1 = new LinkedList<String>();
			LinkedList<String> col2 = new LinkedList<String>();
			LinkedList<String> col3 = new LinkedList<String>();
			
			try{
				connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
				String email = (String) session.getAttribute("email"); 
				Statement statement = connection.createStatement();
				String query = "Select dishname, num_comments, num_likes from recipes where email = '" + email +"'" + 
								"order by time desc";
				ResultSet rs = statement.executeQuery(query);		
				while (rs.next()){
					col1.add(rs.getString(1));
					col2.add(rs.getString(2));
					col3.add(rs.getString(3));
				}

			}catch (SQLException e ) {
					out.println(e); 		
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			// Check for previous receipe updates!
			String updates = (String) session.getAttribute("updaterecipe");
			if (updates !=null){ 
				if (updates.equals("Fail")){
					out.println("Your receipe update could not be completed.");
					session.setAttribute("updaterecipe", "done");
				}
				else if (updates.equals("Success")){
					out.println("You receipe update was successful!");
					session.setAttribute("updaterecipe", "done");
				}
			}
			
			// Print what's in your recipe
			
			if (col1.size()<1){
				out.println("You have no recipes as of now! Time to do start creating your own cookbook today!");
			}
			
			else{
				out.println("<form>"); 
				out.println("Here are you recipes!");
				out.println("<TABLE BORDER=1>");
				
				//print the header
				out.println("<TR>");
					out.println("<TH>" + "Dish" + "</TH>");
					out.println("<TH>" + "Number of Likes" + "</TH>");
					out.println("<TH>" + "Number of Comments" + "</TH>");
				out.println("</TR>");
				
				// print the latest 10 recipes
				 for (int i=0; i<Math.min(10,col1.size());i++){ 
					 out.println("<TR>");
					 	out.println("<TD>" + col1.get(i) + "</TD>");
					    out.println("<TD>" + col2.get(i) + "</TD>");
					    out.println("<TD>" + col3.get(i) + "</TD>");
					  out.println("</TR>"); 
				 }
				 out.println("</TABLE>");
				 out.println("</form>");
			}
			
			//Update your fridge
			
			//prepare the drop down menu
			out.println("<br>");		

			out.println("<form action=\"addrecipepage\"> ");
			out.println("<input type =\"submit\" class=\"btn large primary\" value=\"Add a Receipe\" /> ");
			out.println("</form>");
			
			out.println("</body>");
			out.println("</html>");
		}
		
		else {
			 response.sendRedirect("/mycookbook/index.html"); 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
