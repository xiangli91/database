

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import java.io.*;
import java.sql.*;
import java.util.LinkedList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class MyFridge
 */
@WebServlet("/myfridge")
public class MyFridge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyFridge() {
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
			out.println("<title>My Fridge! </title></head>");
			out.println("<body>");  
		
			out.println("<div class=\"page-header\">");
			out.println("<h1>Here is my Fridge</h1>");
			out.println("<span>");
			out.println("<form action=\"logout\" class=\"pull-right\">");
			out.println("<input type =\"submit\" class=\"span3 btn small primary\" value=\"Logout\" />");
			out.println("</form>");
			out.println("</span>");
			out.println("</div>");
			
			//Get what's in your fridge
			LinkedList<String> col1 = new LinkedList<String>();
			LinkedList<String> all_ingredients = new LinkedList<String>();
			Boolean empty = true;
			try{
				connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
				String email = (String) session.getAttribute("email"); 
				Statement statement = connection.createStatement();
				String query = "Select ingredient from fridge where email = '" + email +"'" ;
				ResultSet rs = statement.executeQuery(query);		
				while (rs.next()){
					col1.add(rs.getString(1));
					empty = false; 
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
			
			// Check for previous updates!
			String updates = (String) session.getAttribute("updatefridge");
			if (updates !=null){ 
				if (updates.equals("Failure")){
					out.println("Your update could not be completed.");
					session.setAttribute("updatefridge", "done");
				}
				else if (updates.equals("Success")){
					out.println("You have updated your fridge successfully!");
					session.setAttribute("updatefridge", "done");
				}
			}
			
			// Print what's in your fridge
			
			if (empty){
				out.println("Your fridge is currently empty! Time to do some shopping!");
			}
			else{
				out.println("Here is what is currently in your fridge: ");
				out.println("<TABLE BORDER=1>");
				
				//print the header
				out.println("<TR>");
					out.println("<TH>" + "Ingredient" + "</TH>");
				out.println("</TR>");
				
				// print the values
				 for (int i=0; i<col1.size();i++){
					 out.println("<TR>");
					    out.println("<TD>" + col1.get(i) + "</TD>");
					  out.println("</TR>"); 
				 }
				 out.println("</TABLE>");
			}
			
			
			//Update your fridge
			
			//prepare the drop down menu
			try{
				connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
				String email = (String) session.getAttribute("email"); 
				Statement statement = connection.createStatement();
				String query = "Select ingredient from ingredients" ;
				ResultSet rs = statement.executeQuery(query);	
				while (rs.next()){
					all_ingredients.add(rs.getString(1));
					empty = false; 
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
		
			
			out.println("<br />");		

			out.println("<form action=\"updatefridge\"> ");
			out.println("Ingredient:");
			
			out.println("<select name=\"ingredient\">");
			for(int i =0; i < all_ingredients.size();i++){
				out.println("<option value=\"" + all_ingredients.get(i) + "\">" + all_ingredients.get(i) + "</option>");
			}
			out.println("</select>");
			
			out.println("<br />");
			out.println("<input type=\"radio\" name=\"update_type\" class=\"btn small primary\" value=\"Add\" /> Add ");
			out.println("<input type=\"radio\" name=\"update_type\" class=\"btn small primary\" value=\"Delete\" /> Delete");
			out.println("<br />");
			out.println("<input type =\"submit\" class=\"btn large primary\" value=\"Update!\" /> ");
			// what if none is pressed?
			
			
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
