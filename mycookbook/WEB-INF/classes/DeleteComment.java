

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
 * Servlet implementation class DeleteComment
 */
@WebServlet("/deletecomment")
public class DeleteComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteComment() {
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
			session.setAttribute("deletecomment", "login");
			response.sendRedirect("/mycookbook/ViewRecipe.jsp");
		}
		session.setAttribute("deletecomment","Failure");	
		String commenter_email = (String) session.getAttribute("email");
		Connection connection = null;
		PrintWriter out = response.getWriter();
		
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			int comment_id = Integer.parseInt(request.getParameter("comment_id"));
			//int comment_id = Integer.parseInt((String) session.getAttribute("comment_id")); 
			int recipe_id = Integer.parseInt((String) session.getAttribute("current_recipe_id")); 
			Statement statement = connection.createStatement();
			
			// Delete the comment
			String update = "Delete from recipe_comments where comment_id =" + comment_id;	
			int updatecheck1 = statement.executeUpdate(update);
			
			//update the recipe itself
			update = "Update recipes set num_comments = num_comments-1 where recipe_id = " + recipe_id;
			int updatecheck2 = statement.executeUpdate(update);
		
			if (updatecheck1 == 1 & updatecheck2 == 1){
				session.setAttribute("deletecomment","Success");
			}
		//	else{
		//		session.setAttribute("deletecomment","Failure");					
		//	}
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
