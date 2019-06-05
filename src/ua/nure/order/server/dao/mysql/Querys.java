package ua.nure.order.server.dao.mysql;

public interface Querys {
	static final String SQL_ADD_MEAL = "INSERT INTO `meal` "
			+ "(`title`, `calory`, `price`, `count`, `category_id`) "
			+ "VALUES (?, ?, ?, ?, ?);";

	static final String SQL_FIND_MEALS = "SELECT `id`, `title`, `ingredients`, "
			+ "`calory`,`price`,  `count`,  `category` FROM `meals` ";

	static final String SQL_FIND_MEAL_BY_ID = "SELECT `id`, `title`, `ingredients`, "
			+ "`calory`,`price`,  `count`, `category` FROM `meals` WHERE ID = ?";

	static final String SQL_FIND_MEALS_COUNT = "SELECT count(*) FROM `meals` ";

	static final String SQL_UPDATE_MEALS_COUNT = "SELECT count(*) FROM `meals` ";

	static final String SQL_DELETE_MEAL_HAS_INGREDIENTS = "DELETE FROM `meal_has_ingredient` WHERE `meal_id`=?";

	static final String SQL_UPDATE_MEAL = "UPDATE `meal` SET `title` = ?,`calory` = ?,`price` = ?,`count` = ?,`category_id`= ?, `description`=? WHERE `id` = ?";

	static final String SQL_GET_CATEGORIES = "SELECT `id`,`title` FROM `category`";


	static final String SQL_ADD_INGREDIENT = "INSERT INTO `ingredient` "
			+ "(`title`) VALUES ";

	static final String SQL_ADD_MEAL_INGREDIENTS = "INSERT INTO `meal_has_ingredient` (`ingredient_id`, `meal_id`) VALUES ";

	static final String SQL_GET_CATEGORY_ID = "SELECT id FROM category WHERE title = ?";

	static final String SQL_GET_INGREDIENTS = "SELECT id, title FROM ingredient WHERE ";

	static final String SQL_GET_MEAL_INGREDIENT = "select ingredient.id, ingredient.title from ingredient "
			+ "inner join meal_has_ingredients on ingredient.id = ingredient_id "
			+ "inner join meal on meal_id = meal.id where meal.id = ?";

	static final String SQL_LIST_INGREDIENTS = "SELECT id, title FROM ingredient";

	static final String SQL_GET_USER = "SELECT `id`,`login`,`password`,`role`,`e-mail`,`phone`,`name`,`address`,`avatar`,`description` FROM `user` WHERE login = ?";

	static final String SQL_ADD_USER = "INSERT INTO `user` (`login`,`password`,`role`,`e-mail`,`phone`,`name`,`address`,`avatar`,`description`) "
			+ "VALUES (?,?,?,?,?,?,?,?,?)";

	static final String SQL_UPDATE_USER = "UPDATE `user` "
			+ "SET `login` = ?,`password` = ?,`role` = ?,`e-mail` = ?,`phone` = ?,"
			+ "`name` = ?,`address` = ?,`avatar` = ?,`description` = ? WHERE `id` = ?;";

	static final String SQL_INSERT_ORDER = "INSERT INTO `order` (`no`, `delivery_id`, `user_id`) VALUES (?, ?, ?);";

	static final String SQL_INSERT_MEAL_HAS_ORDER = "INSERT INTO `meal_has_order` "
			+ "(`meal_id`, `order_id`, `count`) VALUES (?, ?, ?);";

	static final String SQL_INSERT_DELIVERY = "INSERT INTO `delivery` (`name`, `phone`, `email`, `address`,  `description`, `user_id`) VALUES (?, ?, ?, ?, ?, ?)";

	static final String SQL_GET_MEALS_COUNT = "SELECT `id`, `count` FROM `meal` WHERE `id` IN ";

	static final String SQL_GET_FULL_ORDERS = "SELECT `user_id`,`login`,`order_id`,`status`,`meal_id`,`title`,`count`,`price`,`osum` FROM orders ";

	static final String SQL_GET_ORDER_BY_ID = "SELECT `id`,`no`,`user_id`,`date`,`status` FROM `order` WHERE `id` = ?";

	static final String SQL_GET_ORDER_STATUS = "SELECT `id`,`status` FROM `order` WHERE `id` = ?";

	static final String SQL_GET_ORDER_DETAL = "SELECT DISTINCT `orders`.`user_id`,`login`,`order_id`,`status`,"
			+ "`meal_id`,`title`,`count`,`price`,`osum`,`name`,`phone`,`email`,`address`,`description` "
			+ "FROM `orders`,`delivery` WHERE `order_id` = ? AND `delivery_id` = `delivery`.`id`;";

	static final String SQL_UPDATE_ORDER_STATUS = "UPDATE `order` SET `status` = ? WHERE `id` = ?";

	static final String SQL_FIND_ORDERS_COUNT = "SELECT count(*) FROM `order` ";

	static final String SQL_GET_ORDERS_ID = "SELECT DISTINCT `order_id` FROM `orders` ";

}
