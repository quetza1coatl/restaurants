package com.quetzalcoatl.restaurants;

import com.quetzalcoatl.restaurants.model.*;
import java.util.List;

import static com.quetzalcoatl.restaurants.model.AbstractBaseEntity.START_SEQ;
import static java.time.LocalDate.of;


public final class TestValues {

    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final int MENU_ITEM_1_ID = START_SEQ + 14;
    private static final int MENU_ITEM_2_ID = START_SEQ + 15;
    private static final int MENU_ITEM_3_ID = START_SEQ + 16;
    private static final int MENU_ITEM_4_ID = START_SEQ + 17;
    private static final int MENU_ITEM_5_ID = START_SEQ + 18;
    private static final int MENU_ITEM_6_ID = START_SEQ + 19;
    private static final int MENU_ITEM_7_ID = START_SEQ + 20;
    private static final int MENU_ITEM_8_ID = START_SEQ + 21;
    private static final int MENU_ITEM_9_ID = START_SEQ + 22;

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int DISH_1_ID = START_SEQ + 5;
    public static final int DISH_2_ID = START_SEQ + 6;
    public static final int DISH_3_ID = START_SEQ + 7;
    private static final int DISH_4_ID = START_SEQ + 8;
    private static final int DISH_5_ID = START_SEQ + 9;
    private static final int DISH_6_ID = START_SEQ + 10;
    private static final int DISH_7_ID = START_SEQ + 11;
    private static final int DISH_8_ID = START_SEQ + 12;
    private static final int DISH_9_ID = START_SEQ + 13;

    public static final int VOTE_1_ID = START_SEQ + 23;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Ресторан 1", "Адрес 1");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Ресторан 2","Адрес 2");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Ресторан 3", "Адрес 3");
    public static final List<Restaurant> RESTAURANTS_LIST = List.of(RESTAURANT_1,RESTAURANT_2,RESTAURANT_3);
    public static final String VOTE_HISTORY_RESTAURANT_1_RESPONSE = "[[33,12,10,\"2019-01-05\",\"09:00:00\"]]";

    public static final MenuItem MENU_ITEM_1 = new MenuItem(MENU_ITEM_1_ID, 168, of(2018,12,30));
    public static final MenuItem MENU_ITEM_2 = new MenuItem(MENU_ITEM_2_ID, 257, of(2018,12,30));
    public static final MenuItem MENU_ITEM_3 = new MenuItem(MENU_ITEM_3_ID, 132, of(2018,12,31));
    private static final MenuItem MENU_ITEM_4 = new MenuItem(MENU_ITEM_4_ID, 431, of(2018,12,30));
    private static final MenuItem MENU_ITEM_5 = new MenuItem(MENU_ITEM_5_ID, 323, of(2018,12,30));
    private static final MenuItem MENU_ITEM_6 = new MenuItem(MENU_ITEM_6_ID, 98, of(2018,12,30));
    private static final MenuItem MENU_ITEM_7 = new MenuItem(MENU_ITEM_7_ID, 493, of(2018,12,30));
    private static final MenuItem MENU_ITEM_8 = new MenuItem(MENU_ITEM_8_ID, 321, of(2018,12,30));
    private static final MenuItem MENU_ITEM_9 = new MenuItem(MENU_ITEM_9_ID, 816, of(2018,12,30));
    public static final List<MenuItem> MENU_ITEM_LIST = List.of(MENU_ITEM_1, MENU_ITEM_2, MENU_ITEM_3, MENU_ITEM_4, MENU_ITEM_5,
                                                                MENU_ITEM_6, MENU_ITEM_7, MENU_ITEM_8, MENU_ITEM_9);

    private static final Dish DISH_1 = new Dish(DISH_1_ID, "Блюдо 1");
    private static final Dish DISH_2 = new Dish(DISH_2_ID, "Блюдо 2");
    private static final Dish DISH_3 = new Dish(DISH_3_ID, "Блюдо 3");
    private static final Dish DISH_4 = new Dish(DISH_4_ID, "Блюдо 4");
    private static final Dish DISH_5 = new Dish(DISH_5_ID, "Блюдо 5");
    private static final Dish DISH_6 = new Dish(DISH_6_ID, "Блюдо 6");
    private static final Dish DISH_7 = new Dish(DISH_7_ID, "Блюдо 7");
    private static final Dish DISH_8 = new Dish(DISH_8_ID, "Блюдо 8");
    private static final Dish DISH_9 = new Dish(DISH_9_ID, "Блюдо 9");
    public static final List<Dish> DISH_LIST = List.of(DISH_1,DISH_2,DISH_3,DISH_4,DISH_5,DISH_6,DISH_7,DISH_8,DISH_9);

    public static final User USER = new User(USER_ID, "User", "user@gmail.com", "user1", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);

    public static final String[] MENU_ITEMS_FIELDS_TO_IGNORE = {"restaurant", "dish"};
    public static final String[] DISHES_FIELDS_TO_IGNORE = new String[0];
    public static final String[] USERS_FIELDS_TO_IGNORE = {"registered", "password"};
    public static final String[] RESTAURANTS_FIELDS_TO_IGNORE = { "menuItems", "registered"};

    private TestValues(){}

}
