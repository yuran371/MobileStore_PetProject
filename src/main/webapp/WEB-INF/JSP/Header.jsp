<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
	<c:if test="${not empty sessionScope.User}">
		<body>
			<form action="${pageContext.request.contextPath}/logout"
				method="post" enctype="application/x-www-form-urlencoded">
				<button type="submit">Logout</button>
			</form>
		</body>
	</c:if>
</div>