<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html>
<head>
<meta charset="UTF-8">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">

<title>Tribal Education Network Student profile page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_student.jpg');background-attachment: fixed; background-position: right bottom;background-repeat:no-repeat">
	<form
		action="${pageContext.request.contextPath}/view/profiledetails.action"
		method="post" enctype="multipart/form-data">
		<header>
			<%@include file="include_header.jsp"%>
		</header>
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

			<table style="width: 700px;">
				<tr>
					<td width='50%'>Tribe Name</td>
					<td width='50%'><div
							title="${'The tribe to which you belong'}">
							<input type="text" id="tribe" name="studentAnnotationsBean.tribe"
								value="${studentAnnotationsBean.tribe}" /> ?
						</div></td>
				</tr>
				<tr>
					<td width='50%'>Language</td>
					<td><div
							title="${'The preferred language for learning material'}">
							<select name="studentAnnotationsBean.preferredLanguage">
								<option value=""
									<c:if test="${studentAnnotationsBean.preferredLanguage == ''}">selected</c:if>></option>
								<option value="en-US"
									<c:if test="${studentAnnotationsBean.preferredLanguage == 'en-US'}">selected</c:if>>en-US</option>
								<option value="en-GB"
									<c:if test="${studentAnnotationsBean.preferredLanguage == 'en-GB'}">selected</c:if>>en-GB</option>
								<option value="Fr"
									<c:if test="${studentAnnotationsBean.preferredLanguage == 'Fr'}">selected</c:if>>Fr</option>
							</select> ?
						</div></td>
				</tr>
				<tr>
					<td>Preferred Learning Object Type</td>
					<td><div title="${'The preferred type of learning material'}">
							<select name="studentAnnotationsBean.preferredLearningObjectType">
								<option value=""
									<c:if test="${studentAnnotationsBean.preferredLearningObjectType == ''}">selected</c:if>></option>
								<option value="Image"
									<c:if test="${studentAnnotationsBean.preferredLearningObjectType == 'Image'}">selected</c:if>>Image</option>
								<option value="Audio"
									<c:if test="${studentAnnotationsBean.preferredLearningObjectType == 'Audio'}">selected</c:if>>Audio</option>
								<option value="Video"
									<c:if test="${studentAnnotationsBean.preferredLearningObjectType == 'Video'}">selected</c:if>>Video</option>
								<option value="Text"
									<c:if test="${studentAnnotationsBean.preferredLearningObjectType == 'Text'}">selected</c:if>>Text</option>
							</select> ?
						</div></td>
				</tr>
				<tr>
					<td>Preferred Text content type</td>
					<td><div title="${'The preferred type of text material'}">
							<select name="studentAnnotationsBean.preferredTextContent">
								<option value=""
									<c:if test="${studentAnnotationsBean.preferredTextContent == ''}">selected</c:if>></option>
								<option value="Definition"
									<c:if test="${studentAnnotationsBean.preferredTextContent == 'Definition'}">selected</c:if>>Definition</option>
								<option value="Explanation"
									<c:if test="${studentAnnotationsBean.preferredTextContent == 'Explanation'}">selected</c:if>>Explanation</option>
								<option value="Description"
									<c:if test="${studentAnnotationsBean.preferredTextContent == 'Description'}">selected</c:if>>Description</option>
							</select> ?
						</div></td>
				</tr>
				<tr>
					<td>Preferred Image content type</td>
					<td><div title="${'The preferred type of image'}">
							<select name="studentAnnotationsBean.preferredImageContent">
								<option value=""
									<c:if test="${studentAnnotationsBean.preferredImageContent == ''}">selected</c:if>></option>
								<option value="Photo"
									<c:if test="${studentAnnotationsBean.preferredImageContent == 'Photo'}">selected</c:if>>Photo</option>
								<option value="Illustration"
									<c:if test="${studentAnnotationsBean.preferredImageContent == 'Illustration'}">selected</c:if>>Illustration</option>
								<option value="Graph"
									<c:if test="${studentAnnotationsBean.preferredImageContent == 'Graph'}">selected</c:if>>Graph</option>
							</select> ?
						</div></td>
				</tr>
				<tr>
					<td><input type="submit" value="Update Profile" /></td>
				</tr>
			</table>
		</section>
	</form>
</body>
</html>