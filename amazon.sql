use amazon;

CREATE TABLE carts(
 id BIGINT NOT NULL AUTO_INCREMENT,
 price DOUBLE NOT NULL,
 PRIMARY KEY(id));

CREATE TABLE users(
 id BIGINT NOT NULL AUTO_INCREMENT,
 username VARCHAR(30) UNIQUE,
 role VARCHAR(30),
 password VARCHAR(100) NOT NULL,
 first_name VARCHAR(30) NOT NULL,
 last_name VARCHAR(30) NOT NULL,
 email VARCHAR(30) UNIQUE,
 number VARCHAR(30) NOT NULL,
 ssn VARCHAR(30) UNIQUE,
 otp VARCHAR(4),
 is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
 cart_id BIGINT,
 PRIMARY KEY(id),
 FOREIGN KEY(cart_id) REFERENCES Carts(id));
 
CREATE TABLE orders(
 id BIGINT NOT NULL AUTO_INCREMENT,
 price DOUBLE NOT NULL,
 date DATETIME(6) NOT NULL,
 user_id BIGINT,
 PRIMARY KEY(id),
 FOREIGN KEY(user_id) REFERENCES Users(id));
 
CREATE TABLE products(
 id BIGINT NOT NULL AUTO_INCREMENT,
 quantity INTEGER,
 name VARCHAR(30) NOT NULL,
 description VARCHAR(30),
 category VARCHAR(30),
 price DOUBLE NOT NULL,
 PRIMARY KEY(id));
 
CREATE TABLE payments(
 id BIGINT NOT NULL AUTO_INCREMENT,
 type varchar(30) NOT NULL,
 amount DOUBLE NOT NULL,
 user_id BIGINT,
 order_id BIGINT,
 PRIMARY KEY(id),
 FOREIGN KEY(order_id) REFERENCES Orders(id),
 FOREIGN KEY(user_id) REFERENCES Users(id));
  
CREATE TABLE cart_items(
  cart_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INTEGER,
  price DOUBLE,
  PRIMARY KEY (cart_id, product_id),
  FOREIGN KEY (cart_id) REFERENCES Carts(id),
  FOREIGN KEY (product_id) REFERENCES Products(id)
);

CREATE TABLE order_items(
  order_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity INTEGER,
  price DOUBLE,
  PRIMARY KEY (order_id, product_id),
  FOREIGN KEY (order_id) REFERENCES Orders(id),
  FOREIGN KEY (product_id) REFERENCES Products(id)
);

DELIMITER $$
CREATE TRIGGER update_product_price_update
AFTER UPDATE ON products
FOR EACH ROW
BEGIN
    DECLARE product_price DOUBLE DEFAULT 0.0;
    
    SELECT NEW.price INTO product_price
    FROM products
    WHERE id = NEW.id;
    
    UPDATE cart_items
    SET price = product_price
    WHERE product_id = NEW.id;
END $$

DELIMITER $$
CREATE TRIGGER update_cart_price_insert
AFTER INSERT ON cart_items
FOR EACH ROW
BEGIN
    DECLARE cart_price DOUBLE DEFAULT 0.0;
    
    SELECT SUM(quantity * price) INTO cart_price
    FROM cart_items
    WHERE cart_id = NEW.cart_id;
    
    UPDATE carts
    SET price = cart_price
    WHERE id = NEW.cart_id;
END $$

DELIMITER $$
CREATE TRIGGER update_cart_price_delete
AFTER DELETE ON cart_items
FOR EACH ROW
BEGIN
    DECLARE cart_price DOUBLE DEFAULT 0.0;
    
    -- Calculate the total cart price
    SELECT SUM(quantity * price) INTO cart_price
    FROM cart_items 
    WHERE cart_id = OLD.cart_id;
    
    IF cart_price IS NULL THEN
        SET cart_price = 0;
    END IF;
    
    -- Update the corresponding cart row with the new total amount
    UPDATE carts
    SET price = cart_price
    WHERE id = OLD.cart_id;
END $$

DELIMITER $$
CREATE TRIGGER update_cart_price_update
AFTER UPDATE ON cart_items
FOR EACH ROW
BEGIN
    DECLARE cart_price DOUBLE DEFAULT 0.0;
    
    -- Calculate the total cart price
    SELECT SUM(quantity * price) INTO cart_price
    FROM cart_items
    WHERE cart_id = NEW.cart_id;
    
    -- Update the corresponding cart row with the new total amount
    UPDATE carts
    SET price = cart_price
    WHERE id = NEW.cart_id;
END $$

DELIMITER $$
CREATE TRIGGER update_order_price_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    DECLARE order_price DOUBLE DEFAULT 0.0;
    
    -- Calculate the total order price
    SELECT SUM(quantity * price) INTO order_price
    FROM order_items
    WHERE order_id = NEW.order_id;
    
    -- Update the corresponding order row with the new total amount
    UPDATE orders
    SET price = order_price
    WHERE id = NEW.order_id;
END $$

DELIMITER $$
CREATE TRIGGER update_order_price_update
AFTER UPDATE ON order_items
FOR EACH ROW
BEGIN
    DECLARE order_price DOUBLE DEFAULT 0.0;
    
    -- Calculate the total order price
    SELECT SUM(quantity * price) INTO order_price
    FROM order_items
    WHERE order_id = NEW.order_id;
    
    -- Update the corresponding order row with the new total amount
    UPDATE orders
    SET price = order_price
    WHERE id = NEW.order_id;
END $$