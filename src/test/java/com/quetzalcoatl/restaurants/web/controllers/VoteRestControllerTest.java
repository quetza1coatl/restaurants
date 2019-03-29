package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.service.MenuItemService;
import com.quetzalcoatl.restaurants.service.VotesService;
import com.quetzalcoatl.restaurants.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static com.quetzalcoatl.restaurants.web.TestUtil.getMatcher;
import static com.quetzalcoatl.restaurants.web.TestUtil.readFromJsonResultActions;
import static com.quetzalcoatl.restaurants.web.TestUtil.userHttpBasic;
import static com.quetzalcoatl.restaurants.web.controllers.VoteRestController.REST_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class VoteRestControllerTest extends AbstractRestControllerTest {
    private final LocalTime REVOTE_TIME = LocalTime.of(11, 0);
    @Autowired
    VotesService service;
    @Autowired
    MenuItemService menuItemService;

    @Test
    void testGetAllRestaurantsWithMenuOnCurrentDate() throws Exception {
        LocalDate now = LocalDate.now();
        menuItemService.create(new MenuItem(555, now), RESTAURANT_1_ID, DISH_1_ID);
        menuItemService.create(new MenuItem(666, now), RESTAURANT_1_ID, DISH_2_ID);
        menuItemService.create(new MenuItem(777, now), RESTAURANT_2_ID, DISH_3_ID);

        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(RESTAURANTS_FIELDS_TO_IGNORE, Restaurant.class, RESTAURANT_1, RESTAURANT_2));
    }

    @Test
    void testVote() throws Exception {
        ResultActions action = mockMvc.perform(post(REST_URL + "/" + RESTAURANT_1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Votes returned = readFromJsonResultActions(action, Votes.class);
        assertEquals(returned.getRestaurant().getId().intValue(), RESTAURANT_1_ID);
        assertEquals(returned.getUser().getId().intValue(), USER_ID);
        assertEquals(returned.getDateTime().toLocalDate(), LocalDate.now());
    }

    @Test
    void testRevote() throws Exception {
        ResultActions action = mockMvc.perform(post(REST_URL + "/" + RESTAURANT_1_ID)
                .with(userHttpBasic(USER)));
        Votes returned = readFromJsonResultActions(action, Votes.class);
        LocalTime now = LocalTime.now();

        ResultActions actionRevote = mockMvc.perform(post(REST_URL + "/" + RESTAURANT_2_ID)
                .with(userHttpBasic(USER)))
                .andDo(print());

        if (now.isAfter(REVOTE_TIME)) {
            actionRevote.andExpect(status().isNotAcceptable())
                    .andExpect(jsonPath("$.detail").value("It is late to change your vote"));
        } else {
            actionRevote.andExpect(status().isNoContent())
                    .andDo(print());
            assertEquals(service.get(returned.getId()).getRestaurant().getId().intValue(), RESTAURANT_2_ID);
            assertEquals(service.getAll().size(), 3);
        }
    }


    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isForbidden());
    }
}