<!DOCTYPE html>
<html>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<title>Logout Tribal Education Network</title>
</head>
<body>
	<form>
		<section>
			User '
			<c:out value="${user_name}" />
			' has been logged out. <br />
			<br /> <a href="${pageContext.request.contextPath}/main">Click
				here to login again</a>
		</section>
	</form>
</body>
</html>