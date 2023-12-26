
-- CREATE DATABASE MANUALLY FOR POSTGRES

DROP VIEW IF EXISTS monthly_sales;
DROP VIEW IF EXISTS yearly_sales;
DROP VIEW IF EXISTS total_yearly_sales;
DROP TABLE IF EXISTS prospect_links;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS prospects;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS users_roles;
DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS "role";


CREATE TABLE employee (
  id SERIAL PRIMARY KEY,
  first_name VARCHAR(45) NULL,
  last_name VARCHAR(45) NULL,
  email VARCHAR(45) NULL,
  image VARCHAR(45) NULL,
  image_data bytea NULL
);

CREATE TABLE prospects (
  prospect_id SERIAL PRIMARY KEY,
  employee_id INT NULL,
  full_name VARCHAR(45) NULL,
  email VARCHAR(65) NULL,
  CONSTRAINT sales_fk_2
    FOREIGN KEY (employee_id)
    REFERENCES employee (id)
);

CREATE TABLE prospect_links (
  link_id SERIAL PRIMARY KEY,
  prospect_id INT NULL,
  linkedin VARCHAR(2083) NOT NULL,
  instagram VARCHAR(2083) NOT NULL,
  facebook VARCHAR(2083) NOT NULL,
  CONSTRAINT links_fk_1
    FOREIGN KEY (prospect_id)
    REFERENCES prospects (prospect_id)
);

CREATE TABLE sales (
  sale_id SERIAL PRIMARY KEY,
  employee_id INT NULL,
  date DATE NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  full_name VARCHAR(45) NULL,
  CONSTRAINT sales_fk_1
    FOREIGN KEY (employee_id)
    REFERENCES employee (id)
);

CREATE VIEW monthly_sales AS
    SELECT e.id AS employee_id, d.day, m.month, y.year, SUM(s.amount) AS total_sales, e.id * 10000 + m.month * 100 + y.year + d.day AS id
    FROM (SELECT DISTINCT id FROM employee) e
    CROSS JOIN (SELECT DISTINCT EXTRACT(DAY FROM date) AS day FROM sales) d
    CROSS JOIN (SELECT DISTINCT EXTRACT(MONTH FROM date) AS month FROM sales) m
    CROSS JOIN (SELECT DISTINCT EXTRACT(YEAR FROM date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.id AND EXTRACT(DAY FROM s.date) = d.day AND EXTRACT(MONTH FROM s.date) = m.month AND EXTRACT(YEAR FROM s.date) = y.year
    GROUP BY e.id, d.day, m.month, y.year;

CREATE VIEW yearly_sales AS
    SELECT e.id AS employee_id, s.full_name, m.month, y.year, SUM(s.amount) AS total_sales, e.id * y.year + 1000 * m.month AS id
    FROM (SELECT DISTINCT id FROM employee) e
    CROSS JOIN (SELECT DISTINCT EXTRACT(MONTH FROM date) AS month FROM sales) m
    CROSS JOIN (SELECT DISTINCT EXTRACT(YEAR FROM date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.id AND EXTRACT(MONTH FROM s.date) = m.month AND EXTRACT(YEAR FROM s.date) = y.year
    GROUP BY e.id, s.full_name, m.month, y.year;

CREATE VIEW total_yearly_sales AS
    SELECT e.id AS employee_id, s.full_name, y.year, SUM(s.amount) AS total_sales, e.id * y.year AS id
    FROM (SELECT DISTINCT id FROM employee) e
    CROSS JOIN (SELECT DISTINCT EXTRACT(YEAR FROM date) AS year FROM sales) y
    INNER JOIN sales s ON s.employee_id = e.id AND EXTRACT(YEAR FROM s.date) = y.year
    GROUP BY e.id, s.full_name, y.year;

CREATE TABLE "user" (
  id SERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(80) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL,
  image VARCHAR(45) NULL,
  image_data bytea NULL
);

INSERT INTO "user" (username, password, first_name, last_name, email)
VALUES 
('employee','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','John','Doe','john@test.com'),
('manager','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','Jane','Doe','jane@test.com'),
('admin','$2a$12$Qc0GPgAqWdbVBRfps93TUOQCylsWgTeLQ4nsI3lkd8xKUjCL8/7Ie','Mark','Adams','mark@test.com');

CREATE TABLE "role" (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) DEFAULT NULL
);

INSERT INTO "role" (name)
VALUES 
('ROLE_EMPLOYEE'),('ROLE_MANAGER'),('ROLE_ADMIN');

CREATE TABLE users_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);


INSERT INTO users_roles (user_id, role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1),
(3, 2),
(3, 3);
