package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Restaurant;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("restaurantService")
public class RestaurantService {
    private final CrudRestaurantRepository repository;

    @Autowired
    public RestaurantService(CrudRestaurantRepository repository) {
        this.repository = repository;
    }


    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return repository.save(restaurant);
    }

    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(repository.save(restaurant), restaurant.getId());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);

    }

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAllRestaurantsWithMenuOnDate(LocalDate date){
        return repository.getAllRestaurantsWithMenuOnDate(date);
    }

}
