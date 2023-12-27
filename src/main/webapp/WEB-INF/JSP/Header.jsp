<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div>
	<c:if test="${not empty sessionScope.User}">
		<body>
			<form action="${pageContext.request.contextPath}/logout"
				method="post" enctype="application/x-www-form-urlencoded">
				<button type="submit">Logout</button>
			</form>
		</body>
	</c:if>
	<div>
		<form action="${pageContext.request.contextPath}/locale" method="post">
			<button type="submit" name="language" value="ru">RUS</button>
			<button type="submit" name="language" value="en">ENG</button>
		</form>
	</div>
	<fmt:setLocale value="${sessionScope.language}"/>
	<fmt:setBundle basename="translations"/>
</div>