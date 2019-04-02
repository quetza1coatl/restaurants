package com.quetzalcoatl.restaurants.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "votes")
public class Vote extends AbstractBaseEntity{

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "date", nullable = false)
    @NotNull
    @DateTimeFormat
    private LocalDate date;

    @Column(name = "time", nullable = false)
    @NotNull
    @DateTimeFormat
    private LocalTime time;

    public Vote() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDateTime getDateTime(){
        return LocalDateTime.of(date,time);
    }

    public void setDateTime(LocalDateTime dateTime){
        date = dateTime.toLocalDate();
        time = dateTime.toLocalTime();
    }
}
