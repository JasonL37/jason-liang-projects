-- All 17 tables are shown in this file.
-- This file is also used for database initialization.


-- Trigger Description:
-- For the ISA relationship, we have a total participation and disjoint constraint,
-- so we need to set up a trigger to ensure that every review
-- (to be more specific, it is each tuple of the ReviewBasicInfo table) is either a FoodReviews or a DriverReviews and cannot be both.
-- This checking includes the following process:
-- Step 1:  The trigger should be implemented before any insertion or update in the ReviewBasicInfo table.
-- Step 2:  The trigger checks if the input primary key of ReviewBasicInfo, which is (customer_phone, review_timeStamp),
--          is going to be in either their child entities (FoodReviews or DriverReviews) and should not be in both.
-- Step 3:  If the primary key is not in any of the child tables, or in both, then an error should appear.


-- Initialization Step 1: Drop all tables
DROP TABLE Own;
DROP TABLE Contain;
DROP TABLE Ordered;
DROP TABLE FoodReviews;
DROP TABLE DriverReviews;
DROP TABLE ReviewBasicInfo;
DROP TABLE ReviewAdvancedInfo;
DROP TABLE PaymentBasicInfo;
DROP TABLE PaymentAdvancedInfo;
DROP TABLE Orders;
DROP TABLE Drivers;
DROP TABLE FoodBasicInfo;
DROP TABLE FoodAdvancedInfo;
DROP TABLE MenuBasicInfo;
DROP TABLE MenuAdvancedInfo;
DROP TABLE Restaurants;
DROP TABLE Customers;


-- Initialization Step 2: Create all tables
CREATE TABLE Customers (
    customer_phone varchar(15),
    customer_name varchar(255) not null,
    customer_address varchar(255) not null,
    customer_email varchar(255) not null,
    customer_account varchar(20) not null,
    customer_paymentInfo varchar(255) not null,
    customer_membership varchar(3) DEFAULT 'No',
    customer_points integer not null,
    customer_discount varchar(4),
    PRIMARY KEY (customer_phone),
    UNIQUE (customer_email),
    UNIQUE (customer_account)
);

CREATE TABLE Restaurants (
    restaurant_address varchar(255),
    restaurant_name varchar(255) not null,
    restaurant_openHours varchar(255) not null,
    restaurant_account varchar(20) not null,
    PRIMARY KEY (restaurant_address),
    UNIQUE (restaurant_account)
);

CREATE TABLE MenuAdvancedInfo (
    menu_name varchar(255),
    menu_cuisine varchar(255),
    PRIMARY KEY (menu_name)
);

CREATE TABLE MenuBasicInfo (
    menu_id integer,
    menu_name varchar(255) not null,
    PRIMARY KEY (menu_id),
    FOREIGN KEY (menu_name) REFERENCES MenuAdvancedInfo(menu_name) ON DELETE CASCADE
);

CREATE TABLE FoodAdvancedInfo (
    food_type varchar(255),
    food_hasVeganDiet varchar(3) not null,
    PRIMARY KEY (food_type)
);

CREATE TABLE FoodBasicInfo (
    food_name varchar(255),
    food_price decimal(5, 2) not null,
    food_type varchar(255) not null,
    PRIMARY KEY (food_name),
    FOREIGN KEY (food_type) REFERENCES FoodAdvancedInfo(food_type)
);

CREATE TABLE Drivers (
    driver_phone varchar(15),
    driver_name varchar(255) not null,
    driver_account varchar(20) not null,
    driver_vehicle varchar(255) not null,
    PRIMARY KEY (driver_phone),
    UNIQUE (driver_account)
);

CREATE TABLE Orders (
    order_id integer,
    order_totalPrice decimal(5, 2) not null,
    order_timeArrival timestamp not null,
    customer_phone varchar(15) not null,
    restaurant_address varchar(255) not null,
    driver_phone varchar(15) not null,
    PRIMARY KEY (order_id),
    FOREIGN KEY (customer_phone) REFERENCES Customers(customer_phone),
    FOREIGN KEY (restaurant_address) REFERENCES Restaurants(restaurant_address),
    FOREIGN KEY (driver_phone) REFERENCES Drivers(driver_phone)
);

CREATE TABLE PaymentAdvancedInfo (
    order_id integer,
    payment_price decimal(5, 2) not null,
    restaurant_address varchar(255) not null,
    driver_phone varchar(15) not null,
    PRIMARY KEY (order_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_address) REFERENCES Restaurants(restaurant_address),
    FOREIGN KEY (driver_phone) REFERENCES Drivers(driver_phone)
);

CREATE TABLE PaymentBasicInfo (
    payment_id integer,
    payment_status varchar(7) not null,
    order_id integer not null,
    PRIMARY KEY (payment_id),
    FOREIGN KEY (order_id) REFERENCES PaymentAdvancedInfo(order_id) ON DELETE CASCADE
);

CREATE TABLE ReviewAdvancedInfo (
    review_comment varchar(255),
    review_ratingType varchar(8) not null,
    PRIMARY KEY (review_comment)
);

CREATE TABLE ReviewBasicInfo (
    customer_phone varchar(15),
    review_timeStamp timestamp,
    review_title varchar(50) not null,
    review_image varchar(255),
    review_comment varchar(255) not null,
    PRIMARY KEY (customer_phone, review_timeStamp),
    FOREIGN KEY (customer_phone) REFERENCES Customers(customer_phone) ON DELETE CASCADE,
    FOREIGN KEY (review_comment) REFERENCES ReviewAdvancedInfo(review_comment) ON DELETE CASCADE
);

CREATE TABLE FoodReviews (
    customer_phone varchar(15),
    review_timeStamp timestamp,
    restaurant_address varchar(255) not null,
    foodReviews_foodQualityRating integer,
    foodReviews_portionSizeRating integer,
    PRIMARY KEY (customer_phone, review_timeStamp),
    FOREIGN KEY (customer_phone, review_timeStamp) REFERENCES ReviewBasicInfo(customer_phone, review_timeStamp) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_address) REFERENCES Restaurants(restaurant_address) ON DELETE CASCADE
);

CREATE TABLE DriverReviews (
    customer_phone varchar(15),
    review_timeStamp timestamp,
    driver_phone varchar(15) not null,
    driverReviews_packageHandlingRating integer,
    driverReviews_deliveryTimeRating integer,
    PRIMARY KEY (customer_phone, review_timeStamp),
    FOREIGN KEY (customer_phone, review_timeStamp) REFERENCES ReviewBasicInfo(customer_phone, review_timeStamp) ON DELETE CASCADE,
    FOREIGN KEY (driver_phone) REFERENCES Drivers(driver_phone) ON DELETE CASCADE
);

CREATE TABLE Own (
    restaurant_address varchar(255),
    menu_id integer,
    PRIMARY KEY (restaurant_address, menu_id),
    FOREIGN KEY (restaurant_address) REFERENCES Restaurants(restaurant_address) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES MenuBasicInfo(menu_id) ON DELETE CASCADE
);

CREATE TABLE Contain (
    menu_id integer,
    food_name varchar(255),
    PRIMARY KEY (menu_id, food_name),
    FOREIGN KEY (menu_id) REFERENCES MenuBasicInfo(menu_id) ON DELETE CASCADE,
    FOREIGN KEY (food_name) REFERENCES FoodBasicInfo(food_name) ON DELETE CASCADE
);

CREATE TABLE Ordered (
    food_name varchar(255),
    order_id integer,
    orderedQuantity	integer not null,
    PRIMARY KEY (food_name, order_id),
    FOREIGN KEY (food_name) REFERENCES FoodBasicInfo(food_name),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);


-- Initialization Step 3: Insert some tuples for each table as examples

-- Customers Table
INSERT INTO Customers VALUES
    ('6041112222', 'Alice Smith', '123 Broadway', 'alice@example.com', 'alice001', 'Credit Card', 'yes', 150, '2%');
INSERT INTO Customers VALUES
    ('6043334444', 'Bob Johnson', '456 Broadway', 'bob@example.com', 'bob2002', 'PayPal', 'no', 80, NULL);
INSERT INTO Customers VALUES
    ('6045556666', 'John Ha', '789 Broadway', 'john2002@example.com', 'john2002', 'Debit Card', 'yes', 200, '5%');
INSERT INTO Customers VALUES
    ('6047778888', 'John Brown', '101 Broadway', 'john2001@example.com', 'john2001', 'Credit Card', 'yes', 50, '1%');
INSERT INTO Customers VALUES
    ('6049990000', 'John Green', '202 Broadway', 'john1999@example.com', 'john1999', 'Credit Card', 'no', 180, '4%');

-- Restaurants Table
INSERT INTO Restaurants VALUES
    ('100 Broadway', 'Saladland', '10:00-22:00', 'saladland@fd.com');
INSERT INTO Restaurants VALUES
    ('200 Broadway', 'Burgerland', '11:00-23:00', 'burgerland@fd.com');
INSERT INTO Restaurants VALUES
    ('300 Broadway', 'Pastaland', '09:00-21:00', 'pastaland@fd.com');
INSERT INTO Restaurants VALUES
    ('400 Broadway', 'Noodleland', '08:00-20:00', 'noodleland@fd.com');
INSERT INTO Restaurants VALUES
    ('500 Broadway', 'Sushiland', '12:00-24:00', 'sushiland@fd.com');
INSERT INTO Restaurants VALUES
    ('600 Broadway', 'Pizzaland', '12:00-22:00', 'pizzaland@fd.com');

-- MenuAdvancedInfo Table
INSERT INTO MenuAdvancedInfo VALUES
    ('Salad Menu', 'Italian');
INSERT INTO MenuAdvancedInfo VALUES
    ('Burger Menu', 'American');
INSERT INTO MenuAdvancedInfo VALUES
    ('Pasta Menu', 'Italian');
INSERT INTO MenuAdvancedInfo VALUES
    ('Noodle Menu', 'Chinese');
INSERT INTO MenuAdvancedInfo VALUES
    ('Sushi Menu', 'Japanese');
INSERT INTO MenuAdvancedInfo VALUES
    ('Pizza Menu', 'Italian');

-- MenuBasicInfo Table
INSERT INTO MenuBasicInfo VALUES
    (1, 'Salad Menu');
INSERT INTO MenuBasicInfo VALUES
    (2, 'Burger Menu');
INSERT INTO MenuBasicInfo VALUES
    (3, 'Pasta Menu');
INSERT INTO MenuBasicInfo VALUES
    (4, 'Noodle Menu');
INSERT INTO MenuBasicInfo VALUES
    (5, 'Sushi Menu');
INSERT INTO MenuBasicInfo VALUES
    (6, 'Pizza Menu');

-- FoodAdvancedInfo Table
INSERT INTO FoodAdvancedInfo VALUES
    ('salad', 'yes');
INSERT INTO FoodAdvancedInfo VALUES
    ('burger', 'no');
INSERT INTO FoodAdvancedInfo VALUES
    ('pasta', 'yes');
INSERT INTO FoodAdvancedInfo VALUES
    ('Chinese noodle soup', 'no');
INSERT INTO FoodAdvancedInfo VALUES
    ('sushi', 'yes');
INSERT INTO FoodAdvancedInfo VALUES
    ('pizza', 'yes');

-- FoodBasicInfo Table
INSERT INTO FoodBasicInfo VALUES
    ('classic chicken salad', 6.00, 'salad');
INSERT INTO FoodBasicInfo VALUES
    ('special salad', 12.00, 'salad');
INSERT INTO FoodBasicInfo VALUES
    ('double cheeseburger', 11.00, 'burger');
INSERT INTO FoodBasicInfo VALUES
    ('special burger', 19.00, 'burger');
INSERT INTO FoodBasicInfo VALUES
    ('creamed spinach pasta', 15.00, 'pasta');
INSERT INTO FoodBasicInfo VALUES
    ('beef noodle soup', 18.00, 'Chinese noodle soup');
INSERT INTO FoodBasicInfo VALUES
    ('California Roll', 10.00, 'sushi');
INSERT INTO FoodBasicInfo VALUES
    ('special sushi', 25.00, 'sushi');
INSERT INTO FoodBasicInfo VALUES
    ('medium classic pepperoni pizza', 17.00, 'pizza');
INSERT INTO FoodBasicInfo VALUES
    ('special pizza', 20.00, 'pizza');

-- Drivers Table
INSERT INTO Drivers VALUES
    ('6041111111', 'Robert Chambers', 'rc1994', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6042222222', 'Lucy Armstrong', 'rabbit458', 'Toyota Corolla');
INSERT INTO Drivers VALUES
    ('6043333333', 'Aaron Sharp', 'yankeesforever88', 'Porsche 718 Cayman');
INSERT INTO Drivers VALUES
    ('6044444444', 'Robert Chambers', 'iamkeanureeves777', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6045555555', 'Rafael Walls', 'rflw999', 'Ford Escape');
INSERT INTO Drivers VALUES
    ('6046666666', 'William Robinson', 'robot55123', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6047777777', 'Kirk Stern', 'kkkkk9898', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6048888888', 'Star Dabney', 'starstar95', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6049999999', 'Sheard Wheeler', 'sd1597', 'Tesla Model Y');
INSERT INTO Drivers VALUES
    ('6040000000', 'Vince Sumner', 'vince123', 'Tesla Model Y');

-- Orders Table
INSERT INTO Orders VALUES
    (1, 17.00, TO_TIMESTAMP('2024-07-25 18:30:00', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '100 Broadway', '6041111111');
INSERT INTO Orders VALUES
    (2, 22.00, TO_TIMESTAMP('2024-08-10 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), '6043334444', '200 Broadway', '6042222222');
INSERT INTO Orders VALUES
    (3, 30.00, TO_TIMESTAMP('2024-09-01 12:05:00', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '300 Broadway', '6043333333');
INSERT INTO Orders VALUES
    (4, 18.00, TO_TIMESTAMP('2024-12-01 09:25:00', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '400 Broadway', '6044444444');
INSERT INTO Orders VALUES
    (5, 30.00, TO_TIMESTAMP('2025-02-28 11:10:00', 'YYYY-MM-DD HH24:MI:SS'), '6047778888', '500 Broadway', '6045555555');
INSERT INTO Orders VALUES
    (6, 34.00, TO_TIMESTAMP('2025-07-26 13:45:00', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '600 Broadway', '6046666666');
INSERT INTO Orders VALUES
    (7, 70.75, TO_TIMESTAMP('2024-07-28 18:30:07', 'YYYY-MM-DD HH24:MI:SS'), '6047778888', '100 Broadway', '6041111111');
INSERT INTO Orders VALUES
    (8, 55.18, TO_TIMESTAMP('2024-08-08 15:30:38', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '200 Broadway', '6045555555');
INSERT INTO Orders VALUES
    (9, 6.15, TO_TIMESTAMP('2024-06-01 21:31:15', 'YYYY-MM-DD HH24:MI:SS'), '6043334444', '200 Broadway', '6043333333');
INSERT INTO Orders VALUES
    (10, 90.10, TO_TIMESTAMP('2024-07-27 20:17:55', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '300 Broadway', '6047777777');
INSERT INTO Orders VALUES
    (11, 90.10, TO_TIMESTAMP('2024-07-25 11:18:45', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '200 Broadway', '6040000000');
INSERT INTO Orders VALUES
    (12, 10.10, TO_TIMESTAMP('2024-07-01 18:28:45', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '300 Broadway', '6040000000');
INSERT INTO Orders VALUES
    (13, 27.10, TO_TIMESTAMP('2024-06-01 21:17:35', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '400 Broadway', '6041111111');
INSERT INTO Orders VALUES
    (14, 33.37, TO_TIMESTAMP('2024-06-25 12:16:47', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '500 Broadway', '6043333333');
INSERT INTO Orders VALUES
    (15, 29.38, TO_TIMESTAMP('2024-04-15 10:10:25', 'YYYY-MM-DD HH24:MI:SS'), '6041112222', '600 Broadway', '6044444444');
INSERT INTO Orders VALUES
    (16, 14.25, TO_TIMESTAMP('2024-07-18 08:25:07', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '100 Broadway', '6040000000');
INSERT INTO Orders VALUES
    (17, 28.15, TO_TIMESTAMP('2024-07-07 08:08:55', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '200 Broadway', '6045555555');
INSERT INTO Orders VALUES
    (18, 39.09, TO_TIMESTAMP('2024-07-10 18:59:57', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '500 Broadway', '6044444444');
INSERT INTO Orders VALUES
    (19, 46.55, TO_TIMESTAMP('2024-06-08 07:36:27', 'YYYY-MM-DD HH24:MI:SS'), '6045556666', '600 Broadway', '6041111111');
INSERT INTO Orders VALUES
    (20, 38.63, TO_TIMESTAMP('2024-06-27 17:25:20', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '100 Broadway', '6043333333');
INSERT INTO Orders VALUES
    (21, 42.16, TO_TIMESTAMP('2024-05-25 19:20:28', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '300 Broadway', '6041111111');
INSERT INTO Orders VALUES
    (22, 35.01, TO_TIMESTAMP('2024-05-05 22:33:12', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '400 Broadway', '6040000000');
INSERT INTO Orders VALUES
    (23, 22.83, TO_TIMESTAMP('2024-06-12 14:12:39', 'YYYY-MM-DD HH24:MI:SS'), '6049990000', '500 Broadway', '6046666666');

-- PaymentAdvancedInfo Table
INSERT INTO PaymentAdvancedInfo VALUES
    (1, 17.00, '100 Broadway', '6041111111');
INSERT INTO PaymentAdvancedInfo VALUES
    (2, 22.00, '200 Broadway', '6042222222');
INSERT INTO PaymentAdvancedInfo VALUES
    (3, 30.00, '300 Broadway', '6043333333');
INSERT INTO PaymentAdvancedInfo VALUES
    (4, 18.00, '400 Broadway', '6044444444');
INSERT INTO PaymentAdvancedInfo VALUES
    (5, 30.00, '500 Broadway', '6045555555');
INSERT INTO PaymentAdvancedInfo VALUES
    (6, 34.00, '600 Broadway', '6046666666');
INSERT INTO PaymentAdvancedInfo VALUES
    (7, 70.75, '100 Broadway', '6041111111');
INSERT INTO PaymentAdvancedInfo VALUES
    (8, 55.18, '200 Broadway', '6045555555');
INSERT INTO PaymentAdvancedInfo VALUES
    (9, 6.15, '200 Broadway', '6043333333');
INSERT INTO PaymentAdvancedInfo VALUES
    (10, 90.10, '300 Broadway', '6047777777');
INSERT INTO PaymentAdvancedInfo VALUES
    (11, 90.10, '200 Broadway', '6040000000');
INSERT INTO PaymentAdvancedInfo VALUES
    (12, 10.10, '300 Broadway', '6040000000');
INSERT INTO PaymentAdvancedInfo VALUES
    (13, 27.10, '400 Broadway', '6041111111');
INSERT INTO PaymentAdvancedInfo VALUES
    (14, 33.37, '500 Broadway', '6043333333');
INSERT INTO PaymentAdvancedInfo VALUES
    (15, 29.38, '600 Broadway', '6044444444');
INSERT INTO PaymentAdvancedInfo VALUES
    (16, 14.25, '100 Broadway', '6040000000');
INSERT INTO PaymentAdvancedInfo VALUES
    (17, 28.15, '200 Broadway', '6045555555');
INSERT INTO PaymentAdvancedInfo VALUES
    (18, 39.09, '500 Broadway', '6044444444');
INSERT INTO PaymentAdvancedInfo VALUES
    (19, 46.55, '600 Broadway', '6041111111');
INSERT INTO PaymentAdvancedInfo VALUES
    (20, 38.63, '100 Broadway', '6043333333');
INSERT INTO PaymentAdvancedInfo VALUES
    (21, 42.16, '300 Broadway', '6041111111');
INSERT INTO PaymentAdvancedInfo VALUES
    (22, 35.01, '400 Broadway', '6040000000');
INSERT INTO PaymentAdvancedInfo VALUES
    (23, 22.83, '500 Broadway', '6046666666');

-- PaymentBasicInfo Table
INSERT INTO PaymentBasicInfo VALUES
    (1, 'success', 1);
INSERT INTO PaymentBasicInfo VALUES
    (2, 'success', 2);
INSERT INTO PaymentBasicInfo VALUES
    (3, 'failure', 3);
INSERT INTO PaymentBasicInfo VALUES
    (4, 'success', 3);
INSERT INTO PaymentBasicInfo VALUES
    (5, 'success', 4);
INSERT INTO PaymentBasicInfo VALUES
    (6, 'failure', 5);
INSERT INTO PaymentBasicInfo VALUES
    (7, 'success', 6);
INSERT INTO PaymentBasicInfo VALUES
    (8, 'success', 8);
INSERT INTO PaymentBasicInfo VALUES
    (9, 'ongoing', 9);
INSERT INTO PaymentBasicInfo VALUES
    (10, 'success', 10);
INSERT INTO PaymentBasicInfo VALUES
    (11, 'ongoing', 11);
INSERT INTO PaymentBasicInfo VALUES
    (12, 'success', 12);
INSERT INTO PaymentBasicInfo VALUES
    (13, 'success', 13);
INSERT INTO PaymentBasicInfo VALUES
    (14, 'failure', 14);
INSERT INTO PaymentBasicInfo VALUES
    (15, 'success', 15);
INSERT INTO PaymentBasicInfo VALUES
    (16, 'success', 16);
INSERT INTO PaymentBasicInfo VALUES
    (17, 'failure', 17);
INSERT INTO PaymentBasicInfo VALUES
    (18, 'success', 18);
INSERT INTO PaymentBasicInfo VALUES
    (19, 'success', 19);
INSERT INTO PaymentBasicInfo VALUES
    (20, 'failure', 20);
INSERT INTO PaymentBasicInfo VALUES
    (21, 'success', 21);
INSERT INTO PaymentBasicInfo VALUES
    (22, 'success', 22);
INSERT INTO PaymentBasicInfo VALUES
    (23, 'success', 23);

-- ReviewAdvancedInfo Table
INSERT INTO ReviewAdvancedInfo VALUES
    ('very good', 'positive');
INSERT INTO ReviewAdvancedInfo VALUES
    ('good', 'positive');
INSERT INTO ReviewAdvancedInfo VALUES
    ('not bad', 'positive');
INSERT INTO ReviewAdvancedInfo VALUES
    ('disgusting', 'negative');
INSERT INTO ReviewAdvancedInfo VALUES
    ('smells bad', 'negative');

-- ReviewBasicInfo Table
INSERT INTO ReviewBasicInfo VALUES
    ('6041112222', TO_TIMESTAMP('2024-07-25 19:30:00', 'YYYY-MM-DD HH24:MI:SS'), 'Amazing', 'url_link_1', 'very good');
INSERT INTO ReviewBasicInfo VALUES
    ('6043334444', TO_TIMESTAMP('2024-08-10 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Fine', 'url_link_2', 'good');
INSERT INTO ReviewBasicInfo VALUES
    ('6045556666', TO_TIMESTAMP('2024-09-01 13:05:00', 'YYYY-MM-DD HH24:MI:SS'), 'I like this', 'url_link_3', 'good');
INSERT INTO ReviewBasicInfo VALUES
    ('6045556666', TO_TIMESTAMP('2024-12-01 10:25:00', 'YYYY-MM-DD HH24:MI:SS'), 'Good', 'url_link_4', 'not bad');
INSERT INTO ReviewBasicInfo VALUES
    ('6047778888', TO_TIMESTAMP('2025-02-28 12:10:00', 'YYYY-MM-DD HH24:MI:SS'), 'So bad', 'url_link_5', 'disgusting');
INSERT INTO ReviewBasicInfo VALUES
    ('6049990000', TO_TIMESTAMP('2025-07-26 14:45:00', 'YYYY-MM-DD HH24:MI:SS'), 'Not recommended', 'url_link_6', 'smells bad');
INSERT INTO ReviewBasicInfo VALUES
    ('6041112222', TO_TIMESTAMP('2024-07-27 18:26:03', 'YYYY-MM-DD HH24:MI:SS'), 'Best delivery', 'url_link_7', 'very good');
INSERT INTO ReviewBasicInfo VALUES
    ('6043334444', TO_TIMESTAMP('2024-08-10 15:17:13', 'YYYY-MM-DD HH24:MI:SS'), 'Fast service', 'url_link_8', 'very good');
INSERT INTO ReviewBasicInfo VALUES
    ('6047778888', TO_TIMESTAMP('2024-09-01 09:15:20', 'YYYY-MM-DD HH24:MI:SS'), 'Like the food', 'url_link_9', 'good');
INSERT INTO ReviewBasicInfo VALUES
    ('6049990000', TO_TIMESTAMP('2024-09-01 14:25:33', 'YYYY-MM-DD HH24:MI:SS'), 'Good service', 'url_link_10', 'good');

-- DriverReviews Table
INSERT INTO DriverReviews VALUES
    ('6041112222', TO_TIMESTAMP('2024-07-25 19:30:00', 'YYYY-MM-DD HH24:MI:SS'), '6041111111', 5, 5);
INSERT INTO DriverReviews VALUES
    ('6043334444', TO_TIMESTAMP('2024-08-10 16:00:00', 'YYYY-MM-DD HH24:MI:SS'), '6042222222', 5, 4);
INSERT INTO DriverReviews VALUES
    ('6045556666', TO_TIMESTAMP('2024-09-01 13:05:00', 'YYYY-MM-DD HH24:MI:SS'), '6043333333', 4, 4);

-- FoodReviews Table
INSERT INTO FoodReviews VALUES
    ('6045556666', TO_TIMESTAMP('2024-12-01 10:25:00', 'YYYY-MM-DD HH24:MI:SS'), '400 Broadway', 5, 3);
INSERT INTO FoodReviews VALUES
    ('6047778888', TO_TIMESTAMP('2025-02-28 12:10:00', 'YYYY-MM-DD HH24:MI:SS'), '500 Broadway', 2, 1);
INSERT INTO FoodReviews VALUES
    ('6049990000', TO_TIMESTAMP('2025-07-26 14:45:00', 'YYYY-MM-DD HH24:MI:SS'), '600 Broadway', 1, 2);
INSERT INTO FoodReviews VALUES
    ('6041112222', TO_TIMESTAMP('2024-07-27 18:26:03', 'YYYY-MM-DD HH24:MI:SS'), '400 Broadway', 4, 3);
INSERT INTO FoodReviews VALUES
    ('6043334444', TO_TIMESTAMP('2024-08-10 15:17:13', 'YYYY-MM-DD HH24:MI:SS'), '500 Broadway', 5, 4);
INSERT INTO FoodReviews VALUES
    ('6047778888', TO_TIMESTAMP('2024-09-01 09:15:20', 'YYYY-MM-DD HH24:MI:SS'), '200 Broadway', 4, 3);
INSERT INTO FoodReviews VALUES
    ('6049990000', TO_TIMESTAMP('2024-09-01 14:25:33', 'YYYY-MM-DD HH24:MI:SS'), '100 Broadway', 5, 4);

-- Ordered Table
INSERT INTO Ordered VALUES
    ('classic chicken salad', 1, 1);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 1, 1);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 2, 2);
INSERT INTO Ordered VALUES
    ('creamed spinach pasta', 3, 2);
INSERT INTO Ordered VALUES
    ('beef noodle soup', 4, 1);
INSERT INTO Ordered VALUES
    ('California Roll', 5, 3);
INSERT INTO Ordered VALUES
    ('medium classic pepperoni pizza', 6, 2);
INSERT INTO Ordered VALUES
    ('California Roll', 7, 6);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 8, 8);
INSERT INTO Ordered VALUES
    ('medium classic pepperoni pizza', 9, 9);
INSERT INTO Ordered VALUES
    ('classic chicken salad', 10, 10);
INSERT INTO Ordered VALUES
    ('special salad', 11, 8);
INSERT INTO Ordered VALUES
    ('creamed spinach pasta', 12, 3);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 13, 2);
INSERT INTO Ordered VALUES
    ('classic chicken salad', 14, 4);
INSERT INTO Ordered VALUES
    ('medium classic pepperoni pizza', 15, 1);
INSERT INTO Ordered VALUES
    ('classic chicken salad', 16, 1);
INSERT INTO Ordered VALUES
    ('special salad', 17, 2);
INSERT INTO Ordered VALUES
    ('medium classic pepperoni pizza', 18, 1);
INSERT INTO Ordered VALUES
    ('medium classic pepperoni pizza', 19, 3);
INSERT INTO Ordered VALUES
    ('California Roll', 20, 1);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 21, 2);
INSERT INTO Ordered VALUES
    ('beef noodle soup', 22, 4);
INSERT INTO Ordered VALUES
    ('double cheeseburger', 23, 1);

-- Contain Table
INSERT INTO Contain VALUES
    (1, 'classic chicken salad');
INSERT INTO Contain VALUES
    (2, 'double cheeseburger');
INSERT INTO Contain VALUES
    (3, 'creamed spinach pasta');
INSERT INTO Contain VALUES
    (4, 'beef noodle soup');
INSERT INTO Contain VALUES
    (5, 'California Roll');
INSERT INTO Contain VALUES
    (6, 'medium classic pepperoni pizza');

-- Own Table
INSERT INTO Own VALUES
    ('100 Broadway', 1);
INSERT INTO Own VALUES
    ('200 Broadway', 2);
INSERT INTO Own VALUES
    ('300 Broadway', 3);
INSERT INTO Own VALUES
    ('400 Broadway', 4);
INSERT INTO Own VALUES
    ('500 Broadway', 5);
INSERT INTO Own VALUES
    ('600 Broadway', 6);

