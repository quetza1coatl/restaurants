DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM restaurants;
DELETE FROM menu;
DELETE FROM dishes;
DELETE FROM votes;
ALTER SEQUENCE global_seq RESTART WITH 10;


INSERT INTO users (name, email, password) VALUES
  ('User', 'user@gmail.com', 'user'), --10
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 10),
  ('ROLE_ADMIN', 11),
  ('ROLE_USER', 11);



INSERT INTO restaurants (name, address)
VALUES ('Ресторан 1', 'г. Минск, ул. Уличная, 5'), --12
       ('Ресторан 2', 'г. Минск, пер. Заплутавший, 119'),
       ('Ресторан 3', 'г. Минск, пр-т Победителей, 1');

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

INSERT INTO menu (restaurant_id, dish_id, date, price)
VALUES (12, 15,'2018-12-30', 168),
       (12, 16,'2018-12-30', 257),
       (12, 17,'2018-12-30', 132),
       (13, 18,'2018-12-30', 431),
       (13, 19,'2018-12-30', 323),
       (13, 20,'2018-12-30', 98),
       (14, 21,'2018-12-30', 493),
       (14, 22,'2018-12-30', 321),
       (14, 23,'2018-12-30', 816); --32

INSERT INTO votes (restaurant_id, user_id)
VALUES (12, 10),
       (14, 11);