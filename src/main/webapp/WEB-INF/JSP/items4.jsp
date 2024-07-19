<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Items</title>
	<style>
		table {
			width: 100%;
			border-collapse: collapse;
		}
		table, th, td {
			border: 1px solid black;
		}
		th, td {
			padding: 8px;
			text-align: left;
		}
	</style>
</head>
<body>
<h1>Items List</h1>
<table>
	<thead>
	<tr>
		<th>Brand</th>
		<th>Model</th>
		<th>Internal Memory</th>
		<th>RAM</th>
		<th>Color</th>
		<th>OS</th>
		<th>Image</th>
		<th>Price</th>
		<th>Currency</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="item" items="${items}">
		<tr>
			<td>${item.brand}</td>
			<td>${item.model}</td>
			<td>${item.internalMemory}</td>
			<td>${item.ram}</td>
			<td>${item.color}</td>
			<td>${item.os}</td>
			<td><img src="${item.image}" alt="${item.model}" width="100"></td>
			<td>${item.price}</td>
			<td>${item.currency}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<div>
	<c:choose>
		<c:when test="${page > 1}">
			<a href="?page=${page - 1}&limit=${limit}">Previous</a>
		</c:when>
		<c:otherwise>
			Previous
		</c:otherwise>
	</c:choose>

	<span>Page ${page}</span>

	<c:choose>
		<c:when test="${items.size() == limit}">
			<a href="?page=${page + 1}&limit=${limit}">Next</a>
		</c:when>
		<c:otherwise>
			Next
		</c:otherwise>
	</c:choose>
</div>
</body>
</html>
