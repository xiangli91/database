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
	
	<div class="page-header">
	<h1>Add a new Recipe!</h1>
	<span>
	<form action="logout" class="pull-right">
	<input type ="submit" class="span3 btn small primary" value="Logout" />
	</form>
	</span>
	</div>
	
	<form name="recipe" action="addrecipe">
	
	<%
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
	%>
	
	Dish Name:
	<select name="dishname">
	<%for(int i = 0; i < dishnames.size();i++){ %>
		<option value="<%=dishnames.get(i)%>"> <%=dishnames.get(i)%> </option>
	<%}%>
	</select>
	<br />
	<br />
	
	<%//Form 2: number of people served%>
	Number of People Served:
	<select name="People">
	<%for(int i = 0; i <10;i++){ %>
		<option value="<%=i%>"> <%=i%> </option>
	<%}%>
	</select>
	<br />
	<br />
	
	
	<%//Form 3: ingredients - quantity and measure_type %>
	Ingredients:
	
	<%for(int i = 1; i<=5;i++){%>
	Ingredient: <input type="text" name="ingredient<%=i%>_name" />
	Quantity: <input type="int" name="ingredient<%=i%>__quantity" />
	Measure: <input type="text" name="ingredient<%=i%>__measure" />
	<br />
	<%}%>
			
	<%//Form 4: preparation%>
	Input your instructions here
	<textarea cols="40" rows="5" name="instructions">
	</textarea>
	<br />
	<br />
		
	<input type ="submit" class="btn large primary" value="Add this receipe!" /> 
	</form>

  
</body>
</html>