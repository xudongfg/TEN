<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">
<title>Tribal Education Network Annotate Image main page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		function annotateImage(id){
			document.annotateImageMainForm.submit();
		}
	</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_annotator.jpg');background-attachment: fixed; background-position: right; background-repeat:no-repeat">
	<form name="annotateImageForm" id="annotateImageMainForm"
		action="${pageContext.request.contextPath}/annotate/annotateimage.action"
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

			<table style="width: 700px">
				<tr>
					<td><h1>ANNOTATE IMAGE</h1></td>
				</tr>
				<c:choose>
					<c:when
						test="${(actionType == 'display') && (requestScope.learningObjectDetailsBean == null)}">
						<tr>
							<td>Image cannot be found</td>
							<td></td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="objectType" scope="request" value="Image" />
						<c:if test="${(requestScope.learningObjectDetailsBean != null)}">
							<tr>
								<td>Image Preview</td>
							</tr>
							<tr>
								<td><c:choose>
										<c:when
											test="${fn:length(learningObjectDetailsBean.content) < 1024000000000}">
											<img
												src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"
												width="200" height="200" />
										</c:when>
										<c:otherwise>
											<p style="color: red">
												<b>Preview not available as the file size is large</b>
											</p>
										</c:otherwise>
									</c:choose></td>
							</tr>
						</c:if>
						<tr>
							<td>
								<div id="annotations_div">
									<%@include file="ten_annotations.jsp"%>
								</div>
							</td>
						</tr>
						<tr>
							<td><input type="submit" value="Annotate Image" /></td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</section>
		<input type="hidden" id="learningObjectId" name="learningObjectId"
			value="${learningObjectDetailsBean.id}" /> <input type="hidden"
			id="actionType" name="actionType" value="annotate" />
	</form>
</body>
</html>