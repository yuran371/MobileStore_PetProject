<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Sign in</title>
</head>
<body>
	<form action="${pageContext.request.contextPath}/login" method="post"
		enctype="application/x-www-form-urlencoded">
		<label for="emailId">
			Login (email): <input type="email" name="email" id="emailId" required>
		</label>
		<br>
		<label for="passwordId">
			Password: <input type="password" name="password" id="passwordId"
				required>
		</label>
		<br>
		<button type="submit">Login</button>
	</form>
	<div>
		<a href="${pageContext.request.contextPath}/registration">
			<button type="button">Create account</button>
		</a>
	</div>
	<span> <c:if test="${param.error != null}">
			<div>Email or password is incorrect</div>
		</c:if>
	</span>
</body>
</html>
