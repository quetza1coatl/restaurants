DELETE FROM user_roles;
DELETE FROM menu_items;
DELETE FROM votes;
DELETE FROM restaurants;
DELETE FROM dishes;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 10;


INSERT INTO users (name, email, password) VALUES
  ('User', 'user@gmail.com', '{noop}user1'), --10
  ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 10),
  ('ROLE_ADMIN', 11);



INSERT INTO restaurants (name, address)
VALUES ('Ресторан 1', 'Адрес 1'), --12
       ('Ресторан 2', 'Адрес 2'),
       ('Ресторан 3', 'Адрес 3');

INSERT INTO dishes (name)
VALUES ('Блюдо 1'), --15
       ('Блюдо 2'),
       ('Блюдо 3'),
       ('Блюдо 4'),
       ('Блюдо 5'),
       ('Блюдо 6'),
       ('Блюдо 7'),
       ('Блюдо 8'),
       ('Блюдо 9'); --23

INSERT INTO menu_items (restaurant_id, dish_id, date, price)
VALUES (12, 15,'2018-12-30', 168),
       (12, 16,'2018-12-30', 257),
       (12, 17,'2018-12-31', 132),
       (13, 18,'2018-12-30', 431),
       (13, 19,'2018-12-30', 323),
       (13, 20,'2018-12-30', 98),
       (14, 21,'2018-12-30', 493),
       (14, 22,'2018-12-30', 321),
       (14, 23,'2018-12-30', 816); --32

INSERT INTO votes (restaurant_id, user_id, date, time)
VALUES (12, 10, '2019-01-05', '09:00:00'),
       (14, 11, '2019-01-05', '08:30:00');