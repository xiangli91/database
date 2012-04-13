

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class ViewRecipe
 */
@WebServlet("/ViewRecipe")
public class ViewRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewRecipe() {
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
		HttpSession session = request.getSession(true);
		
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
		
		//Print: The recipe
		
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
			int recipe_id = Integer.parseInt((String) session.getAttribute("current_recipe_id")); 
			Statement statement = connection.createStatement();
			
			String query = "Select * from recipes where recipe_id =" + recipe_id;
			ResultSet rs = statement.executeQuery(query);	
			
			String creator = "";
			String dishname = ""; 
			String instructions = "";
			int num_comments = 0;
			int num_likes = 0;
			int num_served = 0;
			String date = "";
			float[] nutrients = new float[14];
			
			
			while (rs.next()){
				dishname = rs.getString(2);
				creator = rs.getString(3);
				instructions = rs.getString(4);
				num_comments = rs.getInt(5);
				num_likes = rs.getInt(6);
				for(int i = 0;i<14;i++){
					nutrients[i]= rs.getFloat(i+7);
				}
				num_served = rs.getInt(21);
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getDate(22)); 
			}
			
			
			// Incomplete
			
			out.println("dishname: " + dishname); 
			out.println("<br />");
			out.println("By: " + creator);
			out.println("<br />");
			out.println("Instructions: " + instructions);
			out.println("<br />");
			out.println("Comments: " + num_comments);
			out.println("<br />");
			
			//to add in likes, comments. 

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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
