package com.quetzalcoatl.restaurants.data;

import com.quetzalcoatl.restaurants.model.User;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static com.quetzalcoatl.restaurants.web.TestUtil.assertMatch;
import static com.quetzalcoatl.restaurants.web.TestUtil.readFromJsonMvcResult;
import static com.quetzalcoatl.restaurants.web.TestUtil.readListFromJsonMvcResult;

public class UserTestData {
    public static final String[] FIELDS_TO_IGNORE = {"registered", "password"};

    public static ResultMatcher getUserMatcher(User expected) {
        return result -> assertMatch(FIELDS_TO_IGNORE, readFromJsonMvcResult(result, User.class), expected);
    }

    public static  ResultMatcher getUserMatcher(User... expected) {
        return result -> assertMatch(FIELDS_TO_IGNORE, readListFromJsonMvcResult(result, User.class), List.of(expected));
    }
}
