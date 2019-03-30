package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Vote;
import com.quetzalcoatl.restaurants.util.exceptions.LateToVoteException;
import com.quetzalcoatl.restaurants.util.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.quetzalcoatl.restaurants.TestValues.*;
import static org.junit.jupiter.api.Assertions.*;

class VoteServiceTest extends AbstractServiceTest{
    @Autowired
    VoteService service;

    @Test
    void create() {
        service.create(RESTAURANT_3_ID, USER_ID, LocalDateTime.now());
        assertEquals(service.getAll().size(),3);
    }

    @Test
    void createWithBadId(){
        assertThrows(NotFoundException.class, () ->{
                try{
                service.create(0,0, LocalDateTime.now());
                }catch (NotFoundException e){
                    System.out.println(e.getMessage());
                    throw e;
                }
        });
    }


    @Test
    void update() {
        LocalDate dateOfUpdate = service.get(VOTE_1_ID).getDateTime().toLocalDate();
        LocalDateTime dateTime = LocalDateTime.of(dateOfUpdate, LocalTime.of(10,30));
        service.update(VOTE_1_ID, RESTAURANT_2_ID, dateTime);
        Vote updated = service.get(VOTE_1_ID);
        assertEquals(RESTAURANT_2_ID, updated.getRestaurant().getId().intValue());
    }

    @Test
    void updateWithBadId(){
        assertThrows(NotFoundException.class, () ->
                service.update(0,0, LocalDateTime.now()));
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
        assertNotNull(service.getVoteIdByUserIdAndDate(USER_ID, LocalDate.of(2019,1,5)));
        assertNull(service.getVoteIdByUserIdAndDate(USER_ID, LocalDate.of(2018,12,31)));

    }


}