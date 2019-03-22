package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Role;
import com.quetzalcoatl.restaurants.model.User;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class UserServiceTest {

    @Autowired
    UserService service;


    @Test
    void create(){
        User newUser = new User(null, "New", "new@gmail.com", "newPass", true, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = service.create(new User(newUser));
        newUser.setId(created.getId());
        assertThat(service.getAll()).usingElementComparatorIgnoringFields(USERS_FIELDS_TO_IGNORE).isEqualTo(List.of(ADMIN, newUser, USER));

    }

    @Test
    void duplicateMailCreate(){
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@gmail.com", "newPass", Role.ROLE_USER)));
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThat(service.getAll()).usingElementComparatorIgnoringFields(USERS_FIELDS_TO_IGNORE).isEqualTo(List.of(ADMIN));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                service.delete(1));
    }

    @Test
    void get()  {
        User user = service.get(ADMIN_ID);
        assertThat(user).isEqualToIgnoringGivenFields(ADMIN, USERS_FIELDS_TO_IGNORE);
    }

    @Test
    void getNotFound()  {
        assertThrows(NotFoundException.class, () ->
                service.get(1));
    }

    @Test
    void getByEmail(){
        User user = service.getByEmail("admin@gmail.com");
        assertThat(user).isEqualToIgnoringGivenFields(ADMIN, USERS_FIELDS_TO_IGNORE);
    }

    @Test
    void update() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        service.update(new User(updated));
        assertThat(service.get(USER_ID)).isEqualToIgnoringGivenFields(updated, USERS_FIELDS_TO_IGNORE);
    }

    @Test
    void getAll(){
        List<User> all = service.getAll();
        assertThat(all).usingElementComparatorIgnoringFields(USERS_FIELDS_TO_IGNORE).isEqualTo(List.of(ADMIN, USER));
    }

    @Test
    void enable() {
        service.enable(USER_ID, false);
        assertFalse(service.get(USER_ID).isEnabled());
        service.enable(USER_ID, true);
        assertTrue(service.get(USER_ID).isEnabled());
    }


}