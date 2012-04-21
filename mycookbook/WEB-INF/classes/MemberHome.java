

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MemberHome
 */
@WebServlet("/memberhome")
public class MemberHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

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
			out.println("<title>Welcome to MyCookBook.com</title></head>");
			out.println("<body>");  
		
			out.println("<div class=\"page-header\">");
			out.println("<h1>myCookBook</h1>");
			out.println("<span>");
			out.println("<form action=\"logout\" class=\"pull-right\">");
			out.println("<input type =\"submit\" class=\"span3 btn small primary\" value=\"Logout\" />");
			out.println("</form>");
			out.println("</span>");
			out.println("</div>");
	  
			out.println("<br><strong> Search for receipes, add a receipe, or edit your fridge!</br>");
			
			out.println("<br>");	
			
			//Search
			out.println("<form action=\"search\"> " +
					"<input type =\"submit\" class=\"btn large primary\" value=\"Search for a Receipe\" /> ");
			
			out.println("</form>");
			
			//Add Receipe
			out.println("<form action=\"AddRecipePage.jsp\"> " +
				"<input type =\"submit\" class=\"btn large primary\" value=\"Add a Receipe\" /> " +
				"</form>");
			
			//My Receipe
			out.println("<form action=\"MyRecipe.jsp\"> " +
					"<input type =\"submit\" class=\"btn large primary\" value=\"Open My Cookbook!\" /> " +
					"</form>");
				
			
			//My Fridge
			out.println("<form action=\"MyFridge.jsp\"> " +
				 "<input type =\"submit\" class=\"btn large primary\" value=\"Update my Fridge\" /> " +
				" </form>");
		
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
