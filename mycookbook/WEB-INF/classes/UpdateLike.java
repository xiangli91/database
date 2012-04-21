

import java.io.IOException;
import java.io.PrintWriter;
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

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class IncreaseLike
 */
@WebServlet("/updatelike")
public class UpdateLike extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateLike() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession(true);
		Object result = session.getAttribute("login_check"); 
		Boolean logged_in = false; 
		if (result != null){ 
			logged_in = (Boolean) session.getAttribute("login_check");
		}
		if (!logged_in){	
			session.setAttribute("updatelike", "login");
			response.sendRedirect("/mycookbook/ViewRecipe.jsp");
		}
		session.setAttribute("updatelike","Failure");
		
		String liker_email = (String) session.getAttribute("email");
		Connection connection = null;
		PrintWriter out = response.getWriter();
		
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			int recipe_id = Integer.parseInt((String) session.getAttribute("current_recipe_id")); 
			//out.println("recipe id: " + recipe_id );
			Statement statement = connection.createStatement();
		
			String query = "Select email from recipes where recipe_id =" + recipe_id;
			String likee_email = "";
			ResultSet rs = statement.executeQuery(query);	
			while (rs.next()){
				likee_email = rs.getString(1);
			}
			
			
			if(request.getParameter("UpdateLike").equals("Unlike")){
				//unlike
				//update the recipe_likes
				//out.println("unlike!");
				String update = "Delete from recipe_likes where "
								+ "liker_email ='" + liker_email + "' and " 
								+ "likee_email ='" + likee_email + "' and " 
								+ "recipe_id = " + recipe_id;
				int updatecheck1 = statement.executeUpdate(update);
			
				//update the recipe itself
				update = "Update recipes set num_likes = num_likes-1 where recipe_id = " + recipe_id;
				int updatecheck2 = statement.executeUpdate(update);
			
				//update members
				update = "Update members set num_likes = num_likes-1 where email = '" + likee_email + "'";
				int updatecheck3 = statement.executeUpdate(update);
				//out.println(updatecheck1 + " " + updatecheck2 + " " + updatecheck3);
				if (updatecheck1 == 1 & updatecheck2 == 1 & updatecheck3 == 1){
					session.setAttribute("updatelike","Success");
				}
				//else{
				//	session.setAttribute("updatelike","Failure");					
				//}
			}
			else if (request.getParameter("UpdateLike").equals("Like")){
				//like
				//update the recipe_likes
				//out.println("like!");
				String update = "Insert into recipe_likes (liker_email, likee_email, recipe_id) values ('" + liker_email + "','" + likee_email + "', " + recipe_id + ")";
				int updatecheck1 = statement.executeUpdate(update);
			
				//update the recipe itself
				update = "Update recipes set num_likes = num_likes+1 where recipe_id = " + recipe_id;
				int updatecheck2 = statement.executeUpdate(update);
			
				//update members
				update = "Update members set num_likes = num_likes+1 where email = '" + likee_email + "'";
				int updatecheck3 = statement.executeUpdate(update);
				
				if (updatecheck1 == 1 & updatecheck2 == 1 & updatecheck3 == 1){
					session.setAttribute("updatelike","Success");
				}
				//else{
				//	session.setAttribute("updatelike","Failure");					
				//}
			}
		
		}catch (SQLException e ) {
			out.println(e); 		
		} finally {
			response.sendRedirect("/mycookbook/ViewRecipe.jsp"); 
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
