<!DOCTYPE html>
<html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">
<title>Welcome to Tribal Education Network</title>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_main.jpg');background-attachment: fixed; background-position:right center; background-repeat:no-repeat">
	<form>
		<header>
			<table>
				<tr>
					<td width="80%"><img
						src="${pageContext.request.contextPath}/images/TEN_icon.png"
						alt="TEN Logo" width="60px" height="60px" style="float: left;">
					<h1 style="float: left; margin: 10px;">Tribal Education
							Network</h1></td>
					<td></td>
					<td width="20%" align="right"><h3>
							<a href="${pageContext.request.contextPath}/logout">Logout</a>
						</h3></td>
				</tr>
			</table>
		</header>
		<section>
			<!-- Upload links intaker role -->
			<c:if
				test="${(sessionScope.user_details.userIntaker == true) || (sessionScope.user_details.userAnnotator == true)}">
				<table>
					<tr>
						<td>
							<div id="upload_links_div">
								<%@include file="include_upload_links.jsp"%>
							</div>
						</td>
					</tr>
					<tr></tr>
				</table>
				<br>
			</c:if>

			<!-- Annotate links Annotator role -->
			<c:if test="${sessionScope.user_details.userAnnotator == true}">
				<table>
					<tr>
						<td>
							<div id="annotate_links_div">
								<%@include file="include_annotate_links.jsp"%>
							</div>
						</td>
					</tr>
					<tr></tr>
				</table>
				<br>
			</c:if>

			<!--Course creation links creator role -->
			<c:if test="${sessionScope.user_details.userCreator == true}">
				<table>
					<tr>
						<td>
							<div id="course_links_div">
								<%@include file="include_course_links.jsp"%>
							</div>
						</td>
					</tr>
					<tr></tr>
				</table>
				<br>
			</c:if>

			<!--Course view links student role -->
			<c:if test="${sessionScope.user_details.userStudent == true}">
				<table class="nav_table">
					<tr>
						<td>
							<div id="course_links_div">
								<%@include file="include_view_links.jsp"%>
							</div>
						</td>
					</tr>
					<tr></tr>
				</table>
			</c:if>
		</section>
	</form>
</body>
</html>