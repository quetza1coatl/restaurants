package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Dish;
import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.repository.CrudDishRepository;
import com.quetzalcoatl.restaurants.service.MenuItemService;
import com.quetzalcoatl.restaurants.to.MenuItemTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(MenuItemsRestController.REST_URL)
public class MenuItemsRestController {
    static final String REST_URL = "/rest";

    private final CrudDishRepository dishRepository;
    private final MenuItemService menuItemService;
    private static final Logger log = getLogger(RestaurantsRestController.class);

    @Autowired
    public MenuItemsRestController(CrudDishRepository dishRepository, MenuItemService menuItemService) {
        this.dishRepository = dishRepository;
        this.menuItemService = menuItemService;
    }

    @GetMapping(value = "/menu-items", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MenuItem> getAllMenuItems() {
        log.info("get all menu items");
        return menuItemService.getAll();
    }

    @PostMapping(value = "/menu-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> create(@RequestBody MenuItemTO menuItem) {
        MenuItem created = menuItemService.create(new MenuItem(menuItem.getPrice(), menuItem.getDate()), menuItem.getRestaurantId(), menuItem.getDishId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/menu-items" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);

    }

    @DeleteMapping("/menu-items/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("id") int id) {
        log.info("delete menu item with id={}", id);
        menuItemService.delete(id);
    }

    //actions with dishes

    @PostMapping(value = "/dishes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> create(@RequestBody Dish dish) {
        Dish created = dishRepository.save(new Dish(dish.getName()));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/dishes" + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("create {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> getAllDish() {
        log.info("get all dishes");
        return dishRepository.findAll();
    }


}
