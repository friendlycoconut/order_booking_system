<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<title>Add meal</title>
<%@ include file="../jspf/headtag.jspf" %>
</head>
<body>
	<%@ include file="../jspf/menu.jspf" %>
	<jsp:useBean id="meal" class="ua.nure.order.entity.meal.Meal" scope="session"/>
	<jsp:setProperty property="*" name="meal"/>
	<div>
		<div id="form">
			<form action="addmeal" method="post">
				<table>
					<caption>Add meal</caption>
					<tr>
						<td colspan="2"><span id="formError"><c:out value="${errors['title'] }" /></span></td>
					</tr>
					<tr>
						<td><span id="formLabel">Название блюда:</span></td>
						<td><input type="text" name="title" value="${meal.title } "
								autofocus="autofocus" required="required" /></td>
					</tr>
					<tr>
						<td colspan="2"><span id="formError"><c:out value="${errors['pass'] }" /></span></td>
					</tr>
					<tr>
						<td><span id="formLabel">Вес порции:</span></td>
						<td><input type="text" name="calory" value="${meal.calory }"
								 /></td>
					</tr>
					<tr>
						<td colspan="2"><span id="formError"><c:out value="${errors['count'] }" /></span></td>
					</tr>
					<tr>
						<td><span id="formLabel">Количество блюд в наличии</span></td>
						<td><input type="number" name="count" value="${meal.count }"
								min="0" required="required" /></td>
					</tr>
					<tr>
						<td colspan="2"><span id="formError"><c:out value="${errors['ingredient'] }" /></span></td>
					</tr>
					<c:set var="k" value="0"/>
					<c:if test="${fn:length(meal.ingredient) != 0 }">
						<c:forEach var="a" items="${meal.ingredient }">
							<tr>
								<td><span id="formLabel">Ингредиент:</span></td>
								<td><input type="text" name="ingredient" value="${meal.ingredient[k] }" />
									<input type="submit" value="+" formaction="addingredient" /></td>
							</tr>
						<c:set var="k" value="${k + 1 }"/>
						</c:forEach>
					</c:if>
					<tr>
						<td><span id="formLabel">Ингредиент:</span></td>
						<td><input type="text" name="ingredient" value="${meal.ingredient[k] }" />
							<input type="submit" value="+" formaction="addingredient" /></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" value="Add"  /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>

<%@ include file="../jspf/bootstrap.jspf" %>
</body>
</html>
