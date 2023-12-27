<%@page import="servlet.AddToCartSerlvet.CartStatus"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<fmt:setLocale value="${sessionScope.language}" />
<fmt:setBundle basename="translations" />
<head>
<title>${sessionScope.User.name}<fmt:message key="cart.cart" />
</title>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<c:choose>
		<c:when test="${sessionScope.CartStatus == 'HAVE_ITEMS'}">
			<ul>
				<c:forEach items="${sessionScope.Cart.itemsDtoMap}" var="entry">
					<li>${entry.value.cartparams}</li>
				</c:forEach>
			</ul>
		</c:when>
		<c:when test="${sessionScope.CartStatus == 'INVALID_QUANTITY'}">
			<h2>
				<fmt:message key="cart.invalidQuantity" />
				${param.canAdd}
			</h2>
			<a href="/items-parameters?itemId=${param.itemId}"><fmt:message
					key="cart.returnToItems" /></a>
		</c:when>
		<c:otherwise>
			<div>
				<fmt:message key="cart.empty" />
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>