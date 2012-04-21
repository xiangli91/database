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
	
	<br><strong> Search for receipes, add a receipe, or edit your fridge!</br>
			
	</br>
			
	<!--Search-->
	
	<form action=\"search\"> 
	<input type =\"submit\" class=\"btn large primary\" value=\"Search for a Receipe\" />
			
	</form>
	<!--Add Receipe--->
	<form action=\"AddRecipePage.jsp\"> 
				"<input type =\"submit\" class=\"btn large primary\" value=\"Add a Receipe\" /> 
				"</form>
			
	<!--My Receipe-->
	<form action=\"MyRecipe.jsp\"> 
	<input type =\"submit\" class=\"btn large primary\" value=\"Open My Cookbook!\" /> 
	</form>
				
			
	<!--/My Fridge-->
	<form action=\"MyFridge.jsp\"> 
	<input type =\"submit\" class=\"btn large primary\" value=\"Update my Fridge\" /> 
	</form>

</body>
</html>