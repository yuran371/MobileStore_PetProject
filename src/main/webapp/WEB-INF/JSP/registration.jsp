<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>Create account</title>
</head>
<body>
<img alt="User Image" src="/images/user/me.jpg">
	<form action="/registration" method="post" enctype="multipart/form-data">
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
		<label for="imageId">
			Image: <input type="file" name="image" id="imageId">
		</label>
		<br>
		<label for="dateId">
			Date of birth: <input type="date" name="dateOfBirth" id="dateId" required>
		</label>
		<br>
		<label>Your countryEnum:</label>
		<select name="countryEnum" id=countryEnum required>
			<c:forEach var="countryEnum" items="${requestScope.countries}">
				<option value="${countryEnum}">${countryEnum}</option>
			</c:forEach>
		</select>
		<br>
		<label for="cityId">
			City: <input type="text" name="city" id="cityId" required>
		</label>
		<br>
		<label for="addressId">
			Home address: <input type="text" name="address" id="addressId"
				required>
		</label>
		<br>
		<label for="phoneNumberId">
			Phone number: <input type="text" name="phoneNumber" id="phoneNumberId">
		</label>
		<br>
		<c:forEach var="genderEnum" items="${requestScope.genders}">
			<input type="radio" name="genderEnum" value="${genderEnum}"> ${genderEnum}
		</c:forEach>
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
