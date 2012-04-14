

import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;




@WebServlet("/addmember")
public class Add_Member extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Add_Member() {
        super();
        // TODO Auto-generated constructor stub
    }
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		Connection connection = null;
		PrintWriter out = response.getWriter();
					
		try{
			connection = DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			if(email== "" || name == "" || password== ""){
				throw new SQLException(); 
			}
			Statement statement = connection.createStatement();
			String insertion = "INSERT INTO members (email, name, password) VALUES ('" 
							+ email +"', '" + name + "', '" + password + "')" ;
			
			int count =	statement.executeUpdate(insertion);
		
			
			if (count == 1){
				out.println("Congratulations " + name + "! You have successfully joined as a member!");
				out.println("Start adding to your cookbook today!");
			
			}
			else {
				out.println("Sorry " + name+ ". There was an error. Please make sure that you have filled in all fields");
				out.println("If you are a member already, please login"); 
			}

		}catch (SQLException e ) {
			
			if (email == "" || name == "" || password== "") {
				out.println("Please fill in all fills"); 
			}
			else if (e.getMessage().contains("Duplicate")){
				out.println("Sorry, there is an account associated with this email already"); 
			}
			else {
				out.println(e); 
			}
			
			
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
