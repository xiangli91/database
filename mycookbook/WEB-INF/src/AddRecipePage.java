

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
 * Servlet implementation class AddRecipePage
 */
@WebServlet("/addrecipepage")
public class AddRecipePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRecipePage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
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
		
		//Display the header elements
		Connection connection = null;
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<link rel=\"stylesheet\" href=\"http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css\">");
		out.println("<title>My Recipes! </title></head>");
		out.println("<body>");  
	
		out.println("<div class=\"page-header\">");
		out.println("<h1>Add a new Recipe!</h1>");
		out.println("<span>");
		out.println("<form action=\"logout\" class=\"pull-right\">");
		out.println("<input type =\"submit\" class=\"span3 btn small primary\" value=\"Logout\" />");
		out.println("</form>");
		out.println("</span>");
		out.println("</div>");
		
		//Validation Form 
		/*
				out.println("</form>");
				
				out.println("<script type=\"text/javascript\">" +
							"function validateForm()" +
							"{" +
								"var x=document.forms[\"recipe\"][\"dishname\"].value;" +
								"if (x==null || x==\"\"){" +
										"alert(\"Please select a dish!\");"+
											"return false;" +
								"}" +
											
								"var x=document.forms[\"recipe\"][\"people\"].value;" +
								"if (x==null || x==\"\"){" +
										"alert(\"Please tell us how many people this recipe serves!!\");"+
											"return false;" +
								"}" +
																
											
								"var x=document.forms[\"recipe\"][\"instructions\"].value;" +
								"if (x==null || x==\"\"){" +
										"alert(\"Please explain your preparation instructions!\");"+
											"return false;" +
								"}" +
								
							"}"+
										
							"</script>");
		*/
		//Fill in the Blanks!
		out.println("<form name=\"recipe\" action=\"addrecipe\" onsubmit=\"return validateForm()\">");
		
			//Form1: dishname
			LinkedList<String> dishnames = new LinkedList<String>();
			try{
				//connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
				connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
				Statement statement = connection.createStatement();
				String query = "Select dishname from dishes" ;
				ResultSet rs = statement.executeQuery(query);	
				while (rs.next()){
					dishnames.add(rs.getString(1));
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
			out.println("Dish Name:");
			out.println("<select name=\"dishname\">");
			for(int i = 0; i < dishnames.size();i++){
				out.println("<option value=\"" + dishnames.get(i) + "\">" + dishnames.get(i) + "</option>");
			}
			out.println("</select>");
			out.println("<br />");
			out.println("<br />");
			
			//Form 2: number of people served
			out.println("Number of People Served:");
			out.println("<select name=\"People\">");
			for(int i = 1; i <= 10;i++){
				out.println("<option value=\"" + i + "\">" + i + "</option>");
			}
			out.println("</select>");
			
			out.println("<br />");
			out.println("<br />");
			
			
			//Form 3: ingredients - quantity and measure_type 
			out.println("Ingredients:"); 
			//Li Xiang please fill this up. 
			
			for(int i = 1; i<=5;i++){
				out.println("Ingredient: <input type=\"text\" name=\"ingredient" + i + "_name\" />");
				out.println("Quantity: <input type=\"int\" name=\"ingredient" + i + "_quantity\" />");
				out.println("Measure: <input type=\"text\" name=\"ingredient" + i + "_measure\" />");
				out.println("<br />");
			}
			
			//Form 4: preparation
			out.println("Input your instructions here");
			out.println("<textarea cols=\"40\" rows=\"5\" name=\"instructions\">");
			out.println("</textarea>");
			out.println("<br />");
			out.println("<br />");
		
		// submission button 
		out.println("<input type =\"submit\" class=\"btn large primary\" value=\"Add this receipe!\" /> ");
		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	

}
