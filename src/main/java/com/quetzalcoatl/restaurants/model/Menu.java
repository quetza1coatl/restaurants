package com.quetzalcoatl.restaurants.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Menu extends AbstractBaseEntity{

    public Menu() {

    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;




    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dishes")
    private List<Dishes> dishes;

    private Integer price;

    private Date date;


}
