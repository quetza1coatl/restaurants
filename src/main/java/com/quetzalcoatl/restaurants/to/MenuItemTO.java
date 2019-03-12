package com.quetzalcoatl.restaurants.to;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class MenuItemTO implements Serializable {
    private static final long serialVersionUID = 1L;

    public MenuItemTO() {
    }

    public MenuItemTO(Integer id, Integer restaurantId, Integer dishId, Integer price, LocalDate date) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishId = dishId;
        this.price = price;
        this.date = date;
    }

    public MenuItemTO(Integer restaurantId, Integer dishId, Integer price, LocalDate date) {
        this.restaurantId = restaurantId;
        this.dishId = dishId;
        this.price = price;
        this.date = date;
    }

    private Integer id;

    private Integer restaurantId;

    private Integer dishId;

    @Range(min = 1)
    @NotNull
    private Integer price;

    @DateTimeFormat
    private LocalDate date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
