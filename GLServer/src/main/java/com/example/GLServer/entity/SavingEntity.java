package com.example.GLServer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SavingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int user_id;
    private int date_id;
    private int saving_food;
    private boolean food_achieved;
    private int saving_traffic;
    private boolean traffic_achieved;
    private int saving_culture;
    private boolean culture_achieved;
    private int saving_life;
    private boolean life_achieved;
    private int saving_etc;
    private boolean etc_achieved;


}
