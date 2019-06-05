<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<div class="modal-content">

		<form class="form-horizintal" action="update" id="editmeal" method="post">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="modal-title">Книга</h4>
	</div>
	<div class="modal-body">
		<div class="row">
			<!-- <fieldset> -->

				<!-- Form Name -->
				<!-- <legend>Книга</legend> -->

				<!-- Text input-->
				<div class="form-group required">
					<label class="col-sm-2 control-label" for="textinput">Название блюда<!-- <span class="asteriskField"> * </span> --></label>
					<div class="col-sm-10">
						<input id="textinput" name="title" placeholder="Название"
							class="form-control input-md" required="required" type="text"
							value="${meal.title }">
						<span class="help-block">Название блюда</span>
					</div>
				</div>

				<!-- Appended Input-->
				<div class="form-group required">
					<label class="col-sm-2 control-label" for="ingredient">Ингредиенты</label>
					<div class="col-sm-10">
						<div class="input-group">
							<input id="ingredient" name="ingredient" class="form-control"
								placeholder="Ингредиенты" required="required" type="text"
								value="${meal.ingredients() }">
								<span class="input-group-addon"> </span>
						</div>
						<p class="help-block">Введите ингредиенты через запятую</p>
					</div>
				</div>

			<div class="col-sm-6">
				<!-- Text input-->
				<div class="form-group required">
					<label class="col-sm-12 control-label requiredField" for="price">Цена</label>
					<div class="col-sm-12">
						<input id="price" name="price" placeholder="цена"
							   class="form-control input-md" type="number"
							   min="0" step="0.01" value="${meal.price }">

					</div>
				</div>
			</div>
				<div class="col-sm-6">
				<!-- Text input-->
				<div class="form-group required">
					<label class="col-sm-12 control-label" for="count">Количество</label>
					<div class="col-sm-12">
						<input id="count" name="count" placeholder="Количество"
							class="form-control input-md" required="required"
							type="number" min="0" value="${meal.count }">
						<span class="help-block">В запасе</span>
					</div>
				</div>
				</div>

				<!-- Text input-->
				<div class="form-group">
					<label class="col-sm-2 control-label" for="calory">Вес порции</label>
					<div class="col-sm-10">
						<input id="calory" name="calory" placeholder="Вес порции"
							class="form-control input-md" type="text"
							value="${meal.calory }">
							<span class="help-block">Вес порции (в гр.)</span>
					</div>
				</div>

				<!-- Select Basic -->
				<div class="form-group">
					<label class="col-sm-2 control-label" for="category">Категория блюда</label>
					<div class="col-sm-10">
						<select id="category" name="category" class="form-control">
							<c:forEach var="cat" items="${categories }">
								<c:set var="c" value="${meal.category.ordinal() }" ></c:set>
								<option ${cat.key == c ? 'selected="selected"' : '' }
									value="${cat.value }" >${cat.value }</option>
							</c:forEach>
						</select>
						<span class="help-block">Выберите одну категорию</span>
					</div>
				</div>

				<!-- Textarea -->
				<div class="form-group">
					<label class="col-sm-2 control-label" for="description">Описание</label>
					<div class="col-sm-10">
						<textarea class="form-control" id="description"
							name="description">${meal.description }</textarea>
						<span class="help-block">Добавьте аннотацию к блюду</span>
					</div>
				</div>

				<!-- File Button -->


			<!-- </fieldset> -->
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-success" type="submit" form="editmeal" name="id" value="${meal.id }">Подтвердить</button>
		<button class="btn btn-danger" type="button" data-dismiss="modal">Отмена</button>
	</div>
		</form>
</div>
