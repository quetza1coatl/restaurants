package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.List;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class MenuItemServiceTest extends AbstractServiceTest{


    @Autowired
    MenuItemService service;

    @Test
    void create() {
        MenuItem newMenuItem = new MenuItem(255, LocalDate.of(2019, 1,3));
        MenuItem created = service.create(newMenuItem, RESTAURANT_1_ID, DISH_1_ID);
        newMenuItem.setId(created.getId());
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields(MENU_ITEMS_FIELDS_TO_IGNORE).isEqualTo(List.of(newMenuItem, MENU_ITEM_3, MENU_ITEM_1, MENU_ITEM_2));
    }

    @Test
    void update() {
        MenuItem updated = new MenuItem(MENU_ITEM_1_ID, MENU_ITEM_1.getPrice(), MENU_ITEM_1.getDate());
        updated.setPrice(999);
        updated.setDate(LocalDate.of(2999,1,1));
        service.update(updated,RESTAURANT_2_ID,DISH_1_ID);
        assertThat(service.get(MENU_ITEM_1_ID)).isEqualTo(updated);

    }

    @Test
    void get() {
        MenuItem menuItem = service.get(MENU_ITEM_1_ID);
        assertThat(menuItem).isEqualToIgnoringGivenFields(MENU_ITEM_1, MENU_ITEMS_FIELDS_TO_IGNORE);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void delete() {
        service.delete(MENU_ITEM_1_ID);
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields(MENU_ITEMS_FIELDS_TO_IGNORE).isEqualTo(List.of(MENU_ITEM_3, MENU_ITEM_2));
    }

    @Test
    void getByRestaurantId() {
        assertThat(service.getByRestaurantId(RESTAURANT_1_ID)).usingElementComparatorIgnoringFields(MENU_ITEMS_FIELDS_TO_IGNORE).isEqualTo(List.of(MENU_ITEM_3, MENU_ITEM_1, MENU_ITEM_2));
    }
}