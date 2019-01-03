package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Menu;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class MenuServiceTest {


    @Autowired
    MenuService service;

    @Test
    void create() {
        Menu newMenu = new Menu(255, LocalDate.of(2019, 1,3));
        Menu created = service.create(newMenu, RESTAURANT_1_ID, DISH_1_ID);
        newMenu.setId(created.getId());
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields("restaurant", "dish").isEqualTo(List.of(newMenu, MENU_3, MENU_1, MENU_2));
    }

    @Test
    void update() {
        Menu updated = new Menu(MENU_1_ID, MENU_1.getPrice(), MENU_1.getDate());
        updated.setPrice(999);
        updated.setDate(LocalDate.of(2999,1,1));
        service.update(updated,RESTAURANT_2_ID,DISH_1_ID);
        assertThat(service.get(MENU_1_ID)).isEqualTo(updated);

    }

    @Test
    void get() {
        Menu menu = service.get(MENU_1_ID);
        assertThat(menu).isEqualToIgnoringGivenFields(MENU_1, "restaurant", "dish");
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void delete() {
        service.delete(MENU_1_ID);
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields("restaurant", "dish").isEqualTo(List.of(MENU_3, MENU_2));
    }

    @Test
    void getByRestaurantId() {
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields("restaurant", "dish").isEqualTo(List.of(MENU_3, MENU_1, MENU_2));
    }
}