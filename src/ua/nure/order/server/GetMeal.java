package ua.nure.order.server;

import org.apache.log4j.Logger;
import ua.nure.order.entity.meal.Meal;
import ua.nure.order.server.dao.DAOException;
import ua.nure.order.server.dao.MealDAO;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Get meal from database by id and forward to edit meal form
 * @param id in request
 * 
 * @author engsyst
 *
 */
@WebServlet("/meal/get")
public class GetMeal extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GetMeal.class);
	private MealDAO mealService = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMeal() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
    @Override
	public void init() {
    	log.trace("init start");
    	mealService = (MealDAO) getServletContext().getAttribute("MealDao");
    	log.debug("Get MealDao from context -- > " + mealService);
    	log.trace("init finish");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.trace("Start");
		String sId = request.getParameter("id");

		Meal meal = null;
		Map<Integer, String> categories = null;
		try {
			categories = mealService.getCategories();
			log.debug("Found categories -- > " + categories);
			request.setAttribute("categories", categories);
			if (sId == null || sId.isEmpty()) {
				meal = new Meal();
				log.debug("Create new Meal -- > ");
			} else {
				meal = mealService.getMeal(Integer.parseInt(sId));
				log.debug("Found Meal -- > " + meal);
			}
			request.setAttribute("meal", meal);
		} catch (DAOException e) {
			log.error("DB access error --> ", e.getCause());
			request.setAttribute("error", e.getMessage());
		} catch (NumberFormatException e) {
			log.error("Unknown id --> " + sId);
			throw new ServletException("Unknown id -->" + sId);
		}
		request.getRequestDispatcher("mealform.jsp").forward(request, response);

		log.debug("Forward to -- > mealform.jsp");
		log.trace("Finish");
	}
}
