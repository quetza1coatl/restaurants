package com.quetzalcoatl.restaurants.model;


import javax.persistence.Entity;

@Entity
public class Dishes extends AbstractBaseEntity{

    public Dishes() {
    }
//TODO annotations
    private String name;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
