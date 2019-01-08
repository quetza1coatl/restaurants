## REST API VOTING SYSTEM
Система голосования, позволяющая пользователям каждый день выбирать ресторан, в котором они хотели бы пообедать, в зависимости от меню.  
Выбор осуществляется из списка ресторанов, в котором есть меню на текущий день.  
Два вида пользователей: администратор (роль ROLE_ADMIN) и обычный пользователь (роль ROLE_USER).  
Обычный пользователь может проголосовать (или изменить свое мнение) до 11:00.  
Администратор добавляет рестораны, обновляет их меню каждый день (добавляет новые меню, 2-5 записи с указанием текущей даты), при необходимости расширяет справочник еды.  
Доступные ресурсы для администратора:  
localhost:8080/rest/admin/**  
localhost:8080/rest/restaurants/**  
Доступный ресурс для обычных пользователей:  
localhost:8080/rest/vote  
Доступный ресурс для неавторизованных пользователей:  
localhost:8080/rest/profile/register  
  
## CURL
Для авторизации после curl-команды вводятся креденшелы в формате `--user {mail}:{password}`, например:  
`curl -s http://localhost:8080/rest/restaurants --user admin@gmail.com:admin`  
Дефолтные креденшелы:  
администратор: admin@gmail.com:admin  
обычный пользователь: user@gmail.com:user1  

###  localhost:8080/rest/restaurants/**   
Ресурс предназначен для работы с ресторанами, их меню и блюдами, а также предоставляет историю еды и голосований.  
Меню ресторана на текущий день формируется путем добавления в него еды из справочника (Dish). Цена на блюдо представлена Integer, если необходимо выделение рублей/копеек - это осуществляется на фронте.  
  
#### get all Restaurants  
`curl -s http://localhost:8080/rest/restaurants`  
#### get restaurant by ID=12
`curl -s http://localhost:8080/rest/restaurants/12`
#### create new restaurant  
`curl -s -X POST -d '{"name":"Restaurant 4","address":"Paris"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants`  
#### delete restaurant with ID=12
`curl -s -X DELETE http://localhost:8080/rest/restaurants/12`  
#### create new menu  
`curl -s -X POST -d '{"restaurantId":"12","dishId":"20","price":"129","date":"2019-01-08"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/menu`  
#### get all menu
`curl -s http://localhost:8080/rest/restaurants/menu`
#### delete menu with ID=30
`curl -s -X DELETE http://localhost:8080/rest/restaurants/menu/30`  
#### create new dish
`curl -s -X POST -d '{"name":"newDish"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/restaurants/dish`  
#### get all dishes
`curl -s http://localhost:8080/rest/restaurants/dish`  
#### get menu history by restaurant ID=14
`curl -s http://localhost:8080/rest/restaurants/history/14`  
#### get vote history by restaurant ID=13 (returns vote id, restaurant_id, user_id, dateTime of voting)  
`curl -s http://localhost:8080/rest/restaurants/vote/history/13`
#### get restaurants with menu on date  
`curl -s http://localhost:8080/rest/restaurants/on_date?date=2018-12-30`  
 
### localhost:8080/rest/admin/**  
Ресурс предназначен для действий администратора в отношении обычных пользователей.  
#### get all users  
`curl -s http://localhost:8080/rest/admin/users`  
#### get by user ID=10   
`curl -s http://localhost:8080/rest/admin/users/10`  

#### delete user by ID=10 
`curl -s -X DELETE http://localhost:8080/rest/admin/users/10`  
###localhost:8080/rest/vote  
Ресурс предназначен для получения списка ресторанов с меню на текущий день и голосования.
#### get all restaurants with menu on current date  
`curl -s http://localhost:8080/rest/vote`  
#### vote for restaurant with ID=13 
`curl -s -X POST -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/vote/13`  
###localhost:8080/rest/profile/register
Ресурс предназначен для создания нового пользователя.  
#### register new user  
`curl -s -X POST -d '{"email":"luser@gmail.com","name":"Luser","password":"swordfish"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/rest/profile/register`