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

<title>Tribal Education Network Sign Up Screen</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script>

    </script>
</head>
<body>
	<script>
    function butCheckForm_onclick()
    {
        var myForm = document.newuser;
        var emailpat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)";
        // "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

        if (myForm.userName.value == "")
        {
            alert("Please enter your user name")
            myForm.userName.focus();
            return false;
        }
        if (myForm.firstName.value == "")
        {
            alert("Please enter your first name")
            myForm.firstName.focus();
            return false;
        }
        if (myForm.lastName.value == "")
        {
            alert("Please enter your last name")
            myForm.lastName.focus();
            return false;
        }       
        if (myForm.emailId.value == "")
        {
            alert("Please enter valid email address")
            myForm.emailId.focus();
            return false;
        }
        else if (myForm.emailId.value != "")
        {
            if (!myForm.emailId.value.match(emailpat))
            {
                alert("Please enter valid email address")
                myForm.emailId.focus();
                return false;
            }
        } 
        if (myForm.userPassword.value == "")
        {
            alert("Please enter password")
            myForm.userPassword.focus();
            return false;
        }
        if (myForm.rePassword.value == "")
        {
            alert("Please re-enter password")
            myForm.rePassword.focus();
            return false;
        }
        if (myForm.userPassword.value != myForm.rePassword.value)
        {
            alert("Passwords do not match")
            myForm.userPassword.focus();
            return false;
        }
        
        alert("Account Created Successfuly")
        myForm.submit();
        return true;
    }
    </script>


	<form action="${pageContext.request.contextPath}/login/signup.action"
		name="newuser" method="POST" enctype="multipart/form-data">
		<!--<form name="newuser" method="POST" enctype="multipart/form-data"> -->
		<%@include file="/WEB-INF/jsp/include_header_signup.jsp"%>
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
			<h3>New user account. Please fill in the details below (fields
				marked with * are mandatory)</h3>
			User Name*: <input type="text" name="userName" required /><br />
			<br /> First Name*: <input type="text" name="firstName" required /><br />
			<br /> Middle Name: <input type="text" name="middleName" required /><br />
			<br /> Last Name*: <input type="text" name="lastName" required /><br />
			<br /> Role: <select name="role" onchange="">
				<option value="Student">STUDENT</option>
				<option value="Creator">CREATOR</option>
				<option value="Annotator">ANNOTATOR</option>
				<option value="Intaker">INTAKER</option>
			</select> <br />
			<br /> Email Id*:<input type="text" name="emailId" required /><br />
			<br /> Password*:<input type="password" name="userPassword" required /><br />
			<br /> Reenter Password*:<input type="password" name="rePassword"
				required /><br />
			<br /> <input type="button" value="Submit" name="butCheckForm"
				onClick="butCheckForm_onclick()">

			<!--<input type="submit" value="Submit">-->
			<!-- <input type="button" name="submit" value="Submit">  -->
			<!-- <input type="button" name="submit" value="Submit" onClick="formSubmitter('sampleform', 'message')"><div id='message'></div>-->
			<!--   <input type="button" value="Submit" name="butCheckForm" onclick="location.href='${pageContext.request.contextPath}/main"/>   -->
		</section>
	</form>
</body>