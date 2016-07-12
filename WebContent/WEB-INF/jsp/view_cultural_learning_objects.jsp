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

<title>Tribal Education Network View Cultural Learning Object
	Page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		function getLearningObjectDetails(id, contentType){
			document.getElementById("learningObjectId").value = id;
			document.getElementById("learningObjectContentType").value = contentType;
			document.updateDigitalRightsManagementForm.submit();
		}
	</script>
</head>
<body>
	<form name="updateDigitalRightsManagementForm"
		id="updateDigitalRightsManagementForm"
		action="${pageContext.request.contextPath}/view/culturallearningobjects/digitalrightsmanagement.action"
		method="get" enctype="multipart/form-data">
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
			<table border="1">
				<tr>
					<th colspan="100%" align="center"><b>List of Existing
							Cultural Learning Objects</b></th>
				</tr>
				<tr>
					<th nowrap="nowrap" align="center"><b>ID</b></th>
					<th nowrap="nowrap" align="center"><b>Cultural Learning
							Object</b></th>
				</tr>

				<c:choose>
					<c:when
						test="${(requestScope.learningObjects == null) || (fn:length(requestScope.learningObjects) == 0)}">
						<tr>
							<td>No cultural learning objects available to view</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${requestScope.learningObjects}"
							var="learningObject">
							<tr>
								<td><a href="#"
									onclick="getLearningObjectDetails('${learningObject.id}','${learningObject.contentType}');"
									style="color: blue"> <c:out
											value="${learningObject.referenceId}"></c:out>
								</a> <br> <c:out value="${learningObject.fileName}"></c:out></td>
								<td><c:choose>
										<c:when test="${learningObject.contentType == 'Image'}">
											<img height="200px"
												src="data:<c:out value='${learningObject.contentType}'></c:out>;base64,<c:out value='${learningObject.content}'/>" />
										</c:when>
										<c:when test="${learningObject.contentType == 'Audio'}">
											<audio controls>
												<source
													src="data:<c:out value='${learningObject.contentType}'></c:out>;base64,<c:out value='${learningObject.content}'/>"></source>
											</audio>
										</c:when>
										<c:when test="${learningObject.contentType == 'Video'}">
											<video height="200" width="auto" controls>
												<source
													src="data:<c:out value='${learningObject.contentType}'></c:out>;base64,<c:out value='${learningObject.content}'/>"></source>
											</video>
										</c:when>
										<c:when test="${learningObject.contentType == 'Text'}">
											<object>
												<embed height="200px"
													src="data:<c:out value='${learningObject.contentType}'></c:out>;base64,<c:out value='${learningObject.content}'/>"></embed>
											</object>
										</c:when>
										<c:otherwise>
								No preview available, unknown type
							</c:otherwise>
									</c:choose></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</table>
		</section>

		<input type="hidden" id="learningObjectId" name="learningObjectId"
			value="" /> <input type="hidden" id="learningObjectContentType"
			name="learningObjectContentType" value="" />

	</form>
</body>
</html>