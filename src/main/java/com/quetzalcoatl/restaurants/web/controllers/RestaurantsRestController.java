package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Dish;
import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.repository.CrudDishRepository;
import com.quetzalcoatl.restaurants.service.MenuService;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.service.VotesService;
import com.quetzalcoatl.restaurants.to.MenuTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(RestaurantsRestController.REST_URL)
public class RestaurantsRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final VotesService votesService;
    private final CrudDishRepository dishRepository;
    private static final Logger log = getLogger(RestaurantsRestController.class);

    @Autowired
    public RestaurantsRestController(RestaurantService restaurantService, MenuService menuService, VotesService votesService, CrudDishRepository dishRepository) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.votesService = votesService;
        this.dishRepository = dishRepository;
    }
    //actions with restaurants

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurants() {
        log.info("get all restaurants");
        return restaurantService.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable("id") int id) {
        log.info("get restaurant with id={}", id);
        return restaurantService.get(id);
    }

    @GetMapping(value = "/on_date",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getOnMenuDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get restaurants with menu on {}", date);
        return restaurantService.getAllWithMenuOnDate(date);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable("id") int id) {
        log.info("delete restaurant with id={}", id);
        restaurantService.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);

        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    //actions with menu

    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody MenuTO menu) {
        Menu created = menuService.create(new Menu(menu.getPrice(), menu.getDate()), menu.getRestaurantId(), menu.getDishId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/menu" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @DeleteMapping("/menu/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("id") int id) {
        log.info("delete menu with id={}", id);
        menuService.delete(id);
    }

    //actions with dishes

    @PostMapping(value = "/dish", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody Dish dish) {
        Dish created = dishRepository.save(new Dish(dish.getName()));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/dish" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @GetMapping(value = "/dish",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getAllDish() {
        log.info("get all dishes");
        return dishRepository.findAll();
    }


    //history

    @GetMapping(value = "/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getMenuHistoryByRestaurantId(@PathVariable("id") int id) {
        log.info("get menu history with restaurant id={}", id);
        return menuService.getByRestaurantId(id);
    }

    //returns id, restaurant_id, user_id, dateTime
    @GetMapping(value = "/vote/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Votes> getVoteHistoryByRestaurantId(@PathVariable("id") int id) {
        log.info("get vote history with restaurant id={}", id);
        return votesService.getAllByRestaurantId(id);
    }


}
