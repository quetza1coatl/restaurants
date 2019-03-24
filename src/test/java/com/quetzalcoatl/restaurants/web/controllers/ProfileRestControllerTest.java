package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Role;
import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.service.UserService;
import com.quetzalcoatl.restaurants.web.AbstractRestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;

import static com.quetzalcoatl.restaurants.TestValues.ADMIN;
import static com.quetzalcoatl.restaurants.TestValues.USER;
import static com.quetzalcoatl.restaurants.TestValues.USERS_FIELDS_TO_IGNORE;
import static com.quetzalcoatl.restaurants.web.TestUtil.*;
import static com.quetzalcoatl.restaurants.web.controllers.ProfileRestController.REST_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractRestControllerTest {
    @Autowired
    UserService service;

    @Test
    void testRegister() throws Exception {
        User created = new User("newUser", "new@gmail.com", "swordfish");
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(created, "swordfish")))
                .andDo(print())
                .andExpect(status().isCreated());
        User returned = readFromJsonResultActions(action, User.class);
        created.setId(returned.getId());
        created.setRoles(Collections.singleton(Role.ROLE_USER));
        assertMatch(USERS_FIELDS_TO_IGNORE, returned, created);
        assertMatch(USERS_FIELDS_TO_IGNORE, service.getAll(), ADMIN, USER, created);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void testRegisterDuplicate() throws Exception {
        User duplicate = new User("newUser", "admin@gmail.com", "swordfish");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(duplicate, "swordfish")))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("User with this email already exists"))
                .andDo(print());

    }

    @Test
    void testRegisterInvalid() throws Exception {
        User invalid = new User("1", "new@gmail.com", "swordfish");
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(invalid, "swordfish")))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.detail").value("name size must be between 2 and 100"));
    }

    @Test
    void testGetForbiddenAdmin() throws Exception {
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isForbidden());
    }
    @Test
    void testGetForbiddenUser() throws Exception {
        mockMvc.perform(post(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }
}