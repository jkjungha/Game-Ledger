package com.example.GLServer.entity;

import jakarta.persistence.*;
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
    private int saving_year;
    private int saving_month;
    private int saving_day;
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
