package com.quetzalcoatl.restaurants.web.controllers;


import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Vote;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.service.VoteService;
import com.quetzalcoatl.restaurants.web.SecurityUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(VoteRestController.REST_URL)
public class VoteRestController {
    static final String REST_URL = "/rest/vote/restaurants";

    private final VoteService voteService;
    private final RestaurantService restaurantService;
    private static final Logger log = getLogger(VoteRestController.class);

    @Autowired

    public VoteRestController(VoteService voteService, RestaurantService restaurantService) {
        this.voteService = voteService;
        this.restaurantService = restaurantService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllRestaurantsWithMenuOnCurrentDate() {
        log.info("get all restaurants with menu on current date");
        return restaurantService.getAllRestaurantsWithMenuOnDate(LocalDateTime.now().toLocalDate());
    }

    @PostMapping(value = "/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@PathVariable("restaurantId") int restaurantId) {
        int userId = SecurityUtil.authUserId();
        LocalDateTime dateTime = LocalDateTime.now();
        Integer voteId = voteService.getVoteIdByUserIdAndDate(userId, dateTime.toLocalDate());
        if (voteId != null) {
            voteService.update(voteId, restaurantId, dateTime);
            log.info("update vote {} with restaurant id={} and user id = {}", voteId, restaurantId, userId);
            return ResponseEntity.noContent().build();
        } else {
            Vote created = voteService.create(restaurantId, userId, dateTime);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{restaurantId}")
                    .buildAndExpand(created.getId()).toUri();
            log.info("create vote id={} with restaurant id={} and user id = {}", created.getId(), restaurantId, userId);
            return ResponseEntity.created(uriOfNewResource).body(created);
        }

    }


}
