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
        function manageCourseLinks(){
            document.getElementById("actionName").value = "manageCulturalLinks";
            document.manageCoursesDetails.submit();
        }

        function exportCourse(id){
            document.getElementById("actionName").value = "export";
            document.manageCoursesDetails.submit();
        }

        function editCourseDetails(){
            document.getElementById("actionName").value = "edit";
            document.manageCoursesDetails.submit();
        }
        
        function deleteCourseDetails(){
            document.getElementById("actionName").value = "delete";
            document.manageCoursesDetails.submit();
        }

        function removeSelectedLinks(){
            document.getElementById("actionName").value = "removeLinks";
            document.manageCoursesDetails.submit();
        }

        function addSelectedLinks(){
            document.getElementById("actionName").value = "addLinks";
            document.manageCoursesDetails.submit();
        }

        function searchForCulturalContent(){
            document.getElementById("actionName").value = "search";
            document.manageCoursesDetails.submit();
        }

	</script>
</head>
<body>
	<form name="manageCoursesDetails" id="manageCoursesDetails"
		action="${pageContext.request.contextPath}/courses/manage/coursedetails.action"
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
					<td><input type="button" value="Export course"
						onclick="exportCourse()" /></td>
				</tr>

				<%-- added by rajneet for new columns--%>
				<tr>
					<td width='70%'>Course Name</td>
					<td width='50%'><s:textarea id="name" name="name" cols="50"
							rows="3">
							<s:param name="value"> ${name} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Description</td>
					<td width='50%'><s:textarea id="description"
							name="description" cols="50" rows="10">
							<s:param name="value"> ${description} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Prerequisites</td>
					<td width='50%'><s:textarea id="prerequisites"
							name="prerequisites" cols="50" rows="10">
							<s:param name="value"> ${prerequisites} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Topics</td>
					<td width='50%'><s:textarea id="topics" name="topics"
							cols="50" rows="10">
							<s:param name="value"> ${topics} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Overview</td>
					<td width='50%'><s:textarea id="overview" name="overview"
							cols="50" rows="10">
							<s:param name="value"> ${overview} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Timeline</td>
					<td width='50%'><s:textarea id="timeline" name="timeline"
							cols="50" rows="10">
							<s:param name="value"> ${timeline} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Individual Assignments</td>
					<td width='50%'><s:textarea id="individualAssignments"
							name="individualAssignments" cols="50" rows="10">
							<s:param name="value"> ${individualAssignments} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Group Assignments</td>
					<td width='50%'><s:textarea id="groupAssignments"
							name="groupAssignments" cols="50" rows="10">
							<s:param name="value"> ${groupAssignments} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>On-site Assignments</td>
					<td width='50%'><s:textarea id="onsiteAssignments"
							name="onsiteAssignments" cols="50" rows="10">
							<s:param name="value"> ${onsiteAssignments} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Exams</td>
					<td width='50%'><s:textarea id="exams" name="exams" cols="50"
							rows="10">
							<s:param name="value"> ${exams} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Quizzes</td>
					<td width='50%'><s:textarea id="quizzes" name="quizzes"
							cols="50" rows="10">
							<s:param name="value"> ${quizzes} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Checkpoints</td>
					<td width='50%'><s:textarea id="knowledgeCheckpoints"
							name="knowledgeCheckpoints" cols="50" rows="10">
							<s:param name="value"> ${knowledgeCheckpoints} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Grading Rubric</td>
					<td width='50%'><s:textarea id="gradingRubric"
							name="gradingRubric" cols="50" rows="10">
							<s:param name="value"> ${gradingRubric} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Progress Meter or Badges</td>
					<td width='50%'><s:textarea id="progressMeterBadges"
							name="progressMeterBadges" cols="50" rows="10">
							<s:param name="value"> ${progressMeterBadges} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Technology Requirements</td>
					<td width='50%'><s:textarea id="technologyRequirements"
							name="technologyRequirements" cols="50" rows="10">
							<s:param name="value"> ${technologyRequirements} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Support Services</td>
					<td width='50%'><s:textarea id="supportServices"
							name="supportServices" cols="50" rows="10">
							<s:param name="value"> ${supportServices} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td width='70%'>Evaluation Format</td>
					<td width='50%'><s:textarea id="evaluationFormat"
							name="evaluationFormat" cols="50" rows="10">
							<s:param name="value"> ${evaluationFormat} </s:param>
						</s:textarea></td>
				</tr>
				<tr>

					<td><input type="button" value="Update Course"
						onclick="editCourseDetails()" /></td>
					<td><input type="button" value="Delete"
						onclick="deleteCourseDetails()" /></td>

				</tr>

				<tr>
					<td></td>
					<td></td>
				</tr>
			</table>

			<%@include file="search_and_link_learning_objects.jsp"%>

		</section>
		<input type="hidden" name="courseId" id="courseId" value="${courseId}" />
		<input type="hidden" name="actionName" id="actionName" value="" />
	</form>
</body>
</html>