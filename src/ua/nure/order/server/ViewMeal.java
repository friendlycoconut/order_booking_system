package ua.nure.order.server;

import org.apache.log4j.Logger;
import ua.nure.order.entity.meal.Meal;
import ua.nure.order.server.dao.MealDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Get meal from database by id and forward to view meal page

 * 
 * @author engsyst
 *
 */
public class ViewMeal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ViewMeal.class);
	private MealDAO mealService = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewMeal() {
		super();
	}

	@Override
	public void init() {
		log.trace("init start");
		mealService = (MealDAO) getServletContext().getAttribute("MealDao");
		log.debug("Get BookDao from context -- > " + mealService);
		log.trace("init finish");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.trace("doPost start");
		RequestDispatcher rd = request.getRequestDispatcher("/error.jsp");
		String id = request.getParameter("id");
		Meal meal = null;
		if (id != null)
			try {
				meal = mealService.getMeal(Integer.parseInt(id));
				log.debug("Get Book from dao -- > " + meal);
			} catch (Exception e) {
				log.error("Book with id = " + id + " not found", e.getCause());
				rd.forward(request, response);
			}
		rd = request.getRequestDispatcher("viewmeal.jsp");
		request.setAttribute("meal", meal);
		rd.forward(request, response);
		log.debug("Forvard to viewmeal.jsp");
		log.trace("doPost finish");
	}

}
