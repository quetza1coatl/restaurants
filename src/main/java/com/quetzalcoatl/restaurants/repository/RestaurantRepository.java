package com.quetzalcoatl.restaurants.repository;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RestaurantRepository {

    @Autowired
    CrudRestaurantRepository crudRepository;


    public Restaurant getWithMenuOnDate(Integer id, LocalDate dateTime) {
        Restaurant restaurant = crudRepository.getWithMenu(id);
        List<Menu> filtered = restaurant.getMenu().stream()
                .filter(m -> m.getDate().isEqual(dateTime)).collect(Collectors.toList());
        restaurant.setMenu(filtered);
        return restaurant;
    }

}
