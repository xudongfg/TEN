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

<title>Tribal Education Network Create Course Page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>
		
	</script>
</head>
<body>
	<form
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

			<table style="width: 800px">
				<tr>
					<td width='70%'>Course Name</td>
					<td width='50%'><input type="text" id="courseName"
						name="courseName" value="${courseName}" readonly /></td>
				</tr>
				<tr>
					<td width='70%'>Description</td>
					<td width='50%'><input type="text" id="courseDescription"
						name="courseDescription" value="${courseDescription}" readonly /></td>
				</tr>
				<tr>
					<td width='70%'>Prerequisites</td>
					<td width='50%'><input type="text" id="coursePrerequisites"
						name="coursePrerequisites" value="${coursePrerequisites}" readonly /></td>
				</tr>
				<tr>
					<td width='30%'><input type="submit" value="Edit" /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
				</tr>
			</table>
			<br>
			<!-- RECOMMENDED LEARNING OBJECTS -->
			<table style="width: 800px">
				<c:set var="learningObjectDetailsBean" scope="session"></c:set>
				<c:if test="${requestScope.mapLearningObjects != null}">
					<tr>
						<td width="80%"><h1>
								<c:out value="${'RECOMMENDED LEARNING OBJECTS'}" />
							</h1></td>
					</tr>
					<c:choose>
						<c:when test="${recommendedLearningObjects == 0}">
							<tr>
								<td colspan=2><h2>
										<c:out
											value="${'No recommended learning objects other than the default ones found'}" />
									</h2></td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="learningObjectMapEntry"
								items="${requestScope.mapLearningObjects}">
								<c:set var="learningObjectsList"
									value="${learningObjectMapEntry.value}" />
								<!-- Display image -->
								<c:if test="${learningObjectMapEntry.key == 'Image'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Image Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1024000}">
														<img
															src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"
															width="auto" height="auto" />
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Audio -->
								<c:if test="${learningObjectMapEntry.key == 'Audio'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Audio Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1024000}">
														<audio controls>
															<source
																src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"></source>
														</audio>
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Video -->
								<c:if test="${learningObjectMapEntry.key == 'Video'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}" varStatus="loop">
										<tr>
											<td><h2>Video Name:</h2></td>
											<td><h2>
													<a
														href="${pageContext.request.contextPath}/view/coursedetails/videopresentation?fileName=${learningObjectDetailsBean.value.fileName}">
														<s:set var="learningObjectDetailsBean" scope="session" />
														<c:out value="${learningObjectDetailsBean.value.fileName}" />

													</a>
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1024000 }">
														<!-- < 1024000 }" -->
														<video width="auto" height="auto" controls>
															<source
																src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"></source>
														</video>
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Text -->
								<c:if test="${learningObjectMapEntry.key == 'Text'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Document Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td colspan="2"><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1500000}">
														<object>
															<embed width="100%" height="400px"
																src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"></embed>
														</object>
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</c:if>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td></td>
				</tr>

				<!-- DEFAULT LEARNING OBJECTS -->
				<c:if test="${requestScope.mapDefaultLearningObjects != null}">
					<tr>
						<td width="80%"><h1>
								<c:out value="${'DEFAULT LEARNING OBJECTS'}" />
							</h1></td>
					</tr>
					<c:choose>
						<c:when test="${defaultLearningObjects == 0 }">
							<tr>
								<td colspan=2><h2>
										<c:out
											value="${'No default learning objects other than the recommended ones found'}" />
									</h2></td>
								<td></td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach var="learningObjectMapEntry"
								items="${requestScope.mapDefaultLearningObjects}">
								<c:set var="learningObjectsList"
									value="${learningObjectMapEntry.value}" />
								<!-- Display image -->
								<c:if test="${learningObjectMapEntry.key == 'Image'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Image Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1024000}">
														<img
															src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"
															width="auto" height="auto" />
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Audio -->
								<c:if test="${learningObjectMapEntry.key == 'Audio'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Audio Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1024000}">
														<audio controls>
															<source
																src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"></source>
														</audio>
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Video -->
								<c:if test="${learningObjectMapEntry.key == 'Video'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}" varStatus="loop">
										<tr>
											<td><h2>Video Name:</h2></td>
											<td><h2>
													<a
														href="${pageContext.request.contextPath}/view/coursedetails/videopresentation?fileName=${learningObjectDetailsBean.value.fileName}">
														<s:set var="learningObjectDetailsBean" scope="session" />
														<c:out value="${learningObjectDetailsBean.value.fileName}" />
													</a>
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td></td>
										</tr>
									</c:forEach>
								</c:if>

								<!-- Display Text -->
								<c:if test="${learningObjectMapEntry.key == 'Text'}">
									<c:forEach var="learningObjectDetailsBean"
										items="${learningObjectsList}">
										<tr>
											<td><h2>Document Name:</h2></td>
											<td><h2>
													<c:out value="${learningObjectDetailsBean.value.fileName}" />
												</h2></td>
										</tr>
										<tr>
											<td></td>
										</tr>
										<tr>
											<td colspan=2><c:choose>
													<c:when
														test="${fn:length(learningObjectDetailsBean.value.content) < 1500000}">
														<object>
															<embed width="100%" height="400px"
																src="data:<c:out value='${learningObjectDetailsBean.value.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.value.content}'/>"></embed>
														</object>
													</c:when>
													<c:otherwise>
														<p style="color: red">
															<b>Preview not available as the file size is large</b>
														</p>
													</c:otherwise>
												</c:choose></td>
										</tr>
									</c:forEach>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</c:if>
			</table>
		</section>
		<input type="hidden" name="courseId" id="courseId" value="${courseId}" />
		<input type="hidden" name="actionName" id="actionName" value="search" />
	</form>
</body>
</html>