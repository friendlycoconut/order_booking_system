package ua.nure.order.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ua.nure.order.entity.meal.Meal;

import ua.nure.order.server.dao.DAOException;
import ua.nure.order.server.dao.DAOFactory;
import ua.nure.order.server.dao.MealDAO;

/**
 * Wrapper class used to get data from the {@link Cart} at jsp.
 * @author engsyst
 *
 */
public class CartMealDAO implements Paginable<Meal> {
	
	Cart<Meal> cart;
	
	public Cart<Meal> getCart() {
		return cart;
	}

	public void setCart(Cart<Meal> cart) {
		this.cart = cart;
	}

	Comparator<Meal> titleAsc  = new Comparator<Meal>() {

		@Override
		public int compare(Meal o1, Meal o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	};

	Comparator<Meal> titleDesc  = new Comparator<Meal>() {
		
		@Override
		public int compare(Meal o1, Meal o2) {
			return o2.getTitle().compareTo(o1.getTitle());
		}
	};
	
	Comparator<Meal> price  = new Comparator<Meal>() {
		
		@Override
		public int compare(Meal o1, Meal o2) {
			return Double.compare(o1.getPrice(), o2.getPrice());
		}
	};
	
	Comparator<Meal> countComp  = new Comparator<Meal>() {
		
		@Override
		public int compare(Meal o1, Meal o2) {
			return Integer.compare(o1.getCount(), o2.getCount());
		}
	};
	
	Comparator<Meal> calory  = new Comparator<Meal>() {
		
		@Override
		public int compare(Meal o1, Meal o2) {
			return o1.getCalory().compareTo(o2.getCalory());
		}
	};

	Comparator<Meal> author  = new Comparator<Meal>() {
		
		@Override
		public int compare(Meal o1, Meal o2) {
			return o1.getIngredient().get(0).getTitle().compareTo(o2.getIngredient().get(0).getTitle());
		}
	};
	
	@Override
	public List<Meal> list(String pattern, String orderColumn, boolean ascending, int start, int count,
			SQLCountWrapper total) throws DAOException {
		Comparator<Meal> comparator = null;
		switch (orderColumn) {
		case "title":
			comparator = ascending ? titleAsc : titleDesc;
			break;
		case "author":
			comparator = author;
			break;
		case "count":
			comparator = countComp;
			break;
		case "price":
			comparator = price;
			break;
		default:
			comparator = titleAsc;
			break;
		}
		if (cart == null) {
			total.setCount(0);
			return new ArrayList<Meal>();
		}
		MealDAO bdao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getmealDAO();
		bdao.getMealsCount(cart.keySet());
		Set<Meal> b = cart.keySet();
		ArrayList<Meal> a = new ArrayList<>();
		for (Meal book : b) {
			a.add(book);
		}
		Collections.sort(a, comparator);
		total.setCount(a.size());
		return a;
	}
	
}
