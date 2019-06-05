<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
	<title>Корзина</title>
	<%@ include file="jspf/headtag.jspf" %>
</head>
<body>
<%@ include file="jspf/menu.jspf" %>

<%-- PAGE VARIABLES --%>
<jsp:useBean id="params" class="ua.nure.order.client.ReqParam" scope="page">
	<jsp:setProperty property="params" name="params" value="${paramValues }"/>
</jsp:useBean>

<jsp:useBean id="cartDao" class="ua.nure.order.client.CartMealDAO" scope="page">
	<jsp:setProperty property="cart" name="cartDao" value="${sessionScope.cart }"/>
</jsp:useBean>
<jsp:useBean id="pag" class="ua.nure.order.shared.Pagination" scope="request" >
	<jsp:setProperty property="ascending" name="pag" value="true"/>
	<jsp:setProperty property="sortField" name="pag" value="title"/>
</jsp:useBean>
<jsp:setProperty property="dao" name="pag" value="${cartDao }"/>
<jsp:setProperty property="*" name="pag" />

<c:set var="meals" value="${pag.items }" scope="page" />

<%-- CONTENT --%>

<div class="section main-content" >
	<%@ include file="jspf/showmsg.jspf" %>

	<div class="container">
		<c:choose>
			<c:when test="${empty cart }">
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 text-center">
						<h1>Корзина пуста</h1>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 text-center">
						<h1>Ваша корзина</h1>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">

						<table class="table table-bordered table-striped">
							<col class="col-position" />
							<col class="col-title" />
							<col class="col-author" />
							<col class="col-price" />
								<%-- <col class="col-count" /> --%>
							<col class="col-action" />
							<thead>
							<tr class="text-center">
								<th class="text-center">#</th>
								<th class="text-center">
									<div class="btn-group drop">
										Название<b class="caret"></b>
									</div>
								</th>
								<th class="text-center">Ингредиенты</th>
								<th class="text-center">Цена</th>
								<!-- <th class="text-center">Кол-во</th> -->
								<th class="text-center">В корзине</th>
							</tr>
							</thead>
							<tbody>
							<c:set var="k" value="0" />
							<c:forEach var="meal" items="${meals }">
								<%-- <tr ${cart.get(book) > book.count ? "class='row-warning' title='Не достаточно книг.'" :'' }> --%>
								<tr class="${cart.get(meal) > meal.count ? 'alert-danger' : '' }" title="В наличии: ${meal.count }">
									<c:set var="k" value="${k + 1}" />
									<td><c:out value="${k}" /></td>
									<td>${meal.title}</td>
									<td>
										<c:forEach var="a" items="${meal.ingredient}">
											${a.title}<br/>
										</c:forEach>
									</td>
									<td class="text-right">${meal.price }</td>
									<td>
										<form id="update-${meal.id }" action="updatecart?${params.setParam('ascending', pag.ascending) }" method="post">
											<c:set value="${cart.get(meal) }" var="c"></c:set>
											<div class="input-group">
												<input type="number" name="count"
													   value="${c > meal.count ? meal.count : c }"
													   class="form-control" min="0" max="${meal.count }">
												<!-- </td>
                                                <td> -->
												<span class="input-group-btn"><button type="submit" name="update" form="update-${meal.id }"
																					  title="Обновить корзину"
																					  class="btn btn-success" value="${meal.id }">
													<i class="glyphicon glyphicon-shopping-cart"></i>
												</button>
												<button type="submit" name="remove" form="update-${meal.id }"
														title="Удалить из корзины"
														class="btn btn-danger" value="${meal.id }">
													<i class="glyphicon glyphicon-remove"></i>
												</button></span>
											</div>
										</form>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 text-right">
						<p class="text-right lead">Всего: ${sessionScope.cart.getPrice() }</p>
					</div>
				</div>

				<div class="row">
					<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 text-center">
						<a href="filldelivery.jsp" class="btn btn-primary btn-block">Оформить заказ</a>
					</div>
				</div>

				<%-- <c:set value="${pag }" var="paging" scope="request" />
                <jsp:include page="jspf/pagination.jsp" /> --%>

			</c:otherwise>
		</c:choose>
	</div>
</div>

<%@ include file="jspf/bootstrap.jspf" %>
</body>
</html>