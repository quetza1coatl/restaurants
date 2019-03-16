package com.quetzalcoatl.restaurants.web.controllers;


import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.service.VotesService;
import com.quetzalcoatl.restaurants.web.SecurityUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping(VoteRestController.REST_URL)
public class VoteRestController {
    static final String REST_URL = "/rest/vote/restaurants";

    private final VotesService votesService;
    private final RestaurantService restaurantService;
    private static final Logger log = getLogger(VoteRestController.class);
    @Autowired

    public VoteRestController(VotesService votesService, RestaurantService restaurantService) {
        this.votesService = votesService;
        this.restaurantService = restaurantService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAllByMenuDate() {
        log.info("get all restaurants with menu on current date");
        return restaurantService.getAllWithMenuOnDate(LocalDateTime.now().toLocalDate());
    }

    @PostMapping(value = "/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Votes vote(@PathVariable("restaurantId") int restaurantId) {
        int userId = SecurityUtil.authUserId();
        LocalDateTime dateTime = LocalDateTime.now();
        if (votesService.isVotesOnDate(userId, dateTime.toLocalDate())) {
            int voteId = votesService.getVoteIdByUserAndDate(userId, dateTime.toLocalDate());
            log.info("update vote {}", voteId);
            return votesService.update(voteId, restaurantId, dateTime);
        } else {
            log.info("create vote with restaurant id={} and user id = {}",restaurantId, userId);
            return votesService.create(restaurantId, userId, dateTime);
        }

    }


}
