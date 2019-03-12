package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CrudMenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Override
    @Transactional
    MenuItem save(MenuItem menuItem);

    @Transactional
    @Modifying
    @Query("DELETE FROM MenuItem m WHERE m.id=:id")
    int delete(@Param("id") Integer id);

    @Override
    List<MenuItem> findAll();

    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id=:restaurantId ORDER BY m.date DESC, m.id")
    List<MenuItem> getByRestaurantId(@Param("restaurantId") int restaurantId);



}
