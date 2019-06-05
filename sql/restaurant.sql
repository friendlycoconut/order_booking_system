-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema meals
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `restaurant` ;

-- -----------------------------------------------------
-- Schema meals
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `restaurant` DEFAULT CHARACTER SET utf8 ;
USE `restaurant` ;

-- -----------------------------------------------------
-- Table `ingredient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ingredient` ;

CREATE TABLE IF NOT EXISTS `ingredient` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `ingredient_title_idx` (`title` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

CREATE TABLE IF NOT EXISTS `category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  
  
  UNIQUE INDEX `title_UNIQUE` (`title` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `meal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meal` ;

CREATE TABLE IF NOT EXISTS `meal` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `calory` VARCHAR(45) NULL DEFAULT NULL,
  `price` DOUBLE UNSIGNED NOT NULL DEFAULT '0',
  `count` INT(11) UNSIGNED NOT NULL DEFAULT '0',
  `category_id` INT(11) NOT NULL,
  
  `description` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `meal_title_idx` (`title` ASC),
  INDEX `fk_meal_category1_idx` (`category_id` ASC),
  CONSTRAINT `fk_meal_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ingredient_has_meal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meal_has_ingredient` ;

CREATE TABLE IF NOT EXISTS `meal_has_ingredient` (
  `ingredient_id` INT(11) NOT NULL,
  `meal_id` INT(11) NOT NULL,
  PRIMARY KEY (`ingredient_id`, `meal_id`),
  INDEX `fk_meal_has_ingredient_meal1_idx` (`meal_id` ASC),
  INDEX `fk_meal_has_ingredients_ingredient_idx` (`ingredient_id` ASC),
  CONSTRAINT `fk_meal_has_ingredient_ingredient1`
    FOREIGN KEY (`ingredient_id`)
    REFERENCES `ingredient` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_meal_has_ingredient_meal1`
    FOREIGN KEY (`meal_id`)
    REFERENCES `meal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(65) NOT NULL,
  `role` ENUM('client','admin') NOT NULL DEFAULT 'client',
  `e-mail` VARCHAR(45) NULL,
  `phone` VARCHAR(15) NULL,
  `name` VARCHAR(45) NULL,
  `address` VARCHAR(1000) NULL,
  `avatar` VARCHAR(1000) NULL,
  `description` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_login_idx` (`login` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `delivery`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `delivery` ;

CREATE TABLE IF NOT EXISTS `delivery` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `phone` VARCHAR(15) NULL,
  `email` VARCHAR(45) NULL,
  `address` VARCHAR(1000) NULL,
  `description` VARCHAR(1000) NULL,
  `user_id` INT(11) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_delivery_user1_idx` (`user_id` ASC),
  CONSTRAINT `fk_delivery_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `order` ;

CREATE TABLE IF NOT EXISTS `order` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `no` INT(11) NULL,
  `user_id` INT(11) NULL,
  `date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP(),
  `status` ENUM('newed','inprogress','completed','rejected') NULL DEFAULT 'newed',
  `delivery_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_order_user1_idx` (`user_id` ASC),
  INDEX `fk_order_delivery1_idx` (`delivery_id` ASC),
  INDEX `order_date_idx` (`date` DESC),
  INDEX `order_status_idx` (`date` ASC),
  CONSTRAINT `fk_order_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_delivery1`
    FOREIGN KEY (`delivery_id`)
    REFERENCES `delivery` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `meal_has_order`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `meal_has_order` ;

CREATE TABLE IF NOT EXISTS `meal_has_order` (
  `meal_id` INT(11) NOT NULL,
  `order_id` INT(11) NOT NULL,
  `count` INT(11) UNSIGNED NOT NULL,
  `price` DOUBLE UNSIGNED NOT NULL DEFAULT 0.0,
  PRIMARY KEY (`meal_id`, `order_id`),
  INDEX `fk_meal_has_order_order1_idx` (`order_id` ASC),
  INDEX `fk_meal_has_order_meal1_idx` (`meal_id` ASC),
  CONSTRAINT `fk_meal_has_order_meal1`
    FOREIGN KEY (`meal_id`)
    REFERENCES `meal` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_meal_has_order_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `restaurant` ;

-- -----------------------------------------------------
-- function concatingredients
-- -----------------------------------------------------

USE `restaurant`;
DROP function IF EXISTS `concatingredients`;

DELIMITER $$
USE `restaurant`$$
CREATE FUNCTION `concatingredients` (aid INT(11)) RETURNS VARCHAR(512)
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE k INT DEFAULT 0;
    DECLARE retv VARCHAR(512);
    DECLARE temp VARCHAR(45);
	DECLARE cur CURSOR FOR 
      SELECT `ingredient`.`title` FROM `meal_has_ingredient`,`ingredient` 
       WHERE  `meal_has_ingredient`.`meal_id` = `aid` and `meal_has_ingredient`.`ingredient_id` = `ingredient`.`id`;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;
    
    OPEN cur;
    SET retv:='';
    
    REPEAT
		FETCH cur INTO temp;
        IF NOT done THEN
			IF k = 0 THEN
				SET retv:=CONCAT(retv, temp);
                SET k := k +1;
			ELSE
				SET retv:=CONCAT(retv, ",", temp);
                END IF;
		END IF;
	UNTIL done END REPEAT;
    RETURN retv;
END
$$

DELIMITER ;

-- -----------------------------------------------------
-- Placeholder table for view `meals`
-- -----------------------------------------------------
-- CREATE TABLE IF NOT EXISTS `meals` (`id` INT, `title` INT, `ingredients` INT, `calory` INT, `price` INT, `count` INT, `category` INT);

-- -----------------------------------------------------
-- Placeholder table for view `sum_by_order`
-- -----------------------------------------------------
-- CREATE TABLE IF NOT EXISTS `sum_by_order` (`oid` INT, `osum` INT);

-- -----------------------------------------------------
-- Placeholder table for view `orders`
-- -----------------------------------------------------
-- CREATE TABLE IF NOT EXISTS `orders` (`user_id` INT, `login` INT, `order_id` INT, `status` INT, `meal_id` INT, `title` INT, `count` INT, `price` INT, `osum` INT, `delivery_id` INT);

-- -----------------------------------------------------
-- View `meals`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `meals` ;
DROP TABLE IF EXISTS `meals`;
USE `restaurant`;
CREATE  OR REPLACE VIEW `meals` AS SELECT meal.id id, meal.title, concatingredients(meal.id) as `ingredients`, meal.calory calory, meal.price price, meal.`count` `count`, category.title as category
  from meal, category WHERE category.id = category_id;


-- -----------------------------------------------------
-- View `sum_by_order`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `sum_by_order` ;
DROP TABLE IF EXISTS `sum_by_order`;
USE `restaurant`;
CREATE  OR REPLACE VIEW `sum_by_order` AS SELECT `order_id` `oid`, sum(`price` * `count`) `osum` FROM `meal_has_order` GROUP BY `order_id`
;

-- -----------------------------------------------------
-- View `orders`
-- -----------------------------------------------------
DROP VIEW IF EXISTS `orders` ;
DROP TABLE IF EXISTS `orders`;
USE `restaurant`;
CREATE  OR REPLACE VIEW `orders` AS 
SELECT 
        `order`.`user_id` `user_id`,
        (SELECT `login` FROM `user` WHERE `user`.`id` = `order`.`user_id`) `login`,
        `order`.`id` `order_id`,
        `order`.`status` `status`,
        `meal`.`id` `meal_id`,
        `meal`.`title` `title`,
        `meal_has_order`.`count` `count`,
        `meal_has_order`.`price` `price`,
        `sum_by_order`.`osum` `osum`,
        `delivery_id`
    FROM
        `order`,
        `meal`,
        `meal_has_order`,
        `sum_by_order`,
        `delivery`
    WHERE
        `order`.`id` = `meal_has_order`.`order_id`
            AND `meal_has_order`.`meal_id` = `meal`.`id`
            AND `order`.`id` = `sum_by_order`.`oid`
    ORDER BY `order`.`user_id` , `order`.`id` , `meal`.`id`;

USE `restaurant`;

DELIMITER $$

USE `restaurant`$$
DROP TRIGGER IF EXISTS `meal_has_order_BEFORE_INSERT` $$
USE `restaurant`$$
CREATE DEFINER = CURRENT_USER TRIGGER `restaurant`.`meal_has_order_BEFORE_INSERT` BEFORE INSERT ON `meal_has_order` FOR EACH ROW
begin
	SET @price = (select price from meal where id = NEW.`meal_id`);
    SET @newcount = (select count from meal where id = NEW.`meal_id`) - NEW.count;
    SET NEW.price = @price;
    UPDATE meal SET meal.count = @newcount where id = NEW.`meal_id`;
end$$

DELIMITER ;

DELIMITER $$

USE `restaurant`$$
DROP TRIGGER IF EXISTS `restaurant`.`order_AFTER_UPDATE` $$

USE `restaurant`$$
CREATE DEFINER = CURRENT_USER TRIGGER `restaurant`.`order_AFTER_UPDATE` AFTER UPDATE ON `order` FOR EACH ROW
BEGIN
	DECLARE done INT DEFAULT 0;
	DECLARE ocount INT DEFAULT 0;
	DECLARE mealid INT DEFAULT 0;
	DECLARE cur CURSOR FOR 
		SELECT `meal_has_order`.`count` oc, `meal_id` bid FROM `meal_has_order`
		WHERE  `order_id` = NEW.`id`;
    DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET done = 1;	
    IF NEW.`status` = 'rejected' AND OLD.`status` != 'rejected' AND OLD.`status` != 'completed' THEN
		OPEN cur;
		FETCH cur INTO ocount, mealid;
		WHILE done = 0 DO
			UPDATE `meal` SET `count` = (ocount + `count`) WHERE `id` = mealid; 
			FETCH cur INTO ocount, mealid;
		END WHILE; 
	END IF;
END;
$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data inserts
-- -----------------------------------------------------

-- -----------------------------------------------------
-- category
-- -----------------------------------------------------
INSERT INTO `category` (`id`,`title`) VALUES (1,'Первые блюда');
INSERT INTO `category` (`id`,`title`) VALUES (2,'Горячие закуски');
INSERT INTO `category` (`id`,`title`) VALUES (3,'Холодные закуски');
INSERT INTO `category` (`id`,`title`) VALUES (4,'Десерт');

-- -----------------------------------------------------
-- meal
-- -----------------------------------------------------
INSERT INTO `meal` VALUES (1,'Бульон куриный с лапшой ','350',0.41,28,1,''),(2,'Салат \"Цезарь\"','300',300,30,3,''),(3,'Форель под голландским соусом','100',245.5,9,1,''),(4,'Салат ','250',0,8,2,''),(5,'Суп из говядины','300',118.3,4,1,''),(6,'Стейк из лосося','190',148.7,25,2,''),(7,'Уха заморская ','300',125,15,1,'');

-- -----------------------------------------------------
-- ingredient
-- -----------------------------------------------------
INSERT INTO `ingredient` VALUES (38,'арахис'),(31,'бекон'),(7,'Бульон из домашней курицы'),(14,'бульон рыбный'),(1,'Данте Алигьери'),(6,'Джованни Боккаччо'),(21,'зелень.'),(37,'кунжут'),(30,'Куриное филе'),(8,'лапша'),(25,'лимон'),(12,'Лосось'),(17,'лук репчатый'),(10,'морковь'),(16,'морковь'),(13,'окунь морской'),(4,'Оноре де Бальзак'),(18,'перец болгарский'),(19,'помидор'),(33,'помидоры чери'),(24,'сливки'),(26,'сливочный соус.'),(36,'солёные огурцы'),(28,'соус голландский'),(34,'соус.'),(39,'соус.'),(15,'стебель сельдерея'),(32,'сыр пармезан'),(35,'томаты черри'),(22,'Филе лосося'),(27,'Форель'),(2,'Ханс Кристиан Андерсен'),(5,'Хорхе Луис Борхес'),(29,'цитрусовые.'),(20,'чеснок'),(3,'Чинуа Ачебе'),(11,'чипсы из чиабатты.'),(23,'шпинат'),(9,'яйцо перепелиное');

-- -----------------------------------------------------
-- ingredieusercategorynt_has_meal
-- -----------------------------------------------------
INSERT INTO `meal_has_ingredient` VALUES (7,1),(8,1),(9,1),(10,1),(11,1),(16,1),(30,2),(31,2),(32,2),(33,2),(34,2),(27,3),(28,3),(29,3),(34,4),(35,4),(36,4),(37,4),(38,4),(39,4),(10,5),(16,5),(18,5),(19,5),(20,5),(21,5),(22,6),(23,6),(24,6),(25,6),(26,6),(10,7),(12,7),(13,7),(14,7),(15,7),(16,7),(17,7),(18,7),(19,7),(20,7),(21,7);

-- -----------------------------------------------------
-- user
-- -----------------------------------------------------
INSERT INTO `user` (`login`, `password`, `role`) VALUES ('admin', '8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918', 'admin');
INSERT INTO `user` (`login`, `password`, `role`) VALUES ('client', '948FE603F61DC036B5C596DC09FE3CE3F3D30DC90F024C85F3C82DB2CCAB679D', 'client');
