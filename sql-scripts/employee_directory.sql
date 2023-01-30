CREATE DATABASE  IF NOT EXISTS `employee_directory`;
USE `employee_directory`;

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `image` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 115
DEFAULT CHARACTER SET = latin1;

DROP TABLE IF EXISTS `sales`;

CREATE TABLE `sales` (
  `sale_id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NULL DEFAULT NULL,
  `date` DATE NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `full_name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`sale_id`),
  INDEX `sales_ibfk_1` (`employee_id` ASC),
  CONSTRAINT `sales_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `employee_directory`.`employee` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 89;

DROP TABLE IF EXISTS `prospects`;

CREATE TABLE `prospects` (
  `prospect_id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NULL DEFAULT NULL,
  `full_name` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(65) NULL DEFAULT NULL,
  PRIMARY KEY (`prospect_id`),
  INDEX `sales_ibfk_2` (`employee_id` ASC),
  CONSTRAINT `sales_ibfk_2`
    FOREIGN KEY (`employee_id`)
    REFERENCES `employee_directory`.`employee` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 48;

DROP TABLE IF EXISTS `prospect_links`;

CREATE TABLE `prospect_links` (
  `link_id` INT NOT NULL AUTO_INCREMENT,
  `prospect_id` INT NULL DEFAULT NULL,
  `linkedin` VARCHAR(2083) NOT NULL,
  `skype` VARCHAR(2083) NOT NULL,
  `facebook` VARCHAR(2083) NOT NULL,
  PRIMARY KEY (`link_id`),
  INDEX `links_ibfk_1` (`prospect_id` ASC),
  CONSTRAINT `links_ibfk_1`
    FOREIGN KEY (`prospect_id`)
    REFERENCES `employee_directory`.`prospects` (`prospect_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 89;

CREATE VIEW monthly_sales AS
    SELECT e.employee_id, d.day, m.month, y.year, SUM(s.amount) AS total_sales, e.employee_id * 10000 + m.month * 100 + y.year + d.day AS id
    FROM (SELECT DISTINCT employee_id FROM sales) e
    CROSS JOIN (SELECT DISTINCT DAY(date) AS day FROM sales) d
    CROSS JOIN (SELECT DISTINCT MONTH(date) AS month FROM sales) m
    CROSS JOIN (SELECT DISTINCT YEAR(date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.employee_id AND DAY(s.date) = d.day AND MONTH(s.date) = m.month AND YEAR(s.date) = y.year
    GROUP BY e.employee_id, d.day, m.month, y.year;

CREATE VIEW yearly_sales AS
    SELECT e.employee_id, s.full_name, m.month, y.year, SUM(s.amount) AS total_sales, e.employee_id * y.year + 1000 * m.month AS id
    FROM (SELECT DISTINCT employee_id FROM sales) e
    CROSS JOIN (SELECT DISTINCT MONTH(date) AS month FROM sales) m
    CROSS JOIN (SELECT DISTINCT YEAR(date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.employee_id AND MONTH(s.date) = m.month AND YEAR(s.date) = y.year
    GROUP BY e.employee_id, m.month, y.year;
    
CREATE VIEW total_yearly_sales AS
    SELECT e.employee_id, s.full_name, y.year, SUM(s.amount) AS total_sales, e.employee_id * y.year AS id
    FROM (SELECT DISTINCT employee_id FROM sales) e
    CROSS JOIN (SELECT DISTINCT YEAR(date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.employee_id AND YEAR(s.date) = y.year
    GROUP BY e.employee_id, y.year;


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`

-- The passwords are encrypted using BCrypt
--
--
-- Default passwords here are: test123
--

INSERT INTO `user` (username,password,first_name,last_name,email)
VALUES 
('employee','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','John','Doe','john@test.com'),
('manager','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','Jane','Doe','jane@test.com'),
('admin','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','Mark','Adams','mark@test.com');



-- Table structure for table `role`


DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- Dumping data for table `roles`


INSERT INTO `role` (name)
VALUES 
('ROLE_EMPLOYEE'),('ROLE_MANAGER'),('ROLE_ADMIN');


-- Table structure for table `users_roles`


DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 1;


-- Dumping data for table `users_roles`


INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(3, 3)
