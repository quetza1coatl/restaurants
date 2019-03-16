## REST API VOTING SYSTEM
Система голосования, позволяющая пользователям каждый день выбирать ресторан, в котором они хотели бы пообедать, в зависимости от меню.  
Выбор осуществляется из списка ресторанов, в котором есть меню на текущий день.  
Два вида пользователей: администратор (роль ROLE_ADMIN) и обычный пользователь (роль ROLE_USER).  
Обычный пользователь может проголосовать в течение дня (и менять свой голос до 11:00).  
Администратор добавляет рестораны, обновляет их меню каждый день (добавляет элементы меню текущего дня, 2-5 записи с указанием текущей даты), при необходимости расширяет справочник еды.  
Доступные ресурсы для администратора:  
localhost:8080/rest/admin/*  
localhost:8080/rest/restaurants/*  
localhost:8080/rest/menu-items/*  
localhost:8080/rest/dishes  
Доступный ресурс для обычных авторизованных пользователей:  
localhost:8080/rest/vote/restaurants/*  
Доступный ресурс для неавторизованных пользователей:  
localhost:8080/rest/profile/register  
  
## CURL
Для авторизации после curl-команды вводятся креденшелы в формате `--user {mail}:{password}`, например:  
`curl -s http://localhost:8080/rest/restaurants --user admin@gmail.com:admin`  
Дефолтные креденшелы:  
администратор: admin@gmail.com:admin  
обычный пользователь: user@gmail.com:user1  

###  localhost:8080/rest/restaurants/*   
Ресурс предназначен для работы с ресторанами, а также предоставляет историю меню и голосований для конкретного ресторана.  
  
  
#### get all Restaurants  
`curl -s http://localhost:8080/rest/restaurants`  
#### create new restaurant  
`curl -s -X POST -d '{"name":"Restaurant 4","address":"Paris"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants` 
#### get restaurant with ID=12
`curl -s http://localhost:8080/rest/restaurants/12`
#### delete restaurant with ID=12
`curl -s -X DELETE http://localhost:8080/rest/restaurants/12`  

#### get menu items history for restaurant with ID=14
`curl -s http://localhost:8080/rest/restaurants/14/menu-history`  
#### get vote history for restaurant with ID=12 
(returns vote id, restaurant id, user id, datetime of voting)  
`curl -s http://localhost:8080/rest/restaurants/12/vote-history`

 ###  localhost:8080/rest/menu-items/* и  localhost:8080/rest/dishes
 Ресурсы предназначены для работы с меню.
 Элементы меню содержат ID ресторана, наименование (берется из справочника блюд (Dish)) и цену блюда, дату создания записи.
 Цена на блюдо представлена Integer, если необходимо выделение рублей/копеек - это осуществляется на фронте.
  #### create new menu item  
 `curl -s -X POST -d '{"restaurantId":"12","dishId":"20","price":"129","date":"2019-03-16"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/menu-items`  
 #### get all menu items
 `curl -s http://localhost:8080/rest/menu-items`
 #### delete menu item with ID=30
 `curl -s -X DELETE http://localhost:8080/rest/menu-items/30`   
  
  Работа со справочником блюд
  #### create new dish
 `curl -s -X POST -d '{"name":"newDish"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/dishes`  
 #### get all dishes
 `curl -s http://localhost:8080/rest/dishes`  
 
### localhost:8080/rest/admin/*  
Ресурс предназначен для действий администратора в отношении обычных пользователей.  
#### get all users  
`curl -s http://localhost:8080/rest/admin/users`  
#### get user with ID=10   
`curl -s http://localhost:8080/rest/admin/users/10`  

#### delete user with ID=10 
`curl -s -X DELETE http://localhost:8080/rest/admin/users/10`  
### localhost:8080/rest/vote/restaurants/*  
Ресурс предназначен для получения списка объектов для голосования (рестораны, имеющие меню на текущий день) и непосредственно голосования.
#### get all restaurants with menu on current date  
`curl -s http://localhost:8080/rest/vote/restaurants`  
#### vote for restaurant with ID=13 
`curl -s -X POST -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/vote/restaurants/13`  
### localhost:8080/rest/profile/register  
Ресурс предназначен для создания нового пользователя.  
#### register new user  
`curl -s -X POST -d '{"email":"luser@gmail.com","name":"Luser","password":"swordfish"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile/register`