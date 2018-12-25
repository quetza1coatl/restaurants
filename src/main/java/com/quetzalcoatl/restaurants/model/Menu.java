package com.quetzalcoatl.restaurants.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends AbstractBaseEntity{

    public Menu() {

    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;




    @OneToMany(fetch = FetchType.LAZY)
    private List<Dish> dishes;


    private Integer price;

    private LocalDate date;


}
