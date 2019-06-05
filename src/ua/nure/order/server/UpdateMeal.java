package ua.nure.order.server;

import org.apache.log4j.Logger;
import ua.nure.order.entity.meal.Category;
import ua.nure.order.entity.meal.Meal;
import ua.nure.order.server.dao.DAOException;
import ua.nure.order.server.dao.InsertException;
import ua.nure.order.server.dao.MealDAO;
import ua.nure.order.server.dao.UpdateException;
import ua.nure.order.shared.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Get book from web-form and updates it in database
 *
 * @author engsyst
 *
 */
@WebServlet("/meal/update")
public class UpdateMeal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UpdateMeal.class);
	private MealDAO mealService = null;

	@Override
	public void init() {
		log.trace("init start");
		mealService = (MealDAO) getServletContext().getAttribute("MealDao");
		log.debug("Get BookDao from context -- > " + mealService);
		log.trace("init finish");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Meal meal = createMeal(request);
		log.debug("Meal --> " + meal);
		try {
			if (meal.getId() == null) {
				meal.setId(mealService.addMeal(meal));
			} else {
				mealService.updateMeal(meal);
			}
		} catch (InsertException e) {
			log.error(e.getMessage(), e.getCause());
			request.setAttribute("error", "Невозможно добавить книгу. Повторите запрос позже.");
		} catch (UpdateException e) {
			log.error(e.getMessage(), e.getCause());
			request.setAttribute("error", "Невозможно обновить книгу. Повторите запрос позже.");
		} catch (DAOException e) {
			log.error(e.getMessage(), e.getCause());
			request.setAttribute("error", "Ошибка доступа базе данных. Повторите запрос позже.");
		}
		request.setAttribute("meal", meal);
		response.sendRedirect("list.jsp");
	}

	private Meal createMeal(HttpServletRequest request) {
		Meal meal = new Meal();
		try {
			String param = request.getParameter("id");
			log.trace("param id --> " + param);
			meal.setId(Util.getIntOrElse(param, null));
			param = request.getParameter("title");
			log.trace("param title --> " + param);
			meal.setTitle(param);
			param = request.getParameter("ingredient");
			log.trace("param author --> " + param);
			meal.setIngredient(param);
			param = request.getParameter("calory");
			log.trace("param isbn --> " + param);
			meal.setCalory(param);
			param = request.getParameter("category");
			log.trace("param category --> " + param);
			meal.setCategory(Category.fromValue(param));
			param = request.getParameter("price");
			log.trace("param price --> " + param);
			meal.setPrice(Util.getDoubleOrElse(param, null));
			param = request.getParameter("count");
			log.trace("param count --> " + param);
			meal.setCount(Util.getIntOrElse(param, null));
			param = request.getParameter("description");
			log.trace("param description --> " + param);
			meal.setDescription(param);

		} catch (Exception e) {
			// do nothing
		}
		return meal;
	}

	private Meal updateMeal(Meal meal, HttpServletRequest request) {
		String param = Util.getOrElse(request.getParameter("id"), "");
//		if (param.isEmpty())
//			crea
//		if (meal.getId() == null) {
//			meal.setId(Util.getIntOrElse(param, null));
//		}
		param = request.getParameter("title");
		if (meal.getTitle() == null) {
			meal.setTitle(param);
		} else if (param != null) {
			meal.setTitle(param);
		}
		param = request.getParameter("ingredient");
		if (meal.getIngredient() == null) {
			meal.setIngredient(param);
		} else if (param != null) {
			meal.setIngredient(param);
		}
		param = request.getParameter("calory");
		if (meal.getCalory() == null) {
			meal.setCalory(param);
		} else if (param != null) {
			meal.setCalory(param);
		}
		param = request.getParameter("price");
		Double p = Util.getDoubleOrElse(request.getParameter("price"), null);
		if (meal.getPrice() == 0.) {
			meal.setPrice(p);
		} else if (p != null) {
			meal.setPrice(p);
		}
		param = request.getParameter("price");
		Integer c = Util.getIntOrElse(request.getParameter("count"), null);
		if (meal.getCount() == 0) {
			meal.setPrice(c);
		} else if (c != null) {
			meal.setPrice(c);
		}
		param = request.getParameter("description");
		if (meal.getDescription() == null) {
			meal.setDescription(param);
		} else if (param != null) {
			meal.setDescription(param);
		}
		// meal.setCover(request.getParameter("cover"));
		return meal;
	}

}
