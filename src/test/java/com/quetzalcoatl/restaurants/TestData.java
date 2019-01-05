package com.quetzalcoatl.restaurants;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.model.Role;
import com.quetzalcoatl.restaurants.model.User;

import java.time.LocalDate;

import static com.quetzalcoatl.restaurants.model.AbstractBaseEntity.START_SEQ;

public class TestData {
    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final int MENU_1_ID = START_SEQ + 14;
    public static final int MENU_2_ID = START_SEQ + 15;
    public static final int MENU_3_ID = START_SEQ + 16;

    public static final int USER_1_ID = START_SEQ;
    public static final int USER_2_ID = START_SEQ + 1;

    public static final int DISH_1_ID = START_SEQ + 13;

    public static final int VOTE_1_ID = START_SEQ + 23;


    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Ресторан 1", "г. Минск, ул. Уличная, 5");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Ресторан 2","г. Минск, пер. Заплутавший, 119");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Ресторан 3", "г. Минск, пр-т Победителей, 1");

    public static final Menu MENU_1 = new Menu(MENU_1_ID, 168, LocalDate.of(2018,12,30));
    public static final Menu MENU_2 = new Menu(MENU_2_ID, 257, LocalDate.of(2018,12,30));
    public static final Menu MENU_3 = new Menu(MENU_3_ID, 132, LocalDate.of(2018,12,31));

    public static final User USER = new User(USER_1_ID, "User", "user@gmail.com", "user1", Role.ROLE_USER);
    public static final User ADMIN = new User(USER_2_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN);


}
