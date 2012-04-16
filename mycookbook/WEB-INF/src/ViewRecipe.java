

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

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
@WebServlet("/viewrecipe")
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
		String liker_email = (String) session.getAttribute("email"); //returns null if nothing is bound under this attribute. 
		
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
	
		
		
		//Main: Print the Recipe, Likes, Comments. 
		
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			
			Integer recipe_id = null; 
			if (request.getParameter("recipe_id") != null ){
				//get the latest request
				recipe_id = Integer.parseInt(request.getParameter("recipe_id"));
				session.setAttribute("current_recipe_id",Integer.toString(recipe_id));

			} else{
				recipe_id = Integer.parseInt((String) session.getAttribute("current_recipe_id")); 

			}
			Statement statement = connection.createStatement();
			
			String query = "Select * from recipes where recipe_id =" + recipe_id;
			ResultSet rs = statement.executeQuery(query);	
			
			String creatoremail = "";
			String creatorname = "";
			String dishname = ""; 
			String instructions = "";
			int num_comments = 0;
			int num_likes = 0;
			int num_served = 0;
			String date = "";
			float[] nutrients = new float[14];
			
			
			while (rs.next()){
				dishname = rs.getString(2);
				creatoremail = rs.getString(3);
				creatorname = rs.getString(4);
				instructions = rs.getString(5);
				num_comments = rs.getInt(6);
				num_likes = rs.getInt(7);
				for(int i = 0;i<14;i++){
					nutrients[i]= rs.getFloat(i+8);
				}
				num_served = rs.getInt(22);
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getDate(23)); 
			}
						
			// Check for success of likes and comments!!
			out.println("<div> ");
			
				//check for likes 
			String updates = (String) session.getAttribute("updatelike");
			if (updates !=null){ 
				if (updates.equals("login")){
					out.println("please login in first!");
					session.setAttribute("updatelike", "done");
				}
				else if (updates.equals("Fail")){
					out.println("Your like/unlike could not be completed.");
					session.setAttribute("updatelike", "done");
				}
				else if (updates.equals("Success")){
					query = "Select count(liker_email) from recipe_likes where likee_email ='" + creatoremail 
							+ "' and liker_email = '" + liker_email + "' and recipe_id = " + recipe_id;
					int count = 0;
					rs = statement.executeQuery(query);	
					while (rs.next()){
						count = rs.getInt(1);
					}
					if (count == 0){
						out.println("You have unliked this recipe! =(");
					}else{
						out.println("You have liked this recipe!");
					}
					session.setAttribute("updatelike", "done");
				}
			}
			
				//check for add comment
			updates = (String) session.getAttribute("addcomment");
			if (updates !=null){ 
				if (updates.equals("login")){
					out.println("Please login in first!");
					session.setAttribute("addcomment", "done");
				}
				else if (updates.equals("Failure")){
					out.println("Your comment could not be added.");
					session.setAttribute("addcomment", "done");
				}
				else if (updates.equals("Success")){
					out.println("You comment has been updated!");
					session.setAttribute("addcomment", "done");
				}
			}
				//check for delete comment 
			updates = (String) session.getAttribute("deletecomment");
			if (updates !=null){ 
				if (updates.equals("login")){
					out.println("Please login in first!");
					session.setAttribute("deletecomment", "done");
				}
				else if (updates.equals("Failure")){
					out.println("Your comment could not be deleted.");
					session.setAttribute("deletecomment", "done");
				}
				else if (updates.equals("Success")){
					out.println("You comment has been deleted! :(");
					session.setAttribute("deletecomment", "done");
				}
			}
			
			out.println ("</div>");
			
			
			//A.1: Recipe Name
			out.println("<div id=\"recipe_header\">");
			out.println("dishname: " + dishname); 
			out.println ("</div>");
			
			//A.2 Recipe Sub-Header: owner + time created + num likes + num comments + Like
			out.println("<div id=\"recipe_subheader\">");
			out.println("<span>");
			out.println("Created By: " + creatorname);
			out.println("</span");
			
			out.println("<span>"); 
			out.println("Created on: " + date);
			out.println("</span>");
			
			out.println("<span>"); 
			out.println("Likes: " + num_likes);
			out.println("</span>");
			
			out.println("<span>"); 
			out.println("Comments: " + num_comments);
			out.println("</span>");
			
			//like or unlike button 
			out.println("<span>"); 
			out.println("<form action=\"updatelike\"> ");
			query = "Select count(liker_email) from recipe_likes where likee_email ='" + creatoremail 
					+ "' and liker_email = '" + liker_email + "' and recipe_id = " + recipe_id;
			int count = 0;
			rs = statement.executeQuery(query);	
			while (rs.next()){
				count = rs.getInt(1);
			}
			if (count == 0){
			 	out.println("<button name=\"UpdateLike\" value=\"Like\" type=\"submit\">Like!</button>" );

			}else{
			 	out.println("<button name=\"UpdateLike\" value=\"Unlike\" type=\"submit\">Unlike</button>" );
			}
			out.println("</form>");
			out.println("</span>");
			
			out.println("</div>");
			
			//A.3 Ingredients
			out.println("<div id=\"recipe_details\">");
			out.println("<span>");
			out.println("<h3> Ingredients </h3>");
			query = "Select ingredient, measure, quantity from recipe_ingredients where recipe_id =" + recipe_id;
			LinkedList<String> ingredient = new LinkedList<String>();
			LinkedList<String> measure = new LinkedList<String>();
			LinkedList<Float> quantity = new LinkedList<Float>();
			rs = statement.executeQuery(query);	
			while (rs.next()){
				ingredient.add(rs.getString(1));
				measure.add(rs.getString(2));
				quantity.add(rs.getFloat(3));
			}
			
			for(int i = 0; i<ingredient.size(); i++){
				out.println(quantity.get(i));
				out.println(" " + measure.get(i));
				out.println(" of " + ingredient.get(i));
				out.println("<br />");
			}
			
			out.println("Serves: " + num_served);
			out.println("</span>");
			
			//A.4 Nutrients
			out.println("<span>");
			out.println("<h3> Nutritional Information </h3>");
			out.println("<br/>");
				out.println(nutrients[0] + "kcal");
				out.println(nutrients[1] + "protein");
				out.println(nutrients[2] + "fat");
				out.println(nutrients[3] + "carbohydrate");
				out.println(nutrients[4] + "fiber");
				out.println(nutrients[5] + "sugar");
				out.println(nutrients[6] + "calcium");
				out.println(nutrients[7] + "iorn");
				out.println(nutrients[8] + "magnesium");
				out.println(nutrients[9] + "potassium");
				out.println(nutrients[10] + "sodium");
				out.println(nutrients[11] + "zinc");
				out.println(nutrients[12] + "copper");
				out.println(nutrients[13] + "vitamin C");
			out.println("</span>");
			out.println("</div>");
			
			//A.5 Instructions
			out.println("<div id=\"recipe_details\">");
			out.println("<span>");
			out.println("<h3> Instructions </h3>");
			out.println(instructions);
			out.println("</span>");
			out.println("</div>");
			
			//A.6 Show Comments 
			out.println("<div id=\"recipe_details\">");
			out.println("<h3> Comments </h3>");
			
			//A.6.1 Get comments to show
			query = "Select comment_id, email, name, comment, time from recipe_comments where recipe_id =" + recipe_id + " order by time desc";
			LinkedList<String> name = new LinkedList<String>();
			LinkedList<String> comment = new LinkedList<String>();
			LinkedList<Integer> comment_id = new LinkedList<Integer>();
			LinkedList<String> commenter_email = new LinkedList<String>();
			LinkedList<String> comment_time = new LinkedList<String>();
			rs = statement.executeQuery(query);	
			while (rs.next()){
				comment_id.add(rs.getInt(1));
				commenter_email.add(rs.getString(2));
				name.add(rs.getString(3));
				comment.add(rs.getString(4));
				comment_time.add(rs.getString(5));
			}
			
			//A.6.2 Show Comments
			out.println("<form action=\"deletecomment\">");
			//session.setAttribute("comment_id", 0);
			for(int i = 0; i<name.size(); i++){
				out.println(name.get(i) + "says: (" + comment_time.get(i) + ")"); 
				out.println("<br />");
				out.println(comment.get(i));
				if (commenter_email.get(i).equals(liker_email)){
					//can delete this comment
					out.println("<button name=\"comment_id\" value= \"" + Integer.toString(comment_id.get(i)) + "\" type=\"submit\">Delete</button>");
					//out.println("<input type =\"submit\" class=\"btn small primary\" value=\"Delete\" /> ");
					//session.setAttribute("comment_id", comment_id.get(i));
				}
				out.println("<br />");
			}
			out.println("</form>");
			out.println("</div>"); 
			
			//A.7 Input Comments 
			out.println("<div id=\"recipe_details\">");
			out.println("<form action=\"addcomment\">");
			out.println("<h4> Comments on this recipe? Let us know! </h4>");
			out.println("<textarea cols=\"40\" rows=\"5\" name=\"comments\">");
			out.println("</textarea>");
			out.println("<br/>");
			out.println("<input type =\"submit\" class=\"btn large primary\" value=\"Comment!\" /> ");
			out.println("</form>");
			
			out.println("</div>"); 

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
