<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head><title>Пошук квартир</title></head>
<body>
<h1>Пошук квартири (Клієнт)</h1>

<form action="${pageContext.request.contextPath}/search" method="GET">
    Кількість кімнат: <input type="number" name="rooms" min="1">
    <button type="submit">Знайти</button>
    <a href="${pageContext.request.contextPath}/search">Скинути</a>
</form>

<hr>

<c:if test="${empty apartments}">
    <p style="color: red;">За вашим запитом нічого не знайдено.</p>
</c:if>

<ul>
    <c:forEach var="apt" items="${apartments}">
        <li>
            <h3><c:out value="${apt.title}" /></h3>
            <p>Ціна: <c:out value="${apt.price}" /> грн</p>

            <p>Тип:
                <c:choose>
                    <c:when test="${apt.rooms == 1}">Студія / Однокімнатна</c:when>
                    <c:when test="${apt.rooms == 2}">Двокімнатна</c:when>
                    <c:otherwise>Простора (3+ кімнат)</c:otherwise>
                </c:choose>
            </p>
            <p>Опис: <c:out value="${apt.description}" /></p>
        </li>
    </c:forEach>
</ul>
</body>
</html>