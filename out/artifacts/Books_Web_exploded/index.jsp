<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="ua.nure.order.entity.meal.Meal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ru">
<head>
<title>Добро пожаловать</title>
<%@ include file="jspf/headtag.jspf" %>
</head>
<body>
<%@ include file="jspf/menu.jspf" %>

<%-- CONTENT --%>

	<div class="" >
	<div id="carousel" class="carousel slide" >
	
		<!-- Slide-indicators -->
		<ol class="carousel-indicators" >
			<li data-target="#carousel" data-slide-to="0" class="active" ></li>
			<li data-target="#carousel" data-slide-to="1" ></li>
			<li data-target="#carousel" data-slide-to="2" ></li>
		</ol>
	
		<!-- Slide-content -->
		<div class="carousel-inner" >
			<div class="item active" >
				<img alt="" src="img/new.jpg">
				<div class="carousel-caption" >
					<h3>Информационная система</h3>
					<p>Ресторан</p>
				</div>
			</div>
			<div class="item" >
				<img alt="" src="img/3.jpg">
				<div class="carousel-caption" >
					<h3>Вкусные блюда</h3>
					<p>Всегда в наличии</p>
				</div>
			</div>
			<div class="item" >
				<img alt="" src="img/1.jpg">
				<div class="carousel-caption" >
					<h3>Азиатская кухня</h3>
					<p>Скоро...</p>
				</div>
			</div>
		</div>
		
		<!-- Slide-control -->
		<a href="#carousel" class="left carousel-control" data-slide="prev">
			<span class="glyphicon glyphicon-chevron-left"></span>
		</a>
		<a href="#carousel" class="right carousel-control" data-slide="next">
			<span class="glyphicon glyphicon-chevron-right"></span>
		</a>
	</div>
	</div>

<%@ include file="jspf/bootstrap.jspf" %>
    <%-- <script src="${context }/js/confirm.js"></script> --%>
</body>
</html>