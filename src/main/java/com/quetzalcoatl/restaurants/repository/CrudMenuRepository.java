package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Override
    @Transactional
    Menu save(Menu menu);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC")
    List<Menu> getByRestaurantId(@Param("restaurantId") int restaurantId);



}
