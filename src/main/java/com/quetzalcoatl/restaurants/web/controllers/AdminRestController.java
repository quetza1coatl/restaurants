package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.service.MenuService;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.service.VotesService;
import com.quetzalcoatl.restaurants.to.MenuTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(AdminRestController.REST_URL)
public class AdminRestController {
    static final String REST_URL = "/rest/admin";

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final VotesService votesService;

    @Autowired
    public AdminRestController(RestaurantService restaurantService, MenuService menuService, VotesService votesService) {
        this.restaurantService = restaurantService;
        this.menuService = menuService;
        this.votesService = votesService;
    }


    @GetMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAll();
    }

    @GetMapping(value = "/restaurants/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable("id") int id) {
        return restaurantService.get(id);
    }

    @GetMapping(value = "/restaurantsOnDate", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getOnMenuDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return restaurantService.getAllWithMenuOnDate(date);
    }

    @PostMapping(value = "/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/restaurants" + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @PostMapping(value = "/menu", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody MenuTO menu) {
        Menu created = menuService.create(new Menu(menu.getPrice(), menu.getDate()), menu.getRestaurantId(), menu.getDishId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/menu" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @GetMapping(value = "/restaurants/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getMenuHistoryByRestaurantId(@PathVariable("id") int id) {
        return menuService.getByRestaurantId(id);
    }

    //returns id, restaurant_id, user_id, dateTime
    @GetMapping(value = "/vote/history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Votes> getVoteHistoryByRestaurantId(@PathVariable("id") int id) {
        return votesService.getAllByRestaurantId(id);
    }


}
