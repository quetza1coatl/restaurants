package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Vote;
import com.quetzalcoatl.restaurants.service.MenuItemService;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.service.VoteService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(RestaurantsRestController.REST_URL)
public class RestaurantsRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final VoteService voteService;
    private static final Logger log = getLogger(RestaurantsRestController.class);

    @Autowired
    public RestaurantsRestController(RestaurantService restaurantService, MenuItemService menuItemService, VoteService voteService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.voteService = voteService;
    }

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

//    @GetMapping(value = "/on_date", produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<Restaurant> getOnMenuDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        log.info("get restaurants with menu on {}", date);
//        return restaurantService.getAllWithMenuOnDate(date);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable("id") int id) {
        log.info("delete restaurant with id={}", id);
        restaurantService.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);

        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    //history

    @GetMapping(value = "/{id}/menu-history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuItem> getMenuHistoryByRestaurantId(@PathVariable("id") int id) {
        log.info("get menu history with restaurant id={}", id);
        return menuItemService.getByRestaurantId(id);
    }

    //(returns id, restaurant_id, user_id, dateTime)
    @GetMapping(value = "/{id}/vote-history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getVoteHistoryByRestaurantId(@PathVariable("id") int id) {
        log.info("get vote history with restaurant id={}", id);
        return voteService.getAllByRestaurantId(id);
    }


}
