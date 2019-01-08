package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.quetzalcoatl.restaurants.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})

@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class VotesServiceTest {
    @Autowired
    VotesService service;

    @Disabled("Can throw LateToVoteException")
    @Test
    void create() {
        Votes created = service.create(RESTAURANT_3_ID, USER_1_ID, LocalDateTime.now());
        assertTrue(service.getAll().size() == 3);
    }

    @Disabled("Can throw LateToVoteException")
    @Test
    void lateToVote() {
        assertThrows(LateToVoteException.class, () ->
                service.create(RESTAURANT_1_ID, USER_1_ID, LocalDateTime.now()));

    }

    @Disabled("Can throw LateToVoteException")
    @Test
    void update() {
        LocalDateTime beforeUpdate = service.get(VOTE_1_ID).getDateTime();
        service.update(VOTE_1_ID, RESTAURANT_2_ID, beforeUpdate);
        Votes updated = service.get(VOTE_1_ID);
        assertEquals(RESTAURANT_2_ID, updated.getRestaurant().getId().intValue());


    }

    @Test
    void isVotesOnDate(){
        assertTrue(service.isVotesOnDate(USER_1_ID, LocalDate.of(2019,1,5)));
        assertFalse(service.isVotesOnDate(USER_1_ID, LocalDate.of(2018,12,31)));

    }

}