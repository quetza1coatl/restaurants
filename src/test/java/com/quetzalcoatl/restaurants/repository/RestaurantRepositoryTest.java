package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.util.List;

import static com.quetzalcoatl.restaurants.RestaurantTestData.RESTAURANT_1_ID;
import static org.junit.jupiter.api.Assertions.*;
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))

class RestaurantRepositoryTest {
    @Autowired
    RestaurantRepository repository;

    @Test
    void getWithMenuOnDate() {
        Restaurant restaurant = repository.getWithMenuOnDate(RESTAURANT_1_ID, LocalDate.of(2018,12,31));
        List<Menu> menu = restaurant.getMenu();
        assertEquals(1, menu.size());
        assertEquals("Блюдо 3", menu.get(0).getDish().getName());
    }
}