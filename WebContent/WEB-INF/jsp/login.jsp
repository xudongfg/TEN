<!DOCTYPE html>
<html>
<head>
<head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/page_layout.css">
<title>Tribal Education Network Login</title>
<style>
body.body_class {
	background-attachment: fixed;
	background-position: center;
	background-image: url('/Ten/images/background_login.jpg');
}

th.labels {
	font-size: large;
	color: black;
}

th.headers {
	font-size: xx-large;
	color: black;
	align: right;
	font-family: sans-serif;
}
</style>
</head>
<body class="body_class">
	<form method="POST" action="j_security_check">
		<table style="margin: auto; margin-top: 280px;">
			<tr>
				<th colspan="3" class="headers">Tribal Education Network</th>
			</tr>
			<tr>
				<td><br></td>
			</tr>
			<tr>
				<th align="right" class="labels">Username:</th>
				<td align="left"><input type="text" name="j_username"></td>
			</tr>
			<tr>
				<th align="right" class="labels">Password:</th>
				<td align="left"><input type="password" name="j_password"></td>
			</tr>
			<tr>
				<td><br></td>
			</tr>
		</table>
		<table style="margin: auto;">
			<tr>
				<td align="right"><input type="submit" value="Log In"></td>
				<td align="left"><input type="reset"></td>
			</tr>
		</table>
		<input type="hidden" name="originalUrl" />
	</form>

	<!--  <form method="POST" action="${pageContext.request.contextPath}/login/signup.action">
		<table style="margin:auto">
			<tr><td align="Right"><input type="submit" value="New User Signup"></td></tr>
		</table>
	</form>-->

	<table style="margin: auto;">
		<tr>
			<td><a href="${pageContext.request.contextPath}/login/signup"
				style="color: blue">New User Signup</a></td>
		</tr>
	</table>
</body>
</html>