package ua.nure.order.server.dao.mysql;

import org.apache.log4j.Logger;
import ua.nure.order.client.SQLCountWrapper;
import ua.nure.order.entity.meal.Category;
import ua.nure.order.entity.meal.Ingredient;
import ua.nure.order.entity.meal.Meal;
import ua.nure.order.server.dao.DAOException;
import ua.nure.order.server.dao.InsertException;
import ua.nure.order.server.dao.MealDAO;
import ua.nure.order.server.dao.UpdateException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class MysqlMealDAO implements MealDAO {
	private static final Logger log = Logger.getLogger(MysqlMealDAO.class);

	private static MysqlMealDAO dao;

	public static MealDAO getInstance() {
		log.trace("Try get instance.");
		if (dao == null) {
			log.trace("Instance not found. Create new.");
			dao = new MysqlMealDAO();
		}
		return dao;
	}

	void mapMeal(PreparedStatement st, Meal item) throws SQLException {
		// meal` (`title`, `calory`, `count`, `category_id`)
		int k = 0;
		st.setString(++k, item.getTitle());
		st.setString(++k, item.getCalory());
		st.setDouble(++k, item.getPrice());

		st.setInt(++k, item.getCount());
		st.setInt(++k, item.getCategory().ordinal() + 1);
	}

	Meal unmapMeal(ResultSet rs) throws SQLException {
		List<Ingredient> ingredients = new ArrayList<>();
		String[] a = rs.getString("ingredients").split(",");
		for (String title : a) {
			ingredients.add(new Ingredient(title));
		}
		Meal meal = new Meal(rs.getInt("id"), rs.getString("title"), ingredients, rs.getString("calory"),
				rs.getDouble("price"), Category.fromValue(rs.getString("category")), rs.getInt("count"));
		return meal;
	}

	List<Integer> getExistedIngredients(Connection con, List<Ingredient> auth) throws SQLException {
		log.trace("Start");
		// Make query string
		StringBuilder sb = new StringBuilder();
		for (Ingredient a : auth) {
			sb.append("title = '");
			sb.append(a.getTitle());
			sb.append("' or ");
		}
		sb.delete(sb.lastIndexOf("'")+1, sb.length());
		sb.append(";");
		String getQuery = Querys.SQL_GET_INGREDIENTS + sb.toString();
		log.debug("Query --> " + getQuery);

		List<Integer> res = new ArrayList<>();
		PreparedStatement st = null;
		try {
			log.debug("Try execute");
			st = con.prepareStatement(getQuery);
			ResultSet rs = st.executeQuery();
			log.debug("Try get result");
			while (rs.next()) {
				res.add(rs.getInt(1));
				auth.remove(new Ingredient(rs.getString(2)));
			}
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result --> " + res);
		log.trace("Finish");
		return res;

	}

	@Override
	public int addMeal(Meal item) throws InsertException {
		Connection con = null;
		int mealid = 0;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try add meal");
			mealid = addMeal(con, item);
			con.commit();
		} catch (SQLException e) {

			MysqlDAOFactory.rollback(con);
			log.error("Can not add meal", e);
			throw new InsertException("Can not add meal", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
		return mealid;
	}

	int addMeal(Connection con, Meal item) throws SQLException {
		log.trace("Start");
		int mealid = 0;
		PreparedStatement st = null;
		try {
			log.debug("Query --> " + Querys.SQL_ADD_MEAL);
			log.debug("Meal --> " + item);
			st = con.prepareStatement(Querys.SQL_ADD_MEAL, PreparedStatement.RETURN_GENERATED_KEYS);
			mapMeal(st, item);
			int count = st.executeUpdate();
			if (count == 0) {
				throw new SQLException("addIngredients: No data inserted");
			}
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next()) {
				mealid = rs.getInt(1);
				log.debug("Inserted meal id --> " + mealid);
			} else {
				log.error("No data inserted");
				throw new SQLException("addMeal: No data inserted");
			}

			log.debug("Try add ingredients");
			List<Integer> aIds = addIngredients(con, item.getIngredient());
			String q = Querys.SQL_ADD_MEAL_INGREDIENTS + SqlUtil.pairToValues(aIds, mealid);
			log.debug("Query --> " + q);
			st = con.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS);
			count = st.executeUpdate();

		} finally {
			st.close();
		}
		log.debug("Result --> " + mealid);
		log.trace("Finish");
		return mealid;

	}

	@Override
	public List<Meal> listMeals(String pattern) throws DAOException {
		return list(pattern, "title", true, 0, 0, null);
	}

	@Override
	public List<Meal> list(String pattern, String orderColumn, boolean ascending,
						   int start, int count, SQLCountWrapper total) throws DAOException {
		log.trace("Start");
		List<Meal> meals = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try list meal with pattern --> " + pattern + "; orderColumn --> " + orderColumn
					+ "; ascending --> " + ascending + "; start --> " + start + "; count --> " + count);
			meals = listMeals(con, pattern, orderColumn, ascending, start, count, total);
		} catch (SQLException e) {
			log.error("Can not listMeals", e);
			throw new DAOException("Can not listMeals", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
		log.trace("Finish");
		return meals;
	}

	List<Meal> listMeals(Connection con, String pattern, String orderColumn, boolean ascending,
						 int start, int count, SQLCountWrapper total) throws SQLException {
		log.trace("Start");
		List<Meal> meals = new ArrayList<>();
		PreparedStatement st = null;
		try {
			String where = pattern == null || pattern.length() == 0 ? "" :
				" WHERE title LIKE '%" + pattern + "%' OR ingredients LIKE '%" + pattern + "%' ";
			String order = orderColumn == null || orderColumn.length() == 0 ? "" : "ORDER BY "
					+ orderColumn + (ascending ? " ASC" : " DESC");
			String limit = (count == 0 ? "" : " LIMIT " + start + "," + count);
			String query = Querys.SQL_FIND_MEALS + where + order + limit;
			log.debug("Query --> " + query);
			st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				meals.add(unmapMeal(rs));
			}
			st.close();
			if (total != null) {
				log.debug("Try get total meals.");
				query = Querys.SQL_FIND_MEALS_COUNT + where;
				log.debug("Query --> " + query);
				st = con.prepareStatement(query);
				rs = st.executeQuery();
				while (rs.next()) {
					total.setCount(rs.getInt(1));
					log.debug("Total --> " + total);
				}
			}
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result -- >" + meals);
		log.trace("Finish");
		return meals;
	}

	@Override
	public Map<Integer, String> listIngredients() throws DAOException {
		log.trace("Start");
		Map<Integer, String> ingredients = null;
		Connection con = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try list ingredients.");
			ingredients = listIngredients(con);
		} catch (SQLException e) {
			log.error("Can not add meal", e);
			throw new DAOException("Can not add meal", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
		log.trace("Finish");
		return ingredients;
	}

	public Map<Integer, String> listIngredients(Connection con) throws SQLException {
		log.trace("Start");
		Map<Integer, String> ingredients = new TreeMap<>();
		PreparedStatement st = null;
		try {
			log.debug("Query --> " + Querys.SQL_LIST_INGREDIENTS);
			st = con.prepareStatement(Querys.SQL_LIST_INGREDIENTS);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				ingredients.put(rs.getInt(1), rs.getString(2));
			}
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}

		log.debug("Result -- >" + ingredients);
		log.trace("Finish");
		return ingredients;
	}

	List<Integer> addIngredients(Connection con, List<Ingredient> list) throws SQLException {
		log.trace("Start");
		assert list.isEmpty() : "Empty ingredients";
		List<Ingredient> auth = new ArrayList<>(list);
		log.debug("Make copy of meal ingredients --> " + auth);
		log.debug("Try get Existed ingredients and remove it from insert -- >");
		List<Integer> res = getExistedIngredients(con, auth);
		if (auth.isEmpty()) {
			log.debug("Nothing to add.");
			log.trace("Finish");
			return res;
		}
		List<String> toAdd = new ArrayList<>();
		for (Ingredient a : auth) {
			toAdd.add(a.getTitle());
		}
		String addQuery = Querys.SQL_ADD_INGREDIENT + SqlUtil.listToValues(toAdd);
		PreparedStatement st = null;
		try {
			log.debug("Query -- >" + addQuery);
			st = con.prepareStatement(addQuery, PreparedStatement.RETURN_GENERATED_KEYS);
			int count = st.executeUpdate();
			if (count == 0) {
				log.error("No data inserted");
				throw new SQLException("addAuthors: No data inserted");
			}
			ResultSet rs = st.getGeneratedKeys();
			while (rs.next()) {
				res.add(rs.getInt(1));
			}
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result -- >" + res);
		log.trace("Finish");
		return res;
	}

	@Override
	public Meal getMeal(int id) throws DAOException {
		log.trace("Start");
		Connection con = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try get meal --> " + id);
			return getMeal(con, id);
		} catch (SQLException e) {
			log.error("listMeal: Can not listMeals", e);
			throw new DAOException("Can not listMeals", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
	}

	public Meal getMeal(Connection con, int id) throws SQLException {
		log.trace("Start");
		Meal meal = null;
		PreparedStatement st = null;
		try {
			log.debug("Query --> " + Querys.SQL_FIND_MEAL_BY_ID);
			st = con.prepareStatement(Querys.SQL_FIND_MEAL_BY_ID);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return unmapMeal(rs);
			}
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result -- >" + meal);
		log.trace("Finish");
		return meal;
	}

	@Override
	public boolean getMealsCount(Set<Meal> meals) throws DAOException {
		log.trace("Start");
		Connection con = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try get meals count.");
			return getMealsCount(con, meals);
		} catch (SQLException e) {
			log.error("Can not get meal count", e);
			throw new DAOException("Can not get meal count", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
	}

	boolean getMealsCount(Connection con, Set<Meal> meals) throws SQLException {
		log.trace("Start");
		if (meals == null || meals.isEmpty()) {
			log.debug("Initial --> " + meals);
			return false;
		}
		PreparedStatement st = null;
		List<Integer> ids = new ArrayList<>();
		for (Meal b : meals) {
			ids.add(b.getId());
		}
		log.debug("Initial --> " + meals);
		try {
			String query = Querys.SQL_GET_MEALS_COUNT + SqlUtil.listToIN(ids);
			log.debug("Query --> " + query);
			st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				int c = rs.getInt(2);
				for (Meal b : meals) {
					if (b.getId() == id) {
						b.setCount(c);
						break;
					}
				}
			}
			log.debug("Result -- >" + meals);
			log.trace("Finish");
			return true;
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
	}

	@Override
	public void updateMeal(Meal item) throws UpdateException {
		// TODO
		// 1. ?????????
		// getMeal with id
		// replace changed fields
		// updateMeal
		// OR
		// 2. ????????????
		// simply updateMeal
		log.trace("Start");
		Connection con = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try add meal");
			updateMeal(con, item);
			con.commit();
		} catch (SQLException e) {
			MysqlDAOFactory.rollback(con);
			log.error("Can not update meal", e);
			throw new UpdateException("Can not update meal", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
		log.trace("Finish");
	}

	void updateMeal(Connection con, Meal item) throws SQLException {
		log.trace("Start");
		Meal meal = null;
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(Querys.SQL_UPDATE_MEAL);
			int k = 0;
			// SET `title` = ?,`calory` = ?,`count` = ?,`category_id`= ?,`cover` = ?,`description`= ? WHERE `id` = ?;

			st.setString(++k, item.getTitle());
			st.setString(++k, item.getCalory());
			st.setDouble(++k, item.getPrice());
			st.setInt(++k, item.getCount());
			st.setInt(++k, item.getCategory().ordinal() + 1);

			st.setString(++k, item.getDescription());
			st.setInt(++k, item.getId());
			log.debug("Query --> " + st);
			st.executeUpdate();
			st.close();
			st = con.prepareStatement(Querys.SQL_DELETE_MEAL_HAS_INGREDIENTS);
			st.setInt(1, item.getId());
			log.debug("Query --> " + st);
			st.executeUpdate();
			log.debug("Try add authors");
			List<Integer> aIds = addIngredients(con, item.getIngredient());
			String q = Querys.SQL_ADD_MEAL_INGREDIENTS + SqlUtil.pairToValues(aIds, item.getId());
			log.debug("Query --> " + q);
			st = con.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS);
			st.executeUpdate();
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result -- >" + meal);
		log.trace("Finish");
	}

	@Override
	public Map<Integer, String> getCategories() throws DAOException {
		Connection con = null;
		Map<Integer, String> cats = null;
		try {
			con = MysqlDAOFactory.getConnection();
			log.debug("Try add meal");
			cats = getCategories(con);
			con.commit();
		} catch (SQLException e) {
			MysqlDAOFactory.rollback(con);
			log.error("Can not update meal", e);
			throw new UpdateException("Can not update meal", e);
		} finally {
			MysqlDAOFactory.close(con);
		}
		return cats;
	}

	Map<Integer, String> getCategories(Connection con) throws SQLException {
		log.trace("Start");
		LinkedHashMap<Integer, String> cats = new LinkedHashMap<>();
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(Querys.SQL_GET_CATEGORIES);
			log.debug("Query --> " + st);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				cats.put(rs.getInt(1), rs.getString(2));
			}
			rs.close();
		} finally {
			MysqlDAOFactory.closeStatement(st);
		}
		log.debug("Result -- >" + cats);
		log.trace("Finish");
		return cats;
	}

}
