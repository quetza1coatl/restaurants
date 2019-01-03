package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static com.quetzalcoatl.restaurants.RestaurantTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class CrudRestaurantRepositoryTest {

    @Autowired
    CrudRestaurantRepository repository;

    @Test
    void save() {
        Restaurant newRestaurant = new Restaurant("Ресторан 4", "Вильнюс");
        Restaurant created = repository.save(newRestaurant);
        newRestaurant.setId(created.getId());
        assertThat(repository.findAll()).isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, newRestaurant));
    }

    @Test
    void delete(){
        int deletion = repository.delete(RESTAURANT_1_ID);
        assertThat(repository.findAll()).isEqualTo(List.of(RESTAURANT_2, RESTAURANT_3));
        assertEquals(1, deletion);


    }

    @Test
    void findAll() {
        List<Restaurant> actual = repository.findAll();
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "menu")
                .isEqualTo(List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3));
    }

    @Test
    void findById() {
        Restaurant restaurant = repository.findById(RESTAURANT_1_ID).get();
        assertThat(restaurant).isEqualToIgnoringGivenFields(RESTAURANT_1, "registered", "menu");
    }

    @Test
    void getWithMenu() {
        Restaurant restaurant = repository.getWithMenu(RESTAURANT_1_ID);
        List<Menu> menu = restaurant.getMenu();
        assertEquals(3, menu.size());
        assertEquals("Блюдо 1", menu.get(0).getDish().getName());
        assertEquals("Блюдо 2", menu.get(1).getDish().getName());
        assertEquals("Блюдо 3", menu.get(2).getDish().getName());
        assertTrue(menu.get(0).getPrice() == 168);


    }
}