package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class RestaurantServiceTest {
    @Autowired
    RestaurantService service;

    @Test
    void create() {
        Restaurant newRestaurant = new Restaurant("Ресторан 4", "Вильнюс");
        Restaurant created = service.create(newRestaurant);
        newRestaurant.setId(created.getId());
        assertThat(service.getAll()).usingElementComparatorIgnoringFields(RESTAURANTS_FIELDS_TO_IGNORE).isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, newRestaurant));
    }

    @Test
    void update() {
        Restaurant updated = new Restaurant(RESTAURANT_1.getId(), RESTAURANT_1.getName(), RESTAURANT_1.getAddress());
        updated.setName("Updated Name");
        updated.setAddress("Updated Address");
        service.update(updated);
        assertThat(service.get(RESTAURANT_1_ID)).isEqualToIgnoringGivenFields(updated, RESTAURANTS_FIELDS_TO_IGNORE);

    }

    @Test
    void delete() {
        service.delete(RESTAURANT_1_ID);
        assertThat(service.getAll()).isEqualTo(List.of(RESTAURANT_2, RESTAURANT_3));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get() {
        Restaurant restaurant = service.get(RESTAURANT_1_ID);
        assertThat(restaurant).isEqualToIgnoringGivenFields(RESTAURANT_1, RESTAURANTS_FIELDS_TO_IGNORE);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getAll() {
        List<Restaurant> actual = service.getAll();
        assertThat(actual).usingElementComparatorIgnoringFields(RESTAURANTS_FIELDS_TO_IGNORE)
                .isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3));
    }

    @Test
    void getAllWithMenuOnDate() {
        List<Restaurant> restaurants = service.getAllWithMenuOnDate(LocalDate.of(2018, 12, 31));
        assertEquals(1, restaurants.size());
        List<MenuItem> menuItems = restaurants.get(0).getMenuItems();
        assertEquals(1, menuItems.size());
        assertEquals("Блюдо 3", menuItems.get(0).getDish().getName());
        List<Restaurant> emptyExpected = service.getAllWithMenuOnDate(LocalDate.of(2015,11,11));
        assertThat(emptyExpected).isEqualTo(Collections.EMPTY_LIST);

    }

}