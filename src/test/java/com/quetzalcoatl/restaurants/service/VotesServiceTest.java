package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Votes;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

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
        Votes created = service.create(RESTAURANT_3_ID, USER_1_ID);
        assertTrue(service.getAll().size() == 3);
    }

    @Test
    void lateToVote() {
        assertThrows(LateToVoteException.class, () ->
                service.create(RESTAURANT_1_ID, 10));

    }

    @Disabled("Can throw LateToVoteException")
    @Test
    void update() {
        Votes voteToUpdate = service.get(VOTE_1_ID);
        LocalDateTime beforeUpdate = voteToUpdate.getDateTime();
        service.update(voteToUpdate, RESTAURANT_2_ID);
        Votes updated = service.get(VOTE_1_ID);
        assertEquals(RESTAURANT_2_ID, updated.getRestaurant().getId().intValue());
        assertFalse(updated.getDateTime().isEqual(beforeUpdate));

    }


}