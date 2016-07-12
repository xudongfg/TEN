<!DOCTYPE html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<title>Tribal Education Network Error Page</title>
</head>
<body>
	<%@include file="include_header.jsp"%>

	Some Error Occured!!
	<br>
	<br> Error Name:
	<s:property value="exception" />
	<br>
	<br> Error Stack Trace:
	<br>
	<s:property value="exceptionStack" />
</body>
</html>