<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">

<title>Tribal Education Network View courses main page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		function getCourseDetails(id, name){
			document.getElementById("courseId").value = id;
			document.getElementById("courseName").value = name;
			document.viewCoursesMainForm.submit();
		}
	</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_student.jpg');background-attachment: fixed; background-position: right bottom;background-repeat:no-repeat">
	<form name="viewCoursesMainForm" id="viewCoursesMainForm"
		action="${pageContext.request.contextPath}/view/coursedetails.action"
		method="post" enctype="multipart/form-data">
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

			<table class="nav_table" style="width: 700px">
				<!--  <tr><td style="width:50%">LIST OF COURSES</td></tr>  -->
				<table border="1">
					<tr>
						<th colspan="2" align="center">List of Existing Courses</th>
					</tr>

					<c:choose>
						<c:when
							test="${(requestScope.listCourses == null) || (fn:length(requestScope.listCourses) == 0)}">
							<tr>
								<td>No courses available to view</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${requestScope.listCourses}" var="course">
								<tr>
									<td><a href="#"
										onclick="getCourseDetails('${course.id}','${course.name}');"
										style="color: blue; display: block"><c:out
												value="${course.name}"></c:out></a></td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>

				</table>


				</section>
				<input type="hidden" id="courseId" name="courseId" value="" />
				<input type="hidden" id="courseName" name="courseName" value="" />
				<input type="hidden" id="actionName" name="actionName" value="view" />
				</form>
</body>
</html>