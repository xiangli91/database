<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
  <%@page import="java.sql.*" %>
  <%@page import="java.io.*" %>
  <%@page import="java.servlet.*" %>
  <%@page import="com.mysql.jdbc.Connection" %>
  <%@page import="java.util.*" %>
   <%@page import="java.text.*" %>

<!DOCTYPE html>
<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <title>View Recipe</title>
</head>
   
<body>
	<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.4.0/bootstrap.min.css">	
	
	<% 
		Connection connection = null;
		session = request.getSession(true);
		String liker_email = (String) session.getAttribute("email"); //returns null if nothing is bound under this attribute. 
	%>

	
	<%
	// Get the parameters
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
	%>
	
	<div class="page-header">
	<h1><%=dishname%></h1>
	<span>
	<form action="logout" class="pull-right">
	<input type ="submit" class="span3 btn small primary" value="Logout" />
	</form>
	</span>
	</div>
	
	<!--Check for success of likes and comments!!-->
	<div>
		<% String updates = (String) session.getAttribute("updatelike");
			if (updates !=null){ 
				if (updates.equals("login")){
					out.println("please login in first!");
					session.setAttribute("updatelike", "done");
				}
				else if (updates.equals("Fail")){%>
					Your like/unlike could not be completed.
				<%	session.setAttribute("updatelike", "done");
				}
				else if (updates.equals("Success")){
					query = "Select count(liker_email) from recipe_likes where likee_email ='" + creatoremail 
							+ "' and liker_email = '" + liker_email + "' and recipe_id = " + recipe_id;
					int count = 0;
					rs = statement.executeQuery(query);	
					while (rs.next()){
						count = rs.getInt(1);
					}
					if (count == 0){%>
					You have unliked this recipe! =(
					<%}else{%>
					You have liked this recipe! =)
					<%}
					session.setAttribute("updatelike", "done");
				}
			}%>
			
		<%		//check for add comment
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
	%>
	</div>
	<!--A.1: Recipe Name-->
	<div id="recipe_header">
	<h3>dishname: <h3><%=dishname%>
	</div>
			
	<!--A.2 Recipe Sub-Header: owner + time created + num likes + num comments + Like-->
	<div id="recipe_subheader">
	<span>
	Created By: <%=creatorname%>
	</span>
	<span>
	| Created on: <%=date%>
	</span>
			
	<span>
	| Likes: <%=num_likes%>
	</span>
	
	</span>
	| Comments: <%=num_comments%>
	</span>
			
		<!--//like or unlike button -->
		</span>
		<form action="updatelike">
		<%query = "Select count(liker_email) from recipe_likes where likee_email ='" + creatoremail 
					+ "' and liker_email = '" + liker_email + "' and recipe_id = " + recipe_id;
			int count = 0;
			rs = statement.executeQuery(query);	
			while (rs.next()){
				count = rs.getInt(1);
			}
			if (count == 0){%>
			 	<button name="UpdateLike" value="Like" type="submit">Like!</button>
			<%}else{%>
			 	<button name="UpdateLike" value="Unlike" type="submit">Unlike</button>
			<%}%>
		</form>
		</span>
			
	</div>
	
	<!--A.3 Ingredients-->
	<div id="recipe_details">
	<span>
	<h3> Ingredients </h3>
	
	<%query = "Select ingredient, measure, quantity from recipe_ingredients where recipe_id =" + recipe_id;
			LinkedList<String> ingredient = new LinkedList<String>();
			LinkedList<String> measure = new LinkedList<String>();
			LinkedList<Float> quantity = new LinkedList<Float>();
			rs = statement.executeQuery(query);	
			while (rs.next()){
				ingredient.add(rs.getString(1));
				measure.add(rs.getString(2));
				quantity.add(rs.getFloat(3));
			}
			for(int i = 0; i<ingredient.size(); i++){ %>
				<%=quantity.get(i)%> <%=measure.get(i)%> of <%=ingredient.get(i)%>
				<br />
			<%}%>
	<%=recipe_id%>		
	<h3>Serves: </h3> <%=num_served%>
	</span>
			
	<!--A.4 Nutrients-->
	<span>
	<h3> Nutritional Information </h3>
	<br/>
	<%=nutrients[0]%> kcal
	<%=nutrients[1]%> protein
	<%=nutrients[2]%> fat
	<%=nutrients[3]%> carbohydrate
	<%=nutrients[4]%> fiber
	<%=nutrients[5]%> sugar
	<%=nutrients[6]%> calcium
	<%=nutrients[7]%> iorn
	<%=nutrients[8]%> magnesium
	<%=nutrients[9]%> potassium
	<%=nutrients[10]%> sodium
	<%=nutrients[11]%> zinc
	<%=nutrients[12]%> copper
	<%=nutrients[13]%> vitamin C
	</span>
	</div>
	
	<!--A.5 Instructions-->

	<div id="recipe_details">
	<span>
	<h3> Instructions </h3>
	<%=instructions%>
	</span>
	</div>
			
	<!--//A.6 Show Comments -->
	<div id="recipe_details">
	<h3> Comments </h3>
			
		<!--A.6.1 Get comments to show-->
			<%query = "Select comment_id, email, name, comment, time from recipe_comments where recipe_id =" + recipe_id + " order by time desc";
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
			}%>
			
		<!--A.6.2 Show Comments-->
			<form action="deletecomment">
			<%for(int i = 0; i<name.size(); i++){%>
				<%=name.get(i)%> says: (<%=comment_time.get(i)%>)
				<br />
				<%=comment.get(i)%>
				<%if (commenter_email.get(i).equals(liker_email)){
					//can delete this comment%>
					<button name="comment_id" value="<%=Integer.toString(comment_id.get(i))%>" type="submit">Delete</button>
				<%}%>
				<br />
			<%}%>
			</form>
	</div>
			
	<!--A.7 Input Comments -->
	<div id="recipe_details">
	<form action="addcomment">
	<h4> Comments on this recipe? Let us know! </h4>
	<textarea cols="40" rows="5" name="comments">
	</textarea>
	<br/>
	<input type ="submit" class="btn large primary" value="Comment!" /> 
	</form>
			
	</div>

	<%	}catch (SQLException e ) {
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
	
	
	
	
</body>
</html>
	
	
	
	
	
	
	
	
	
	
	