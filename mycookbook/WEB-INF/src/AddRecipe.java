

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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


/**
 * Servlet implementation class AddRecipe
 */
@WebServlet("/addrecipe")
public class AddRecipe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddRecipe() {
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
		
		//get the parameters
		String dishname = request.getParameter("dishname");
		float people = Float.valueOf(request.getParameter("People"));
		String instructions = request.getParameter("instructions");
		
		boolean next_ingredient = true; 
		int count = 0; 
		while (next_ingredient){
			if (request.getParameter("ingredient" + count + "_name") != null){
				count ++;
			}else{
				next_ingredient = false; 
			}
		}
		String[] ingredient_names = new String[count];
		float[] ingredient_quantity = new float[count];
		String[] ingredient_measure = new String[count];
		//i counts the number of ingredients used. note that it starts from 0 
		for(int i =0; i<count;i++){
			ingredient_names[i] = request.getParameter("ingredient" + count + "_name");
			ingredient_quantity[i] = Float.valueOf(request.getParameter("ingredient" + count + "_quantity")); 
			ingredient_measure[i] = request.getParameter("ingredient" + count + "_measure"); 
		}
		
		String email = (String) session.getAttribute("email"); 
		
		Date date = new Date();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		
		// Update Recipe condition: default: a failure
		session.setAttribute("updaterecipe", "Fail");

		
		// Set up the connection 
		Connection connection = null;
		PrintWriter out = response.getWriter();

		
		//A. Update the recipe table (other than nutrients)
		Integer recipe_id = 0;
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
			Statement statement = connection.createStatement();
			
			//find the largest recipeID, and add 1 to it:
			String query = "select max(recipe_id) from recipes";
			ResultSet rs = statement.executeQuery(query);	
			while(rs.next()){
				recipe_id = rs.getInt(1);
			}
			if(recipe_id == null){
				recipe_id = 1;
			}else{
				recipe_id ++;
			}
			
			String update = "Insert into recipes (recipe_id, dishname, email, instructions, num_comments, num_likes, num_served, time) values ("
							+ "'" + recipe_id + "', " 
							+ "'" + dishname + "', " 
							+ "'" + email + "', " 
							+ "'" + instructions + "', " 
							+ "0, "
							+ "0, "
							+ "'" + people + "', " 
							+ "'" + time + "')";
			
			int updatecheck = statement.executeUpdate(update);
			if (updatecheck != 1){
				//should only affect exactly 1 row!
				//delete the entire recipe, cascade. Then return to homepage. 
				String delete = "Delete from recipes where recipe_id = " + recipe_id ;
				statement.executeUpdate(delete);
				out.println("error in A");
				//response.sendRedirect("/mycookbook/myrecipe"); 
				return; 
			}
		}catch (SQLException e ) {
			out.println("SQL Exception!" + e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
					
		
		//B. Update the recipe_ingredients table
		try{
			connection = DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
			Statement statement = connection.createStatement();
			String update = "";
			
			for(int i = 0; i<count;i++){
				update = "Insert into recipe_ingredients (recipe_id, ingredient, measure, quantity) values (" 
						+ "'" + recipe_id + "', " 
						+ "'" + ingredient_names[i] + "'"
						+ "'" + ingredient_measure[i] + "'"
						+ "'" + ingredient_quantity[i] + "')";
				
				int updatecheck =  statement.executeUpdate(update);
				if (updatecheck != 1){
					//Should only affect exactly 1 row!!! 
					//delete the entire recipe, cascade. Then return to homepage. 
					String delete = "Delete from recipes where recipe_id = " + recipe_id ;
					statement.executeUpdate(delete);
					out.println("error in B" + ingredient_names[i]);
					//response.sendRedirect("/mycookbook/myrecipe"); 
					return; 
				}
			}
		}catch (SQLException e ) {
			out.println("SQL Exception!" + e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		//C. Update the recipe table (the nutrients part)
		
			  
			Float nutrients[][] = new Float[count][14]; //rows=count, number of ingredients. columns = 14 nutrients. 
			try{
				connection = DriverManager.getConnection("jdbc:mysql://localhost:8888/food", "myuser", "abcd");
				Statement statement = connection.createStatement();
				//C.1 Get the nutritional values of each ingredient
				String query = "";
				for(int i = 0; i<count;i++){
					
					//get the scale 
					query = "Select weight1, measure1, weight2, measure2 "
							+ "from ingredients where ingredient = '" 
							+ ingredient_names[i] + "'";
					ResultSet rs = statement.executeQuery(query);
					Float scale = (float) 0.0; 
					String measure1 = "";
					String measure2 = "";
					Float quantity1 = (float) 0.0;
					Float quantity2 = (float) 0.0;
					while (rs.next()){
						quantity1 = rs.getFloat(1);
						measure1 = rs.getString(2);
						quantity2 = rs.getFloat(3);
						measure2 = rs.getString(4);
					}
					if(ingredient_measure[i].equals(measure1)){
						scale = ingredient_quantity[i]*quantity1/100;
					}else{
						scale = ingredient_quantity[i]*quantity2/100;
					}
					
					//get the numbers for 100g 
					query = "Select kcal, protein, fat, carbohydrate, fiber, sugar, calcium, iron, magnesium, potassium, sodium, zinc, copper, vitamin_c"
							+ "from ingredients where ingredient = '" 
							+ ingredient_names[i] + "'";
					
					ResultSet rs1 = statement.executeQuery(query);
					
					//Fill up the nutrients table
					while (rs1.next()){
						for(int j=0;j<14;j++){
							nutrients[i][j] = (rs1.getFloat(j+1))*scale;
						}
					}
				}
				//C.2 Calculate the final values
				Float final_nutrients[] = new Float[14];
				for(int i = 0; i<14;i++){
					//initialize to 0
					final_nutrients[i] = (float) 0;
				}
				for(int i = 0; i <14;i++){
					for(int j = 0;j<count;j++){
						final_nutrients[i] = final_nutrients[i]+nutrients[j][i];
					}
				}
				//C.3 Update the Recipes Tables 
				String update = "Update recipes set "
						+ "kcal=" + final_nutrients[0] + ", "
						+ "protein=" +final_nutrients[1] + ", "
						+ "fat=" + final_nutrients[2] + ", "
						+ "carbohydrate=" + final_nutrients[3] + ", "
						+ "fiber=" + final_nutrients[4] + ", "
						+ "sugar=" + final_nutrients[5] + ", "
						+ "calcium=" + final_nutrients[6] + ", "
						+ "iron=" + final_nutrients[7] + ", "
						+ "magnesium=" + final_nutrients[8] + ", "
						+ "potassium=" + final_nutrients[9] + ", "
						+ "sodium=" + final_nutrients[10] + ", "
						+ "zinc=" + final_nutrients[11] + ", "
						+ "copper=" + final_nutrients[12] + ", "
						+ "vitamin_c=" + final_nutrients[13] 
						+ " where recipe_id=" + recipe_id;
				int updatecheck = statement.executeUpdate(update);
				if (updatecheck != 1){
					// should only affect 1 row!!!
					//delete the entire recipe, cascade. Then return to homepage. 
					String delete = "Delete from recipes where recipe_id = " + recipe_id ;
					statement.executeUpdate(delete);
					out.println("error in C");
					// response.sendRedirect("/mycookbook/myrecipe"); 
					return; 
				}
			}catch (SQLException e ) {
				out.println("SQL Exception!" + e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		// Everything was updated successfully. 	
		session.setAttribute("updatefridge","Success");
		out.println("success!");
		response.sendRedirect("/mycookbook/myrecipe"); 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
