package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.MenuItem;
import com.quetzalcoatl.restaurants.repository.CrudDishRepository;
import com.quetzalcoatl.restaurants.repository.CrudMenuItemRepository;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("menuItemService")
public class MenuItemService {
    private final CrudMenuItemRepository repository;
    private final CrudRestaurantRepository restaurantRepository;
    private final CrudDishRepository dishRepository;

    @Autowired
    public MenuItemService(CrudMenuItemRepository repository,
                           CrudRestaurantRepository restaurantRepository,
                           CrudDishRepository dishRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public MenuItem create(MenuItem menu, int restaurantId, int dishId) {
        Assert.notNull(menu, "menu must not be null");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menu.setDish(dishRepository.getOne(dishId));
        return repository.save(menu);
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public MenuItem update(MenuItem menu, int restaurantId, int dishId){
        Assert.notNull(menu, "menu must not be null");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menu.setDish(dishRepository.getOne(dishId));
        return checkNotFoundWithId(repository.save(menu), menu.getId());
    }

    public MenuItem get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);

    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public List<MenuItem> getAll(){return repository.findAll();}

    //menu history
    public List<MenuItem> getByRestaurantId(int restaurantId) {
        return repository.getByRestaurantId(restaurantId);

    }

}
