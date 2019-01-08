package com.quetzalcoatl.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
@Table(name = "menu")
public class Menu extends AbstractBaseEntity{

    public Menu() {

    }

    public Menu(Integer price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

    public Menu(Integer id, Integer price, LocalDate date){
        super(id);
        this.price = price;
        this.date = date;
    }

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;


    @OneToOne
    private Dish dish;

    @Column(name = "price")
    @Range(min = 1)
    @NotNull
    private Integer price;

    @Column(name = "date")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;


    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
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

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                 ", price=" + price +
                ", date=" + date +
                "} ";
    }
}
