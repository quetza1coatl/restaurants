package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.util.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.util.List;

import static com.quetzalcoatl.restaurants.TestData.*;
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
        assertThat(service.getAll()).usingElementComparatorIgnoringFields("menu", "registered").isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, newRestaurant));
    }

    @Test
    void update(){
        Restaurant updated = new Restaurant(RESTAURANT_1.getId(), RESTAURANT_1.getName(),RESTAURANT_1.getAddress());
        updated.setName("Updated Name");
        updated.setAddress("Updated Address");
        service.update(updated);
        assertThat(service.get(RESTAURANT_1_ID)).isEqualToIgnoringGivenFields(updated, "registered", "menu");

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
        assertThat(restaurant).isEqualToIgnoringGivenFields(RESTAURANT_1, "registered", "menu");
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getAll() {
        List<Restaurant> actual = service.getAll();
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "menu")
                .isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3));
    }

    @Test
    void getByIdWithMenu() {
        Restaurant restaurant = service.getByIdWithMenu(RESTAURANT_1_ID);
        List<Menu> menu = restaurant.getMenu();
        assertEquals(3, menu.size());
        assertEquals("Блюдо 1", menu.get(0).getDish().getName());
        assertEquals("Блюдо 2", menu.get(1).getDish().getName());
        assertEquals("Блюдо 3", menu.get(2).getDish().getName());
        assertTrue(menu.get(0).getPrice() == 168);

    }


    @Test
    void getWithMenuOnDate() {
        Restaurant restaurant = service.getByIdWithMenuOnDate(RESTAURANT_1_ID, LocalDate.of(2018, 12, 31));
        List<Menu> menu = restaurant.getMenu();
        assertEquals(1, menu.size());
        assertEquals("Блюдо 3", menu.get(0).getDish().getName());
    }

}