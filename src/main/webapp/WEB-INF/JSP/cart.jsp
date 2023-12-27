<%@page import="servlet.AddToCartSerlvet.CartStatus"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
	<title>${sessionScope.User.name} корзина</title>
</head>
<body>
	<%@ include file="Header.jsp"%>
	<c:choose>
		<c:when test="${sessionScope.CartStatus == 'HAVE_ITEMS'}">
			<ul>
				<c:forEach items="${sessionScope.Cart.itemsDtoMap}" var="entry">
				<li> ${entry.value.cartparams} </li>
				</c:forEach>
			</ul>
		</c:when>
		<c:when
			test="${sessionScope.CartStatus == 'INVALID_QUANTITY'}">
			<h2>Quantity is invalid. You can add only ${param.canAdd}</h2>
			<a href="/items-parameters?itemId=${param.itemId}">Return to item</a>
		</c:when>
		<c:otherwise>
		<div>Cart is empty</div>
		</c:otherwise>
	</c:choose>
</body>
</html>