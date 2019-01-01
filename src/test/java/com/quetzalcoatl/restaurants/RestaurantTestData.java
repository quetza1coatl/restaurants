package com.quetzalcoatl.restaurants;

import com.quetzalcoatl.restaurants.model.Restaurant;

import static com.quetzalcoatl.restaurants.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final int RESTAURANT_1_ID = START_SEQ + 2;
    public static final int RESTAURANT_2_ID = START_SEQ + 3;
    public static final int RESTAURANT_3_ID = START_SEQ + 4;

    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "Ресторан 1", "г. Минск, ул. Уличная, 5");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Ресторан 2","г. Минск, пер. Заплутавший, 119");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Ресторан 3", "г. Минск, пр-т Победителей, 1");
}
