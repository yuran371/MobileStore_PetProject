<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Create account</title>
<%@ include file="Header.jsp" %>
</head>

<body>
	<form action="/add-item" method="post">
		<label for="modelId">
			Model: <input type="text" name="model" id="modelId" required>
		</label>
		<br>
		<label for="brandId">
			Brand: <input type="text" name="brand" id="brandId"
				required>
		</label>
		<br>
		<label for="attributesId">
			Attributes: <input type="text" name="attributes" id="attributesId" required>
		</label>
		<br>
		<label for="priceId">
			Price: <input type="text" name="price" id="priceId"
				required>
		</label>
		<br>
		<label for="currencyId">
			Currency: <input type="text" name="currency" id="currencyId"
				required>
		</label>
		<br>
		<label for="quantityId">
			Quantity: <input type="text" name="quantity" id="quantityId"
				required>
		</label>
		<br>
		<button type="submit">Create account</button>
	</form>
	<div>
		<c:if test="${not empty requestScope.errors}">
			<c:forEach var="error" items="${requestScope.errors}">
				<span>${error.message}</span>
			</c:forEach>
		</c:if>
	</div>
</body>
</html>
