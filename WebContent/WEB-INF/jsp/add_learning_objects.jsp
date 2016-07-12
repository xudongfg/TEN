<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">

<title>Tribal Education Network Add Learning Objects Page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<style>
.nav_table table {
	padding-left: 800px;
	float: right;
}

.box {
	float: right;
	width: 500px;
}
</style>
</head>
<body>
	<form name="searchandlinkform" enctype="multipart/form-data"
		action="${pageContext.request.contextPath}/course/searchlearningobjects.action"
		method="post" onsubmit="checkBoxValidation()">
		<%@include file="include_header.jsp"%>

		<section>
			<table>
				<tr>
					<td><s:if test="hasActionErrors()">
							<s:actionerror />
						</s:if></td>
				</tr>
			</table>

			<table>
				<tr>
					<td><s:if test="hasActionMessages()">
							<s:actionmessage />
						</s:if></td>
				</tr>
			</table>

			<table style="width: 700px">
				<tr>
					<td width='70%'>Course Name</td>
					<td width='50%'><textarea id="courseName" cols="50" rows="3">
							<c:out value="${course.name}" />
						</textarea></td>
				</tr>
				<tr>
					<td width='70%'>Description</td>
					<td width='50%'><textarea id="courseDescription" cols="50"
							rows="10">
							<c:out value="${course.description}" />
						</textarea></td>
				</tr>
				<tr>
					<td>Course Objectives</td>
				</tr>
				<tr>
					<td colspan="100%" align="left"><c:choose>
							<c:when
								test="${course.objectives == null || fn:length(course.objectives) == 0}">
					No course objectives available to view
			 	</c:when>
							<c:otherwise>
								<ol start=1>
									<c:forEach items="${course.objectives}"
										var="courseObjectivesKVP">
										<c:set var="courseObjective"
											value="${courseObjectivesKVP.value}" />
										<li>[${courseObjective.id}] <c:out
												value="${courseObjective.description}" /></li>
									</c:forEach>
								</ol>
							</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td></td>
				</tr>
			</table>

			<%@include file="search_and_link_learning_objects.jsp"%>

		</section>
	</form>
</body>
</html>