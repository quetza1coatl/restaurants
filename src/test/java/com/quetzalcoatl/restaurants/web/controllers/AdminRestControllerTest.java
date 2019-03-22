package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.service.UserService;
import com.quetzalcoatl.restaurants.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static com.quetzalcoatl.restaurants.web.TestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_URL = AdminRestController.REST_URL + '/';
    @Autowired
    private UserService service;

    @Test
    void testGetAll() throws Exception{
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(USERS_FIELDS_TO_IGNORE,User.class, ADMIN, USER));
    }

    @Test
    void testGet() throws Exception{
        mockMvc.perform(get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(USERS_FIELDS_TO_IGNORE, ADMIN));
    }
    @Test
    void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 1)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity())
                .andDo(print());
    }


    @Test
    void testDelete() throws Exception{
        mockMvc.perform(delete(REST_URL + USER_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());
                assertMatch(USERS_FIELDS_TO_IGNORE, service.getAll(), List.of(ADMIN));


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