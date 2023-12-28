<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>List of phones</title>
</head>
<body>
	<div>
		<form action="${pageContext.request.contextPath}/items" method="post">
			<button type="submit" name="brand" value="Apple">Apple</button>
			<button type="submit" name="brand" value="Xiaomi">Xiaomi</button>
			<button type="submit" name="brand" value="Samsung">Samsung</button>
			<button type="submit" name="brand" value="AllPhones">All phones</button>
		</form>
	</div>
	<div>List of phones</div>
	<br>
	<ul>
		<c:forEach var="item" items="${requestScope.items}">
			<li>${item.model} ${item.price} ${item.currency} </li>
		</c:forEach>
	</ul>
</body>
</html>