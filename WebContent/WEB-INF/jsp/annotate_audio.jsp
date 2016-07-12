<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<meta charset="UTF-8">

<!-- Stylesheets -->
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/page_layout.css">

<title>Tribal Education Network Annotate Audio main page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		function annotateAudio(id){
			document.annotateAudioMainForm.submit();
		}
	</script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_annotator.jpg');background-attachment: fixed; background-position: right; background-repeat:no-repeat">
	<form name="annotateAudioForm" id="annotateAudioMainForm"
		action="${pageContext.request.contextPath}/annotate/annotateaudio.action"
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
					<td><h1>ANNOTATE AUDIO</h1></td>
				</tr>
				<c:choose>
					<c:when
						test="${(actionType == 'display') && (requestScope.learningObjectDetailsBean == null)}">
						<tr>
							<td>Audio cannot be found</td>
							<td></td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:set var="objectType" scope="request" value="Audio" />
						<c:if test="${(requestScope.learningObjectDetailsBean != null)}">
							<tr>
								<td>Audio Preview</td>
								<td></td>
							</tr>
							<tr>
								<td><c:choose>
										<c:when
											test="${fn:length(learningObjectDetailsBean.content) < 1024000}">
											<audio controls>
												<source
													src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></source>
											</audio>
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
							<td colspan="2">
								<div id="annotations_div">
									<%@include file="ten_annotations.jsp"%>
								</div>
							</td>
						</tr>
						<tr>
							<td><input type="submit" value="Annotate Audio" /></td>
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