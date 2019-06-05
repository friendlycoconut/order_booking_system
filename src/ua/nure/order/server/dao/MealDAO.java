package ua.nure.order.server.dao;

import ua.nure.order.client.Paginable;
import ua.nure.order.client.SQLCountWrapper;
import ua.nure.order.entity.meal.Meal;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MealDAO extends Paginable<Meal> {
	/**
	 * Add a meal to order
	 * 

	 * @return
	 * @throws DAOException 
	 */
	public int addMeal(Meal item) throws DAOException;

	/**
	 * Get count of each meal
	 * 

	 * @return
	 * @throws DAOException 
	 */
	public boolean getMealsCount(Set<Meal> meals) throws DAOException;
	
	/**
	 * 
	 * @return All books in order
	 * @throws DAOException 
	 */
	public List<Meal> listMeals(String pattern) throws DAOException;

	@Override
	public List<Meal> list(String pattern, String orderColumn, boolean ascending, int start, int count,
                           SQLCountWrapper total) throws DAOException;
	
	Map<Integer, String> listIngredients() throws DAOException;

	public Meal getMeal(int id) throws DAOException;

	public void updateMeal(Meal meal) throws DAOException;

	public Map<Integer, String> getCategories() throws DAOException;

}
