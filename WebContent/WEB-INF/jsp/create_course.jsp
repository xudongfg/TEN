<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

	var DefaultName = "objective";
	var DefaultNameIncrementNumber = 0;

	function AddFormField(id, type, name, value, tag) {
		if (! document.getElementById && document.createElement) { return; }
		var inhere = document.getElementById(id);
		var formfield = document.createElement("textarea");
		if (name.length < 1) {
	    	DefaultNameIncrementNumber++;
	   		name = String(DefaultName + DefaultNameIncrementNumber);
	    }
		formfield.name = name;
		formfield.type = type;
		formfield.value = value;
		if (tag.length > 0) {
	   		var thetag = document.createElement(tag);
	   		thetag.appendChild(formfield);
	   		inhere.appendChild(thetag);
	   	}
		else {
			inhere.appendChild(formfield);
		}
		return false;
	} // function AddFormField()

	</script>
<style>
span.fake-link {
	text-decoration: underline;
	cursor: pointer;
	color: blue;
}
</style>
</head>
<body>
	<form action="${pageContext.request.contextPath}/course/create.action"
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
					<td width='50%'><b>Course Name</b></td>
					<td width='50%'><s:textarea id="courseName" name="courseName"
							cols="50" rows="3">
							<s:param name="value"> ${courseName} </s:param>
						</s:textarea></td>
				</tr>
				<tr>
					<td><b>Course Objective(s)</b></td>
				</tr>
				<tr>
					<td valign="top">List course objectives, one per field. Click
						to add <span class="fake-link"
						onclick="AddFormField('courseObjectivesList','textarea','courseObjectives','','li');">[more]</span>.
					</td>
					<td>
						<ol start="1" id="courseObjectivesList">
							<li><textarea id="textarea" name="courseObjectives"></textarea></li>
						</ol>
					</td>
				</tr>

				<tr>
					<td width='70%'><b>Description </b></td>
					<td width='50%'><s:textarea id="courseDescription"
							name="courseDescription" cols="50" rows="10">
							<s:param name="value"> ${courseDescription} </s:param>
						</s:textarea></td>
				</tr>

				<tr>
					<td width='70%'><b>Prerequisites </b></td>
					<td width='50%'><s:textarea id="coursePrerequisites"
							name="coursePrerequisites" cols="50" rows="10">
							<s:param name="value"> ${coursePrerequisites} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Topics </b></td>
					<td width='50%'><s:textarea id="topics" name="topics"
							cols="50" rows="10">
							<s:param name="value"> ${topics} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Overview </b></td>
					<td width='50%'><s:textarea id="overview" name="overview"
							cols="50" rows="10">
							<s:param name="value"> ${overview} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Timeline </b></td>
					<td width='50%'><s:textarea id="timeline" name="timeline"
							cols="50" rows="10">
							<s:param name="value"> ${timeline} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Individual Assignments </b></td>
					<td width='50%'><s:textarea id="individualAssignments"
							name="individualAssignments" cols="50" rows="10">
							<s:param name="value"> ${individualAssignments} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Group Assignments </b></td>
					<td width='50%'><s:textarea id="groupAssignments"
							name="groupAssignments" cols="50" rows="10">
							<s:param name="value"> ${groupAssignments} </s:param>
						</s:textarea></td>
				</tr>

				<tr>
					<td width='70%'><b>On-site Assignments </b></td>
					<td width='50%'><s:textarea id="onsiteAssignments"
							name="onsiteAssignments" cols="50" rows="10">
							<s:param name="value"> ${onsiteAssignments} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Exams </b></td>
					<td width='50%'><s:textarea id="exams" name="exams" cols="50"
							rows="10">
							<s:param name="value"> ${exams} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Quizzes </b></td>
					<td width='50%'><s:textarea id="quizzes" name="quizzes"
							cols="50" rows="10">
							<s:param name="value"> ${quizzes} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Checkpoints </b></td>
					<td width='50%'><s:textarea id="knowledgeCheckpoints"
							name="knowledgeCheckpoints" cols="50" rows="10">
							<s:param name="value"> ${knowledgeCheckpoints} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Grading Rubric </b></td>
					<td width='50%'><s:textarea id="gradingRubric"
							name="gradingRubric" cols="50" rows="10">
							<s:param name="value"> ${gradingRubric} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Progress Meter or Badges </b></td>
					<td width='50%'><s:textarea id="progressMeterBadges"
							name="progressMeterBadges" cols="50" rows="10">
							<s:param name="value"> ${progressMeterBadges} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Technology Requirements </b></td>
					<td width='50%'><s:textarea id="technologyRequirements"
							name="technologyRequirements" cols="50" rows="10">
							<s:param name="value"> ${technologyRequirements} </s:param>
						</s:textarea></td>
				</tr>


				<tr>
					<td width='70%'><b>Support Services </b></td>
					<td width='50%'><s:textarea id="supportServices"
							name="supportServices" cols="50" rows="10">
							<s:param name="value"> ${supportServices} </s:param>
						</s:textarea></td>
				</tr>

				<tr>
					<td width='70%'><b>Evaluation Format </b></td>
					<td width='50%'><s:textarea id="evaluationFormat"
							name="evaluationFormat" cols="50" rows="10">
							<s:param name="value"> ${evaluationFormat} </s:param>
						</s:textarea></td>
				</tr>
				<%@include file="course_annotations.jsp"%>
				<tr>
					<td><input type="submit" value="Create Course" /></td>
				</tr>
			</table>
		</section>

	</form>
</body>
</html>