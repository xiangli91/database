<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
  <%@page import="java.sql.*" %>
  <%@page import="java.io.*" %>
  <%@page import="java.servlet.*" %>
  <%@page import="com.mysql.jdbc.Connection" %>
  <%@page import="java.util.*" %>
  

<!DOCTYPE html>
<html>
<head>
   <link rel="stylesheet" href="mcb_stylesheet.css">
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <script type="text/javascript" src="./javascript/mcb.js" language="javascript"></script>
   <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" language="javascript"></script>
   <title>My Fridge</title>
</head>
   
<body style="font-family:Arial, Helvetica, sans-serif;font-size:medium;">
	
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
	
<div class="bodywrapper" align="center">		
	<div class="navpane">
		<div class="leftnav"><%--top left side--%></div>
		<div class="centernav">
			<a href="MemberHome.jsp">
			<div id="logo"></div></a>
			<div class="login">
				<form action="logout" method="get">
					<span class="welcomeuser"><specialtext>Welcome</specialtext>  <%=session.getAttribute("email")%></span><input type="submit" class="loginbtn" value="Logout"/>
				</form>
			</div>
		</div>
		<div class="rightnav"><%--top right side--%></div>
	</div>
	<div class="bodypane">
		<div class="leftpane"> <%--left side--%>.   </div>
		<div class="centerpane"><br />
			
			<div> <%--top centerpane--%>
			<table style="width: 800px">
				<tr>
					<td align="center" class="navbtn"><a href="AddRecipePage.jsp"><img src="images/site/spoon.png" height="80" width="100"/></a><br/>Add A Recipe</td>
					<td align="center" class="navbtn"><a href="MyRecipe.jsp"><img src="images/site/book.png" height="80" width="100"/></a><br/>Open My Cookbook</td>
					<td align="center" class="navbtn"><a href="MyFridge.jsp"><img src="images/site/fridge.png" height="80" width="100"/></a><br/>Update My Fridge</td>
				</tr>
			
			</table>
			<br/>
			<hr color="#CCCCCC"/>
			</div>
			<div> <%--bottom centerpane--%> 
				<div>
					<h1>My Fridge</h1>
				</div>
				<div>
										<% 
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
					%>		
						
						
					<%
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
					%>
					
					<%// Print what's in your fridge
					if (empty){ %>
						Your fridge is currently empty! Time to do some shopping!
					<%}	else{ %>
						<h3>Here is what is currently in your fridge: </h3>
						<TABLE BORDER=1>
						<TR>
						<TH> Ingredient </TH>
						</TR>
						<% for (int i=0; i<col1.size();i++){
						%>
							<TR>
								<TD> <%=col1.get(i)%>
								<form action ="updatefridge">
								<input type="submit" name="update_type" class="btn small primary" value="Delete" />
								<input type="hidden" name="ingredient" value=" <%=col1.get(i)%>"/>
								</form>
								</TD>
							</TR>	
						<%}%>
						</TABLE>
					<%}%>					
				</div>
			</div>
		</div>
		<div class="rightpane"> <%--right side--%> </div>
	</div>

	
	<%
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
	%>
	
	<br />	
	<form action="updatefridge">
	Ingredients:
	<select name="ingredient">
	<%for(int i =0; i < all_ingredients.size();i++){ %>
			<option value="<%=all_ingredients.get(i)%>"> <%=all_ingredients.get(i)%> </option>
	<%}%>
	</select>
	<br />
	<input type="hidden" name="update_type" value="Add" />
	<br />
	<input type ="submit" class="btn large primary" value="Add!!" />
	</form>
		
	
  
</body>
</html>