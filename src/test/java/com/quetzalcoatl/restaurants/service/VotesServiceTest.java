package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VotesServiceTest {
    @Autowired
    VotesService service;

    @Test
    void create() {
        service.create(RESTAURANT_3_ID, USER_ID, LocalDateTime.now());
        assertEquals(service.getAll().size(),3);
    }


    @Test
    void update() {
        LocalDate dateOfUpdate = service.get(VOTE_1_ID).getDateTime().toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(dateOfUpdate, LocalTime.of(10,30));
        service.update(VOTE_1_ID, RESTAURANT_2_ID, dateTime);
        Votes updated = service.get(VOTE_1_ID);
        assertEquals(RESTAURANT_2_ID, updated.getRestaurant().getId().intValue());
    }

    @Test
    void updateInvalid() {
        LocalDate dateOfUpdate = service.get(VOTE_1_ID).getDateTime().toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(dateOfUpdate, LocalTime.of(11,10));
        assertThrows(LateToVoteException.class, () ->
                service.update(VOTE_1_ID, RESTAURANT_2_ID, dateTime));
    }

    @Test
    void isVotesOnDate(){
        assertTrue(service.isVotesOnDate(USER_ID, LocalDate.of(2019,1,5)));
        assertFalse(service.isVotesOnDate(USER_ID, LocalDate.of(2018,12,31)));

    }

}