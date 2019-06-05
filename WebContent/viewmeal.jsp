%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ru">
<head>
<title>View meal</title>
<%@ include file="jspf/headtag.jspf" %>
</head>
<body>
<%@ include file="jspf/menu.jspf" %>

	<div class="section main-content" >
		<div class="container">
			<div class="row">
				<h2>${meal.title}</h2>
			</div>
			<div class="row">
				<div class="col-md-3">
					<img alt="" src="">
				</div>
				<div class="col-md-9">
					<ul>
						<li>Ингредиенты:
							<c:forEach var="a" items="${meal.ingredient }">
								${a.title }<br/>
							</c:forEach>
						</li>
						<li>Вес порции: ${meal.calory} гр.</li>
						<li>Цена за одно блюдо: ${meal.price} Грн.</li>
						<li>Вожможно приготовить: ${meal.count} ед.</li>
					</ul>
				</div>
			</div>
			<c:if test="${user.role == null || user.role != util.getRole('admin') }">
			<div class="row">
				<div class="col-md-3">
					<img alt="" src="">
				</div>
				<div class="col-md-9">
					<form action="${context }/addtocart" method="post">
						<input type="hidden" name="id" value="${meal.id}"/>
						<input type="number" min="1" max="${meal.count}" name="count" value="1"/>
						<button type="submit" name="tocart" id="${meal.id }" title="Добавить в заказ"
							class="btn btn-success" value="${meal.id }">
							<i class="glyphicon glyphicon-shopping-cart"></i>
						</button>
					</form>
				</div>
			</div>
			</c:if>
		</div>
	</div>


<%@ include file="jspf/bootstrap.jspf" %>
</body>
</html>