<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<html>
<head><title>Керування квартирами</title></head>
<body>
<h1>Мої об'єкти (Хазяїн)</h1>

<ul>
  <c:forEach var="apt" items="${apartments}">
    <li><c:out value="${apt.title}" /> - <c:out value="${apt.price}" /> грн</li>
  </c:forEach>
</ul>

<hr>
<h2>Додати нову квартиру</h2>
<form action="${pageContext.request.contextPath}/manage" method="POST">
  Назва: <input type="text" name="title" required><br>
  Кімнат: <input type="number" name="rooms" min="1" required><br>
  Ціна: <input type="number" name="price" min="0" required><br>
  Опис: <textarea name="description"></textarea><br>
  <button type="submit">Зберегти</button>
</form>
</body>
</html>