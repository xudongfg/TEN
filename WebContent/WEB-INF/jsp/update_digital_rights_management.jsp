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

<title>Tribal Education Network update digital rights management
	page</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
</head>
<body
	style="background-image: url('${pageContext.request.contextPath}/images/background_student.jpg');background-attachment: fixed; background-position: right bottom;background-repeat:no-repeat">
	<form name="updateDigitalRightsManagementForm"
		id="updateDigitalRightsManagementForm"
		action="${pageContext.request.contextPath}/view/culturallearningobjects/digitalrightsmanagement.action"
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

			<%@include file="digital_rights_management.jsp"%>

			<input type="submit" value="Update Digital Rights Management" />
		</section>
		<input type="hidden" id="learningObjectId" name="learningObjectId"
			value="${learningObjectId}" /> <input type="hidden"
			id="learningObjectContentType" name="learningObjectContentType"
			value="${learningObjectContentType}" />
	</form>
	<script>
    var copyRightHolderCheckbox = document.getElementById("copyRightHolderCheckbox");
    onCopyRightHolderChange(copyRightHolderCheckbox);
    var storyProvidedCheckbox = document.getElementById("storyProvidedCheckbox");
    onStoryProvidedChange(storyProvidedCheckbox);
    if (!copyRightHolderCheckbox.checked) {
    	expandCollapse("idDivCopyRightHolder");
    }
    expandCollapse("idDivPublisher");
    expandCollapse("idDivContributor");
    if (storyProvidedCheckbox.checked) {
    	expandCollapse("idDivStoryProvider");
    }
	</script>
</body>
</html>