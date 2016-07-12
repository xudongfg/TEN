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
<body>
	<table style="width: 100%">
		<tr>
			<td style="width: 40%">Date of annotation</td>
			<td><div title="${'Date of annotation in YYYY-MM-DD format'}">
					<input type="text" name="tenLearningObjectAnnotationsBean.date"
						value='<fmt:formatDate pattern="yyyy-MM-dd" value="${date}" />' />
					?
				</div></td>
		</tr>
		<tr>
			<td>Format</td>
			<td><div
					title="${'The physical or digital manifestation of the resource'}">
					<input type="text" id="format"
						name="tenLearningObjectAnnotationsBean.format"
						value="${learningObjectDetailsBean.fileType}" /> ?
				</div></td>
		</tr>
		<!-- <tr><td>Identifier </td><td><div title="${'An unambiguous reference to the resource within a given context eg. the Digital Object Identifier (DOI)'}" ><input type="text" name="tenLearningObjectAnnotationsBean.identifier" />  ?</div></td></tr> -->
		<tr>
			<td width='50%'>Annotator</td>
			<td width='50%'><div
					title="${'Entity responsible for annotating the content'}">
					<input type="text"
						name="tenLearningObjectAnnotationsBean.annotator" readonly
						value="${sessionScope.user_details.user_name}" /> ?
				</div></td>
		</tr>
		<c:if test="${objectType == 'Image'}">
			<tr>
				<td>Type</td>
				<td><div title="${'The type of the content of the resource. '}">
						<select name="tenLearningObjectAnnotationsBean.imageType">
							<option value=""
								<c:if test="${tenLearningObjectAnnotationsBean.imageType == ''}">selected</c:if>></option>
							<option value="Photo"
								<c:if test="${tenLearningObjectAnnotationsBean.imageType == 'Photo'}">selected</c:if>>Photo</option>
							<option value="Illustration"
								<c:if test="${tenLearningObjectAnnotationsBean.imageType == 'Illustration'}">selected</c:if>>Illustration</option>
							<option value="Graph"
								<c:if test="${tenLearningObjectAnnotationsBean.imageType == 'Graph'}">selected</c:if>>Graph</option>
						</select> ?
					</div></td>
			</tr>
		</c:if>
		<c:if test="${objectType == 'Text'}">
			<tr>
				<td>Type</td>
				<td>
					<div title="${'The type of the content of the resource. '}">
						<select name="tenLearningObjectAnnotationsBean.textType">
							<option value=""
								<c:if test="${tenLearningObjectAnnotationsBean.textType == ''}">selected</c:if>></option>
							<option value="Definition"
								<c:if test="${tenLearningObjectAnnotationsBean.textType == 'Definition'}">selected</c:if>>Definition</option>
							<option value="Explanation"
								<c:if test="${tenLearningObjectAnnotationsBean.textType == 'Explanation'}">selected</c:if>>Explanation</option>
							<option value="Description"
								<c:if test="${tenLearningObjectAnnotationsBean.textType == 'Description'}">selected</c:if>>Description</option>
						</select> ?
					</div>
				</td>
			</tr>
		</c:if>
		<tr>
			<td width='50%'>Title</td>
			<td width='50%'><div
					title="${'The name given to the resource eg. The sound of music'}">
					<input type="text" name="tenLearningObjectAnnotationsBean.title" />
					?
				</div></td>
		</tr>
		<tr>
			<td>Keywords</td>
			<td><div
					title="${'The topic of the content of the resource typically keywords separated by semi-colons eg. Street, Picabo'}">
					<input type="text" name="tenLearningObjectAnnotationsBean.keywords" />
					?
				</div></td>
		</tr>
		<tr>
			<td>Description</td>
			<td><div
					title="${'An account of the content of the resource eg. an abstract, table of contents, reference to a graphical representation of content or a free-text account of the content.'}">
					<textarea name="tenLearningObjectAnnotationsBean.description"
						rows="4" cols="22"></textarea>
					?
				</div></td>
		</tr>
		<tr>
			<td>Language</td>
			<td>
				<div
					title="${'A language of the intellectual content of the resource eg. en-GB for English used in the United Kingdom'}">
					<select name="tenLearningObjectAnnotationsBean.language">
						<option value=""
							<c:if test="${tenLearningObjectAnnotationsBean.language == ''}">selected</c:if>></option>
						<option value="en-US"
							<c:if test="${tenLearningObjectAnnotationsBean.language == 'en-US'}">selected</c:if>>en-US</option>
						<option value="en-GB"
							<c:if test="${tenLearningObjectAnnotationsBean.language == 'en-GB'}">selected</c:if>>en-GB</option>
						<option value="Fr"
							<c:if test="${tenLearningObjectAnnotationsBean.language == 'Fr'}">selected</c:if>>Fr</option>
					</select> ?
				</div>
			</td>
		</tr>
		<tr>
			<td>Tribe</td>
			<td><div
					title="${'A reference to tribes referenced in the content'}">
					<input type="text" name="tenLearningObjectAnnotationsBean.tribe" />
					?
				</div></td>
		</tr>

		<tr>
			<td>Relevant Subjects</td>
			<td><div title="${'Other subject areas related to the content'}">
					<input type="text"
						name="tenLearningObjectAnnotationsBean.relevantSubjects" /> ?
				</div></td>
		</tr>

	</table>
</body>
</html>