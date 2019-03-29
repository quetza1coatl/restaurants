package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional(readOnly = true)
public interface CrudRestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Override
    @Transactional
    Restaurant save(Restaurant r);

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") Integer id);

    @Override
    List<Restaurant> findAll();

    @Query("SELECT DISTINCT r FROM Restaurant r LEFT JOIN FETCH r.menuItems m WHERE m.date=?1")
    List<Restaurant> getAllRestaurantsWithMenuOnDate(@Param("date") LocalDate date);

}
