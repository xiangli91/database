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
   <title>Home</title>
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
					<h1>Search for your Recipe</h1>
				</div>
				<div>
					<div id="searchBoxOne">
		  				<form action="?" method="get">
		    				<input class="search" type="text" />
		    				<input class="submit" type="submit" value="Search" />
		    				<br/>
		    				<span><input type ="radio" name="byingredient" value="by Ingredient"/><specialtext>by Ingredient </specialtext></span>
							<span><input type ="radio" name="byname" value="by Name"/><specialtext>by Name </specialtext></span>
	  					</form>
					</div>
				</div>
				<div>
					<br/>
					<a class="advsearchbtn" onclick="toggleContent('.info');">Advanced Search</a>
				</div>
			</div>
		</div>
		<div class="rightpane"> <%--right side--%> </div>
	</div>
	
	<div class="info" style="display:none;">
		<div class="advancedsearchinfo" style="left: 142px; top: 5px">
			<div class="leftcenterinfo">
				<br/>
				<table>
				<tr>
					<td><span class="adv"><specialtext>Calories: </specialtext><input type="text" name="kcal"/></span></td>
					<td><span class="adv"><specialtext>Protein: </specialtext><input type="text" name="protein"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Fat: </specialtext><input type="text" name="fat"/></span></td>
					<td><span class="adv"><specialtext>Carbohydrate: <input type="text" name="carbohydrate"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Fiber: </specialtext><input type="text" name="fiber"/></span></td>
					<td><span class="adv"><specialtext>Sugar: </specialtext><input type="text" name="sugar"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Calcium: </specialtext><input type="text" name="calcium"/></span></td>
					<td><span class="adv"><specialtext>Iron: </specialtext><input type="text" name="iron"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Magnesium: </specialtext><input type="text" name="magnesium"/></span></td>
					<td><span class="adv"><specialtext>Potassium: </specialtext><input type="text" name="potassium"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Sodium: </specialtext><input type="text" name="sodium"/></span></td>
					<td><span class="adv"><specialtext>Zinc: </specialtext><input type="text" name="zinc"/></span></td>
				</tr>
				<tr>
					<td><span class="adv"><specialtext>Copper: </specialtext><input type="text" name="copper"/></span></td>
					<td><span class="adv"><specialtext>Vitamin C: </specialtext><input type="text" name="vitamin_c"></span></td>
				</tr>
				</table>
				
				</br>
	
				<specialtext>Limit to My Fridge: </specialtext><input type="checkbox" name="myfridge"/><input class="advsearch" type="submit" name="advanced" value="Advanced Search"/>

			
			</div>
			
			
			
			<div class="rightcenterinfo">
				<h4>Search Tips</h4>
			</div>
		</div>
	</div>
	
	
	<div class="bottom">
		<div class="centerbottom" style="left: 141px; top: 2px"> 
			<span class="misc">
				<misc>myCookbook</misc>
				<li style="list-style-type:none;margin:5px;"><misc2>Home</misc2></li>
				<li style="list-style-type:none;margin:5px"><misc2>Blog</misc2></li>
				<li style="list-style-type:none;margin:5px"><misc2>Mobile</misc2></li>
			</span>
			<span class="misc">
				<misc>About</misc>
				<li style="list-style-type:none;margin:5px;"><misc2>Team</misc2></li>
				<li style="list-style-type:none;margin:5px"><misc2>Press</misc2></li>
				<li style="list-style-type:none;margin:5px"><misc2>Jobs</misc2></li>
			</span>
			<span class="misc">
				<misc>Support</misc>
				<li style="list-style-type:none;margin:5px;"><misc2>FAQs</misc2></li>
				<li style="list-style-type:none;margin:5px"><misc2>Contact Us</misc2></li>
			</span>
			<div class="findus">
				<misc>Find us on: </misc>
				<div>
					
					<span style="margin-right:10px"><a href="http://www.facebook.com/"><img  alt="" height="40" src="images/site/facebook-button.png" width="40" /></a></span>
					<span><a href="http://www.twitter.com/"><img alt="" src="images/site/twitter-button.png" height="40" width="40"/></a></span>
				</div>
			</div>
		</div>
	</div>

</div>


</body>

</html>