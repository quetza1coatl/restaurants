package com.quetzalcoatl.restaurants.data;

import com.quetzalcoatl.restaurants.model.Dish;
import com.quetzalcoatl.restaurants.model.MenuItem;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static com.quetzalcoatl.restaurants.web.TestUtil.assertMatch;
import static com.quetzalcoatl.restaurants.web.TestUtil.readListFromJsonMvcResult;

public final class MenuItemsData {
    private MenuItemsData(){}
    public static final String[] FIELDS_TO_IGNORE = {"restaurant", "dish"};


    public static  ResultMatcher getMenuItemsMatcher(List<MenuItem> expected) { //*
        return result -> assertMatch(FIELDS_TO_IGNORE, readListFromJsonMvcResult(result, MenuItem.class), expected);
    }

    public static  ResultMatcher getDishMatcher(List<Dish> expected) {
        return result -> assertMatch(new String[0], readListFromJsonMvcResult(result, Dish.class), expected);
    }

}
