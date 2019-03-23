package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.service.RestaurantService;
import com.quetzalcoatl.restaurants.web.AbstractRestControllerTest;
import com.quetzalcoatl.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.quetzalcoatl.restaurants.TestValues.*;
import static com.quetzalcoatl.restaurants.web.TestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class RestaurantsRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_URL = RestaurantsRestController.REST_URL + '/';
    @Autowired
    RestaurantService service;

    @Test
    void testGetAllRestaurants() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(RESTAURANTS_FIELDS_TO_IGNORE, Restaurant.class, RESTAURANTS_LIST));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(RESTAURANTS_FIELDS_TO_IGNORE, RESTAURANT_1));
    }

    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Test
    void testDeleteRestaurant() throws Exception {
        mockMvc.perform(delete(REST_URL + "/" + RESTAURANT_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());
        List<Restaurant> expectedList = new ArrayList<>(RESTAURANTS_LIST);
        expectedList.remove(RESTAURANT_1);
        assertMatch(RESTAURANTS_FIELDS_TO_IGNORE, service.getAll(), expectedList);
    }

    @Test
    void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }


    @Test
    void testCreate() throws Exception {
        Restaurant created = new Restaurant(null, "New Restaurant", "address");
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        Restaurant returned = readFromJsonResultActions(action, Restaurant.class);
        created.setId(returned.getId());

        assertMatch(RESTAURANTS_FIELDS_TO_IGNORE, returned, created);
        List<Restaurant> expectedList = new ArrayList<>(RESTAURANTS_LIST);
        expectedList.add(created);
        assertMatch(RESTAURANTS_FIELDS_TO_IGNORE, service.getAll(), expectedList);
    }

    @Test
    void testCreateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, "", "address");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }

    @Transactional(propagation = Propagation.NEVER)
    @Test
    void testCreateDuplicate() throws Exception {
        Restaurant duplicate = new Restaurant(null, "duplicate", "Адрес 1");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(ADMIN))
                .content(JsonUtil.writeValue(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Restaurant with this address already exists"))
                .andDo(print());

    }


    @Test
    void testGetMenuHistoryByRestaurantId() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT_1_ID + "/menu-history")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(MENU_ITEMS_FIELDS_TO_IGNORE, MenuItem.class, MENU_ITEM_3, MENU_ITEM_1, MENU_ITEM_2));
    }

    @Test
    void testGetVoteHistoryByRestaurantId() throws Exception {
        ResultActions action = mockMvc.perform(get(REST_URL + RESTAURANT_1_ID + "/vote-history")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        String response = getContent(action.andReturn());
        assertEquals(response, VOTE_HISTORY_RESTAURANT_1_RESPONSE);


    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }
}