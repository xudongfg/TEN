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

<title>Tribal Education Network Annotate Text main page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		function annotateText(id){
			document.annotateTextMainForm.submit();
		}
	</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_annotator.jpg');background-attachment: fixed; background-position: right; background-repeat:no-repeat">
	<form name="annotateTextForm" id="annotateTextMainForm"
		action="${pageContext.request.contextPath}/annotate/annotatetext.action"
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
					<td style="width: 100%"><h1>ANNOTATE TEXT</h1></td>
				</tr>
				<c:choose>
					<c:when
						test="${(actionType == 'display') && (requestScope.learningObjectDetailsBean == null)}">
						<tr>
							<td>Text cannot be found</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="objectType" scope="request" value="Text" />
						<c:if test="${(requestScope.learningObjectDetailsBean != null)}">
							<tr>
								<td>Text Preview</td>
							</tr>
							<tr>
								<td><c:choose>
										<c:when
											test="${fn:length(learningObjectDetailsBean.content) < 1500000}">
											<object>
												<embed width="100%" height="400"
													src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></embed>
											</object>
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
							<td><input type="submit" value="Annotate Text" /></td>
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