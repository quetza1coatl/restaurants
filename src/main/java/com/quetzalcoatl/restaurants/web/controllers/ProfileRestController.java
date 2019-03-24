package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Role;
import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.service.UserService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController {
    static final String REST_URL = "/rest/profile/register";
    private final UserService userService;
    private static final Logger log = getLogger(ProfileRestController.class);

    public ProfileRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        User created = userService.create(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("register {}", created);

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

}
