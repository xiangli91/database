<%@page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.sql.*" %>
<%@page import="java.io.*" %>
<%@page import="java.servlet.*" %>
<%@page import="com.mysql.jdbc.Connection" %>
<%@page import="java.util.*" %>
<%@page import="java.text.*" %>

<%@ page import="java.io.IOException" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="javax.servlet.* %>



<html>
<head>
<link rel="stylesheet" href="mcb_stylesheet.css">
<script type="text/javascript" src="./mcb.js" language="javascript"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js" language="javascript"></script>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<title>Home</title>
</head>

<body>
	<% 
	Object result = session.getAttribute("login_check");
	Boolean logged_in = false;
	if (result != null){
		logged_in = (Boolean) session.getAttribute("login_check");
	}
	
	if (!logged_in){
		response.sendRedirect("/index2.html");
	}
	%>
</body>
</html>
