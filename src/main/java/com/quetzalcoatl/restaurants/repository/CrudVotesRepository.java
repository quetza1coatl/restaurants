package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVotesRepository extends JpaRepository<Votes, Integer> {

    @Override
    @Transactional
    Votes save(Votes votes);

    @Override
    List<Votes> findAll();

    @Query("SELECT v.dateTime FROM Votes v WHERE v.user.id=:userId ORDER BY v.dateTime DESC")
    List<LocalDateTime> getDateTimeByUser(@Param("userId") int userId);

    @Query("SELECT v.id, v.restaurant.id, v.user.id, v.dateTime FROM Votes v WHERE v.restaurant.id=:restaurantId ORDER BY v.dateTime DESC")
    List<Votes> getByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT v FROM Votes v WHERE v.user.id=:userId ORDER BY v.dateTime DESC")
    List<Votes> getByUserId(@Param("userId") int userId);

    @Override
    Optional<Votes> findById(Integer id);



}
