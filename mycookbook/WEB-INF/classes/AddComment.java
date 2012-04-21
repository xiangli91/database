

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class AddComment
 */
@WebServlet("/addcomment")
public class AddComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComment() {
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
			session.setAttribute("addcomment", "login");
			response.sendRedirect("/mycookbook/ViewRecipe.jsp");
			return; 
		}
		session.setAttribute("addcomment","Failure");
		
		Connection connection = null;
		PrintWriter out = response.getWriter();
		
		//out.println("here!");
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			int recipe_id = Integer.parseInt((String) session.getAttribute("current_recipe_id")); 
			Statement statement = connection.createStatement();
			
			//1. Get the Comment ID
			Integer comment_id = 0;
			String query = "select max(comment_id) from recipe_comments";
			ResultSet rs = statement.executeQuery(query);	
			while(rs.next()){
				comment_id = rs.getInt(1);
			}
			if(comment_id == null){
				comment_id = 1;
			}else{
				comment_id ++;
			}
			
			//out.println("comment id :" +comment_id);
			//2. Get the parameters
			String commenter_email = (String) session.getAttribute("email");
			query = "Select name from members where email = '" + commenter_email + "'";
			String commentername = "";
			rs = statement.executeQuery(query);	
			while (rs.next()){
				commentername = rs.getString(1);
			}
			String comment = request.getParameter("comments") ;
			//out.println("okay");
			
			Date date = new Date();
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			
			//3. Insert the new comment 
			String insert = "Insert into recipe_comments (comment_id, recipe_id, email, name, comment, time) values ("
							+ comment_id + ", " 
							+ recipe_id + ", " 
							+ "'" + commenter_email + "', " 
							+ "'" + commentername + "', "
							+ "'" + comment + "', "
							+ "'" + time + "')"; 
			int updatecheck1 = statement.executeUpdate(insert);
			//out.println("okay + updatecheck: " + updatecheck);

			//update the recipe itself
			String update = "Update recipes set num_comments = num_comments+1 where recipe_id = " + recipe_id;
			int updatecheck2 = statement.executeUpdate(update);
			
			if (updatecheck1 == 1 & updatecheck2 == 1){
				session.setAttribute("addcomment","Success");
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
