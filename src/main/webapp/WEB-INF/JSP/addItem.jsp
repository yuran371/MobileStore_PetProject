<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="entity.enums.Attributes" %>
<%@ page import="entity.enums.CurrencyEnum" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add item</title>
    <%@ include file="Header.jsp" %>
</head>

<body>
<form action="/add-item" method="post">
    <label for="modelId">
        Model: <input type="text" name="model" id="modelId" required>
    </label>
    <br>
    <label for="brandId">
        Brand:
        <select name="brand" id="brandId">
            <c:forEach items="<%=Attributes.BrandEnum.values()%>" var="brand">
                <option value="${brand.brand}">${brand.brand}</option>
            </c:forEach>
        </select>
    </label>
    <br>
    <label for="ramId">
        RAM:
        <select name="ram" id="ramId">
            <c:forEach items="<%=Attributes.RamEnum.values()%>" var="ram">
                <option value="${ram.ram}">${ram.ram}</option>
            </c:forEach>
        </select>
    </label>
    <br>
    <label for="internalMemoryId">
        Internal Memory:
        <select name="internalMemory" id="internalMemoryId">
            <c:forEach items="<%=Attributes.InternalMemoryEnum.values()%>" var="internalMemory">
                <option value="${internalMemory.internalMemory}">${internalMemory.internalMemory}</option>
            </c:forEach>
        </select>
    </label>
    <br>
    <label for="osId">
        OS:
        <select name="os" id="osId">
            <c:forEach items="<%=Attributes.OperatingSystemEnum.values()%>" var="os">
                <option value="${os.os}">${os.os}</option>
            </c:forEach>
        </select>
    </label>
    <br>
    <label for="colorId">
        <label for="modelId">
            Color: <input type="text" name="color" id="colorId" required>
        </label>
    </label>
    <br>
    <label for="priceId">
        Price: <input type="text" name="price" id="priceId"
                      required>
    </label>
    <br>
    <label for="currencyId">
        Currency:
        <select name="currency" id="currencyId">
            <c:forEach items="<%=CurrencyEnum.values()%>" var="currency">
                <option value="${currency.name()}">${currency.toString()}</option>
            </c:forEach>
        </select>
    </label>
    <br>
    <label for="quantityId">
        Quantity: <input type="text" name="quantity" id="quantityId"
                         required>
    </label>
    <br>
    <button type="submit">Add item</button>
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
