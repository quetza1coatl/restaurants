package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Votes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVotesRepository extends JpaRepository<Votes, Integer> {

    @Override
    @Transactional
    Votes save(Votes votes);

    @Override
    List<Votes> findAll();

    @Query("SELECT v.id, v.restaurant.id, v.user.id, v.date, v.time FROM Votes v WHERE v.restaurant.id=:restaurantId ORDER BY v.date DESC, v.time DESC")
    List<Votes> getByRestaurantId(@Param("restaurantId") int restaurantId);

    @Query("SELECT v.id FROM Votes v WHERE v.user.id=:userId AND v.date=:date")
    Integer getVoteIdByUserIdAndDate(@Param("userId") int userId, @Param("date") LocalDate date);

    @Override
    Optional<Votes> findById(Integer id);



}
