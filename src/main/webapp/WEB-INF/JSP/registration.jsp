<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Create account</title>
</head>
<body>
	<form action="/registration" method="post">
		<label for="emailId">
			Email: <input type="email" name="email" id="emailId" required>
		</label>
		<br>
		<label for="passwordId">
			Password: <input type="password" name="password" id="passwordId"
				required>
		</label>
		<br>
		<label for="nameId">
			First name: <input type="text" name="name" id="nameId" required>
		</label>
		<br>
		<label for="surnameId">
			Second name: <input type="text" name="surname" id="surnameId"
				required>
		</label>
		<br>
		<label for="dateId">
			Date of birth: <input type="date" name="dateOfBirth" id="dateId"
				required>
		</label>
		<br>
		<label>Your country:</label>
		<select name="country" id=country required>
			<c:forEach var="country" items="${requestScope.countries}">
				<option value="${country}">${country}</option>
			</c:forEach>
		</select>
		<br>
		<label for="cityId">
			Second name: <input type="text" name="city" id="cityId" required>
		</label>
		<br>
		<label for="addressId">
			Second name: <input type="text" name="address" id="addressId"
				required>
		</label>
		<br>
		<label for="phoneNumberId">
			Second name: <input type="text" name="phoneNumber" id="phoneNumberId"
				required>
		</label>
		<br>
		<c:forEach var="gender" items="${requestScope.genders}">
			<input type="radio" name="gender" value="${gender}"> ${gender}
		</c:forEach>
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
