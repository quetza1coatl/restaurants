package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(AdminRestController.REST_URL)
public class AdminRestController {
    static final String REST_URL = "/rest/admin/users";
    private final UserService userService;
    private static final Logger log = getLogger(AdminRestController.class);

    @Autowired
    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        log.info("get all users");
        return userService.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable("id") int id) {
        log.info("get user with id={}", id);
        return userService.get(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        log.info("delete user with id={}", id);
        userService.delete(id);
    }

}
