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
	<table style="width: 700px;">
		<tr>
			<td colspan="100%" align="left"><header>Linked Learning
					Objects</header></td>
		</tr>
		<c:choose>
			<c:when
				test="${course.learningObjectLinks == null || fn:length(course.learningObjectLinks) == 0}">
				<tr>
					<td colspan="100%" align="left"><header>No learning
							objects linked</header></td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach var="learningObjectLinksKVP"
					items="${course.learningObjectLinks}">
					<c:set var="learningObjectLinkBean"
						value="${learningObjectLinksKVP.value}" />
					<c:set var="learningObjectDetailsBean"
						value="${learningObjectLinkBean.learningObject}" />
					<tr>
						<td colspan="100%"><c:out
								value="${learningObjectDetailsBean.referenceId}" /></td>
					</tr>
					<tr>
						<td><input type="checkbox" name="learningObjectIds"
							value="${learningObjectDetailsBean.id}"
							<c:if test="${not empty course.learningObjectLinks[learningObjectDetailsBean.id]}">checked</c:if> />
						</td>
						<td><c:choose>
								<c:when
									test="${learningObjectDetailsBean.contentType == 'Image'}">
									<img height="200px"
										src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>" />
								</c:when>
								<c:when
									test="${learningObjectDetailsBean.contentType == 'Audio'}">
									<audio controls>
										<source
											src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></source>
									</audio>
								</c:when>
								<c:when
									test="${learningObjectDetailsBean.contentType == 'Video'}">
									<video height="200px" width="auto" controls>
										<source
											src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></source>
									</video>
								</c:when>
								<c:when
									test="${learningObjectDetailsBean.contentType == 'Text'}">
									<object>
										<embed height="200px"
											src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></embed>
									</object>
								</c:when>
								<c:otherwise>
							No preview available, unknown type
						</c:otherwise>
							</c:choose></td>
						<td><select name="courseContentNames" multiple>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Name" />
								<option value="${linkKey}"
									<c:if test="${not empty course.courseContentLearningObjectLinks[linkKey]}">selected</c:if>>Name</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Description" />
								<option value="${linkKey}"
									<c:if test="${not empty course.courseContentLearningObjectLinks[linkKey]}">selected</c:if>>Description</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Prerequisites" />
								<option value="${linkKey}"
									<c:if test="${not empty course.courseContentLearningObjectLinks[linkKey]}">selected</c:if>>Prerequisites</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Topics" />
								<option value="${linkKey}"
									<c:if test="${not empty course.courseContentLearningObjectLinks[linkKey]}">selected</c:if>>Topics</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Overview" />
								<option value="${learningObjectDetailsBean.id}-Overview"
									<c:if test="${courseContentName == 'Overview'}">selected</c:if>>Overview</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Timeline" />
								<option value="${learningObjectDetailsBean.id}-Timeline"
									<c:if test="${courseContentName == 'Timeline'}">selected</c:if>>Timeline</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-IndividualAssignments" />
								<option
									value="${learningObjectDetailsBean.id}-IndividualAssignments"
									<c:if test="${courseContentName == 'IndividualAssignments'}">selected</c:if>>Individual
									Assignments</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-GroupAssignments" />
								<option value="${learningObjectDetailsBean.id}-GroupAssignments"
									<c:if test="${courseContentName == 'GroupAssignments'}">selected</c:if>>Group
									Assignments</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-OnsiteAssignments" />
								<option
									value="${learningObjectDetailsBean.id}-OnsiteAssignments"
									<c:if test="${courseContentName == 'OnsiteAssignments'}">selected</c:if>>Onsite
									Assignments</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Exams" />
								<option value="${learningObjectDetailsBean.id}-Exams"
									<c:if test="${courseContentName == 'Exams'}">selected</c:if>>Exams</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-Quizzes" />
								<option value="${learningObjectDetailsBean.id}-Quizzes"
									<c:if test="${courseContentName == 'Quizzes'}">selected</c:if>>Quizzes</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-KnowledgeCheckpoints" />
								<option
									value="${learningObjectDetailsBean.id}-KnowledgeCheckpoints"
									<c:if test="${courseContentName == 'KnowledgeCheckpoints'}">selected</c:if>>Knowledge
									Checkpoints</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-GradingRubric" />
								<option value="${learningObjectDetailsBean.id}-GradingRubric"
									<c:if test="${courseContentName == 'GradingRubric'}">selected</c:if>>Grading
									Rubric</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-TechnologyRequirements" />
								<option
									value="${learningObjectDetailsBean.id}-TechnologyRequirements"
									<c:if test="${courseContentName == 'TechnologyRequirements'}">selected</c:if>>Technology
									Requirements</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-SupportServices" />
								<option value="${learningObjectDetailsBean.id}-SupportServices"
									<c:if test="${courseContentName == 'SupportServices'}">selected</c:if>>Support
									Services</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-DocumentTemplates" />
								<option
									value="${learningObjectDetailsBean.id}-DocumentTemplates"
									<c:if test="${courseContentName == 'DocumentTemplates'}">selected</c:if>>Document
									Templates</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-EvaluationFormat" />
								<option value="${learningObjectDetailsBean.id}-EvaluationFormat"
									<c:if test="${courseContentName == 'EvaluationFormat'}">selected</c:if>>Evaluation
									Format</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-EvaluationAttachments" />
								<option
									value="${learningObjectDetailsBean.id}-EvaluationAttachments"
									<c:if test="${courseContentName == 'EvaluationAttachments'}">selected</c:if>>Evaluation
									Attachments</option>
								<c:set var="linkKey"
									value="${learningObjectDetailsBean.id}-ProgressMeterBadges" />
								<option
									value="${learningObjectDetailsBean.id}-ProgressMeterBadges"
									<c:if test="${courseContentName == 'ProgressMeterBadges'}">selected</c:if>>Progress
									Meter Badges</option>
								<c:forEach items="${course.objectives}"
									var="courseObjectivesKVP" varStatus="loop">
									<c:set var="courseObjective"
										value="${courseObjectivesKVP.value}" />
									<option
										value="${learningObjectDetailsBean.id}-Objective-${courseObjective.id}"
										<c:if test="${not empty courseObjective.learningObjectLinks[learningObjectDetailsBean.id]}">selected</c:if>>Objective
										${loop.count} [${courseObjective.id}]</option>
								</c:forEach>
						</select></td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<c:if
		test="${course.learningObjectLinks != null && fn:length(course.learningObjectLinks) > 0}">
		<input type="submit" value="Update Links to Learning Objects"
			onclick="linklearningobjects_onclick(this)" />
	</c:if>
	<table style="width: 700px">
		<tr>
			<td width='50%'>Keywords</td>
			<td width='50%'><c:choose>
					<c:when test="${not empty keywords}">
						<textarea id="keywords" name="keywords" rows="4">
							<c:out value="${keywords}" />
						</textarea>
					</c:when>
					<c:otherwise>
						<textarea id="keywords" name="keywords" rows="4">
							<c:out value="${course.annotations.keywords}" />
						</textarea>
					</c:otherwise>
				</c:choose></td>
		</tr>
		<tr>
			<td>Type of Learning Object</td>
			<td><select name="typeOfLearningObject">
					<option value="Image"
						<c:if test="${typeOfLearningObject == 'Image'}">selected</c:if>>Image</option>
					<option value="Audio"
						<c:if test="${typeOfLearningObject == 'Audio'}">selected</c:if>>Audio</option>
					<option value="Video"
						<c:if test="${typeOfLearningObject == 'Video'}">selected</c:if>>Video</option>
					<option value="Text"
						<c:if test="${typeOfLearningObject == 'Text'}">selected</c:if>>Text</option>
			</select></td>
		</tr>
	</table>
	<input type="submit" value="Search Learning Objects"
		onclick="searchlearningobjects_onclick(this)" />
	<c:if test="${requestScope.learningObjectsSearchResults != null}">
		<table style="width: 700px;">
			<c:choose>
				<c:when
					test="${fn:length(requestScope.learningObjectsSearchResults) == 0 }">
					<tr>
						<td colspan="100%" align="left"><header>No matching
								learning objects found</header></td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="learningObjectsKVP"
						items="${requestScope.learningObjectsSearchResults}">
						<c:set var="learningObjectDetailsBean"
							value="${learningObjectsKVP.value}" />
						<tr>
							<td colspan="100%"><c:out
									value="${learningObjectDetailsBean.referenceId}" /></td>
						</tr>
						<tr>
							<td><input type="checkbox" name="learningObjectIds"
								value="${learningObjectDetailsBean.id}" /></td>
							<td><c:choose>
									<c:when
										test="${learningObjectDetailsBean.contentType == 'Image'}">
										<img height="200px"
											src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>" />
									</c:when>
									<c:when
										test="${learningObjectDetailsBean.contentType == 'Audio'}">
										<audio controls>
											<source
												src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></source>
										</audio>
									</c:when>
									<c:when
										test="${learningObjectDetailsBean.contentType == 'Video'}">
										<video height="200px" width="auto" controls>
											<source
												src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></source>
										</video>
									</c:when>
									<c:when
										test="${learningObjectDetailsBean.contentType == 'Text'}">
										<object>
											<embed height="200px"
												src="data:<c:out value='${learningObjectDetailsBean.fileType}'></c:out>;base64,<c:out value='${learningObjectDetailsBean.content}'/>"></embed>
										</object>
									</c:when>
									<c:otherwise>
							No preview available, unknown type
						</c:otherwise>
								</c:choose></td>
							<td><select name="courseContentNames" multiple>
									<option value="${learningObjectDetailsBean.id}-Name">Name</option>
									<option value="${learningObjectDetailsBean.id}-Description">Description</option>
									<option value="${learningObjectDetailsBean.id}-Prerequisites">Prerequisites</option>
									<option value="${learningObjectDetailsBean.id}-Topics">Topics</option>
									<option value="${learningObjectDetailsBean.id}-Overview">Overview</option>
									<option value="${learningObjectDetailsBean.id}-Timeline">Timeline</option>
									<option
										value="${learningObjectDetailsBean.id}-IndividualAssignments">Individual
										Assignments</option>
									<option
										value="${learningObjectDetailsBean.id}-GroupAssignments">Group
										Assignments</option>
									<option
										value="${learningObjectDetailsBean.id}-OnsiteAssignments">Onsite
										Assignments</option>
									<option value="${learningObjectDetailsBean.id}-Exams">Exams</option>
									<option value="${learningObjectDetailsBean.id}-Quizzes">Quizzes</option>
									<option
										value="${learningObjectDetailsBean.id}-KnowledgeCheckpoints">Knowledge
										Checkpoints</option>
									<option value="${learningObjectDetailsBean.id}-GradingRubric">Grading
										Rubric</option>
									<option
										value="${learningObjectDetailsBean.id}-TechnologyRequirements">Technology
										Requirements</option>
									<option value="${learningObjectDetailsBean.id}-SupportServices">Support
										Services</option>
									<option
										value="${learningObjectDetailsBean.id}-DocumentTemplates">Document
										Templates</option>
									<option
										value="${learningObjectDetailsBean.id}-EvaluationFormat">Evaluation
										Format</option>
									<option
										value="${learningObjectDetailsBean.id}-EvaluationAttachments">Evaluation
										Attachments</option>
									<option
										value="${learningObjectDetailsBean.id}-ProgressMeterBadges">Progress
										Meter Badges</option>
									<c:forEach items="${course.objectives}"
										var="courseObjectivesKVP" varStatus="loop">
										<c:set var="courseObjective"
											value="${courseObjectivesKVP.value}" />
										<option
											value="${learningObjectDetailsBean.id}-Objective-${courseObjective.id}">Objective
											${loop.count} [${courseObjective.id}]</option>
									</c:forEach>
							</select></td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
		<input type="submit" value="Link Learning Objects"
			onclick="linklearningobjects_onclick(this)" />
	</c:if>
	<script>
function searchlearningobjects_onclick(element) {
    var searchandlinkform = element.form;
    if (searchandlinkform.keywords.value == "") {
        alert("Please enter keywords")
        document.searchandlinkform.keywords.focus();
        return false;
    }
    searchandlinkform.method = "get";
    var actionName = document.getElementById("actionName")
    if (actionName) {
    	actionName.value = "searchLearningObjects";
    }
    searchandlinkform.submit();
    return true;
}

function linklearningobjects_onclick(element) {
    var searchandlinkform = element.form;
    searchandlinkform.method = "post";
    var actionName = document.getElementById("actionName")
    if (actionName) {
    	actionName.value = "linkLearningObjects";
    }
    searchandlinkform.submit();
    return true;
}
</script>
</body>
</html>