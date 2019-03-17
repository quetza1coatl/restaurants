package com.quetzalcoatl.restaurants;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Role;
import com.quetzalcoatl.restaurants.model.User;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.List;

import static com.quetzalcoatl.restaurants.model.AbstractBaseEntity.START_SEQ;
import static com.quetzalcoatl.restaurants.web.TestUtil.readFromJsonMvcResult;
import static com.quetzalcoatl.restaurants.web.TestUtil.readListFromJsonMvcResult;
import static org.assertj.core.api.Assertions.assertThat;

public class TestData {

    private TestData(){}

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final int MENU_ITEM_1_ID = START_SEQ + 14;
    public static final int MENU_ITEM_2_ID = START_SEQ + 15;
    public static final int MENU_ITEM_3_ID = START_SEQ + 16;

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int DISH_1_ID = START_SEQ + 13;

    public static final int VOTE_1_ID = START_SEQ + 23;


    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Ресторан 1", "Адрес 1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Ресторан 2","Адрес 2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Ресторан 3", "Адрес 3");

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM_1_ID, 168, LocalDate.of(2018,12,30));
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM_2_ID, 257, LocalDate.of(2018,12,30));
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM_3_ID, 132, LocalDate.of(2018,12,31));

    public static final User USER = new User(USER_ID, "User", "user@gmail.com", "user1", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static ResultMatcher getUserMatcher(User expected) {
        return result -> assertMatch(readFromJsonMvcResult(result, User.class), expected);
    }
    public static ResultMatcher getUserMatcher(User... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, User.class), List.of(expected));
    }
    public static void assertMatch(User actual, User expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "registered", "password");
    }
    public static void assertMatch(Iterable<User> actual, Iterable<User> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "password").isEqualTo(expected);
    }
    public static void assertMatch(Iterable<User> actual, User... expected) {
        assertMatch(actual, List.of(expected));
    }

}
