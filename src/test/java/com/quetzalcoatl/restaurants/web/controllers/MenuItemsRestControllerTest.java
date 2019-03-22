package com.quetzalcoatl.restaurants.web.controllers;

import com.quetzalcoatl.restaurants.model.Dish;
import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.repository.CrudDishRepository;
import com.quetzalcoatl.restaurants.service.MenuItemService;
import com.quetzalcoatl.restaurants.to.MenuItemTO;
import com.quetzalcoatl.restaurants.web.AbstractRestControllerTest;
import com.quetzalcoatl.restaurants.web.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static com.quetzalcoatl.restaurants.web.TestUtil.*;
import static java.time.LocalDate.of;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuItemsRestControllerTest extends AbstractRestControllerTest {
    private static final String REST_MENU_ITEMS_URL = MenuItemsRestController.REST_URL + "/menu-items";
    private static final String REST_DISHES_URL = MenuItemsRestController.REST_URL + "/dishes";
    @Autowired
    private MenuItemService service;
    @Autowired
    private CrudDishRepository dishRepository;

    @Test
    void testGetAllMenuItems() throws Exception{
        mockMvc.perform(get(REST_MENU_ITEMS_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(MENU_ITEMS_FIELDS_TO_IGNORE, MenuItem.class, MENU_ITEM_LIST));
    }

    @Test
    void testCreateMenuItem() throws Exception{
        MenuItemTO createdTO = new MenuItemTO(null, 13,15,777, of(2019,3,21));
        MenuItem created = new MenuItem(createdTO.getId(), createdTO.getPrice(), createdTO.getDate());
        ResultActions action = mockMvc.perform(post(REST_MENU_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(createdTO))
                .with(userHttpBasic(ADMIN)));

        MenuItem returned = readFromJsonResultActions(action, MenuItem.class);
        created.setId(returned.getId());

        assertMatch(MENU_ITEMS_FIELDS_TO_IGNORE,returned, created);
        List<MenuItem> expectedList = new ArrayList<>(MENU_ITEM_LIST);
        expectedList.add(created);
        assertMatch(MENU_ITEMS_FIELDS_TO_IGNORE, service.getAll(), expectedList);
    }

    @Test
    void testDeleteMenuItem() throws Exception{
        mockMvc.perform(delete(REST_MENU_ITEMS_URL + "/" + MENU_ITEM_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent())
                .andDo(print());
        List<MenuItem> expectedList = new ArrayList<>(MENU_ITEM_LIST);
        expectedList.remove(MENU_ITEM_1);
        assertMatch(MENU_ITEMS_FIELDS_TO_IGNORE, service.getAll(), expectedList);
    }

    @Test
    void createDish() throws Exception{
        Dish created = new Dish(null, "New Dish");
        ResultActions action = mockMvc.perform(post(REST_DISHES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        Dish returned = readFromJsonResultActions(action, Dish.class);
        created.setId(returned.getId());

        assertMatch(DISHES_FIELDS_TO_IGNORE,returned, created);
        List<Dish> expectedList = new ArrayList<>(DISH_LIST);
        expectedList.add(created);
        assertMatch(DISHES_FIELDS_TO_IGNORE, dishRepository.findAll(), expectedList);
    }

    @Test
    void getAllDishes() throws Exception{
        mockMvc.perform(get(REST_DISHES_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(getMatcher(DISHES_FIELDS_TO_IGNORE, Dish.class, DISH_LIST));

    }

    @Test
    void testGetUnAuthMenuItems() throws Exception {
        mockMvc.perform(get(REST_MENU_ITEMS_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForbiddenMenuItems() throws Exception {
        mockMvc.perform(get(REST_MENU_ITEMS_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUnAuthDishes() throws Exception {
        mockMvc.perform(get(REST_DISHES_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetForbiddenDishes() throws Exception {
        mockMvc.perform(get(REST_DISHES_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }
}