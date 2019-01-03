package com.quetzalcoatl.restaurants.service;

import com.quetzalcoatl.restaurants.model.Menu;
import com.quetzalcoatl.restaurants.repository.CrudDishRepository;
import com.quetzalcoatl.restaurants.repository.CrudMenuRepository;
import com.quetzalcoatl.restaurants.repository.CrudRestaurantRepository;
import com.quetzalcoatl.restaurants.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.quetzalcoatl.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Service("menuService")
public class MenuService {
    private final CrudMenuRepository repository;
    private final CrudRestaurantRepository restaurantRepository;
    private final CrudDishRepository dishRepository;

    @Autowired
    public MenuService(CrudMenuRepository repository,
                       CrudRestaurantRepository restaurantRepository,
                       CrudDishRepository dishRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public Menu create(Menu menu, int restaurantId, int dishId) {
        Assert.notNull(menu, "menu must not be null");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menu.setDish(dishRepository.getOne(dishId));
        return repository.save(menu);
    }

    public Menu update(Menu menu, int restaurantId, int dishId) throws NotFoundException {
        Assert.notNull(menu, "menu must not be null");
        menu.setRestaurant(restaurantRepository.getOne(restaurantId));
        menu.setDish(dishRepository.getOne(dishId));
        return checkNotFoundWithId(repository.save(menu), menu.getId());
    }

    public Menu get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);

    }

    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    //menu history
    public List<Menu> getByRestaurantId(int restaurantId) {
        return repository.getByRestaurantId(restaurantId);

    }

}
