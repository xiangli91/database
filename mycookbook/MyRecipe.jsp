<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
  <%@page import="java.sql.*" %>
  <%@page import="java.io.*" %>
  <%@page import="java.servlet.*" %>
  <%@page import="com.mysql.jdbc.Connection" %>
  <%@page import="java.util.*" %>
  

<!DOCTYPE html>
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>My Recipes!</title>
</head>
   
<body>
	<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">	
	
	<% 
		Connection connection = null;
		//check if user has logged in or not
		session = request.getSession(true);
		Object result = session.getAttribute("login_check"); 
		Boolean logged_in = false; 
		if (result != null){ 
			logged_in = (Boolean) session.getAttribute("login_check");
		}
		if (!logged_in){	
			response.sendRedirect("/mycookbook/index.html");
		}
	%>
		
	<div class="page-header">"
	<h1>My very own cookbook</h1>
	<span>
	<form action="logout" class="pull-right">
	<input type ="submit" class="span3 btn small primary" value="Logout">
	</form>
	</span>
	</div>
	
	
	<!--Get what's in your receipe-->
	<%
		LinkedList<Integer> recipe_id = new LinkedList<Integer>();
		LinkedList<String> dishname = new LinkedList<String>();
		LinkedList<Integer> num_comments = new LinkedList<Integer>();
		LinkedList<Integer> num_likes = new LinkedList<Integer>();
		
		try{
			connection = (Connection) DriverManager.getConnection("jdbc:mysql://box289.bluehost.com/penniaac_llw", "penniaac_wll", "lixiang");
			String email = (String) session.getAttribute("email"); 
			Statement statement = connection.createStatement();
			String query = "Select recipe_id, dishname, num_comments, num_likes from recipes where email = '" + email +"'" + 
							"order by time desc";
			ResultSet rs = statement.executeQuery(query);		
			while (rs.next()){
				recipe_id.add(rs.getInt(1));
				dishname.add(rs.getString(2));
				num_comments.add(rs.getInt(3));
				num_likes.add(rs.getInt(4));
			}

		}catch (SQLException e ) {
				out.println(e); 		
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	%>

	<!-- Check for previous receipe updates! -->
	<%
		String updates = (String) session.getAttribute("updaterecipe");
		if (updates !=null){ 
			if (updates.equals("Failure")){
			%>
			Your receipe update could not be completed.
			<%
				session.setAttribute("updaterecipe", "done");
			}
			else if (updates.equals("Success")){
			%>
			You receipe update was successful!
			<%
				session.setAttribute("updaterecipe", "done");
			}
		}
	%>

	<!-- Print the Recipes -->
	
	<%
		if (dishname.size()<1){
		%>
		You have no recipes as of now! Time to do start creating your own cookbook today!
		<%
		}
			
		else{ 
		%>
			<form action="ViewRecipe.jsp">
			Here are you recipes!
			<TABLE BORDER=1>
				
			<TR>
			<TH> Dish </TH>
			<TH> Number of Likes </TH>
			<TH> Number of Comments </TH>
			</TR>
			<%
				// print the latest 10 recipes
				 for (int i=0; i<Math.min(10,dishname.size());i++){ 
				 %>
				<TR>
				<TD> <button name="recipe_id" value="<%=Integer.toString(recipe_id.get(i)) %>"  type="submit"> <%= dishname.get(i) %> </button></TD>
				<TD><%= num_likes.get(i) %> </TD>
			 	<TD><%= num_comments.get(i) %> </TD>
				</TR> 
				<%
				 }
				 %>
				 </TABLE>
				 </form>
				 <%}%>

			<br/>

	<form action="AddRecipePage.jsp">
	<input type ="submit" class="btn large primary" value="Add a Receipe">
	</form>

			
</body>
</html>

