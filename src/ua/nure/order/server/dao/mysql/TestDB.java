package ua.nure.order.server.dao.mysql;

import ua.nure.order.entity.meal.Category;
import ua.nure.order.entity.meal.Ingredient;
import ua.nure.order.entity.meal.Meal;
import ua.nure.order.server.dao.DAOException;
import ua.nure.order.server.dao.DAOFactory;
import ua.nure.order.server.dao.MealDAO;

import java.util.ArrayList;
import java.util.List;

public class TestDB {
	static MealDAO bdao = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getmealDAO();
	
	static void testAddMeal(Meal item) throws DAOException {
		bdao.addMeal(item);
	}
	public static void main(String[] args) throws DAOException {
//		System.out.println(bdao.listMeals(null));
		
		for (int i = 2; i < 22; i++) {
			Ingredient ingredient = new Ingredient("ingredient" + i);
			List<Ingredient> ingredients = new ArrayList<>();
			ingredients.add(ingredient);
			testAddMeal(new Meal(i,
					"name", ingredients, "ISBN-01234-0133",100.00, Category.NONE, 5));
			ingredients.clear();
			
		}
//		testAddBook(new Meal("Вопрос о воде и земле",
//				Arrays.asList(new String[] {"Данте Алигьери"}),
//				"ISBN-01234-0133", 125.0, Category.NONE, 3));
//		testAddBook(new Meal("Божественная комедия",
//				Arrays.asList(new String[] {"Данте Алигьери"}),
//				"ISBN-01234-0123", 125.0, Category.LOVE_NOVEL, 3));
//		testAddBook(new Meal("Сказки",
//				Arrays.asList(new String[] {"Ханс Кристиан Андерсен"}), 
//				"ISBN-01234-0124", 300.0, Category.ACTION, 12));
//		testAddBook(new Meal("И пришло разрушение",
//				Arrays.asList(new String[] {"Чинуа Ачебе"}), 
//				"ISBN-01234-0125", 245.5, Category.ACTION, 4));
//		testAddBook(new Meal("Отец Горио",
//				Arrays.asList(new String[] {"Оноре де Бальзак"}), 
//				"ISBN-01234-0126", 45.5, Category.LOVE_NOVEL, 5));
//		testAddBook(new Meal("Вымыслы",
//				Arrays.asList(new String[] {"Хорхе Луис Борхес"}), 
//				"ISBN-01234-0127", 118.3, Category.FANTASY, 8));
//		testAddBook(new Meal("Декамерон",
//				Arrays.asList(new String[] {"Джованни Боккаччо"}), 
//				"ISBN-01234-0128", 148.7, Category.LOVE_NOVEL, 7));
	}
	
}
