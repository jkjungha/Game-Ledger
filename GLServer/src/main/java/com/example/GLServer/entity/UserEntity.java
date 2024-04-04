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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String goal_name;
    private int goal_value;
    private int goal_achieved;
    private int food_value;
    private int traffic_value;
    private int culture_value;
    private int life_value;
    private int etc_value;
    private String role;

}
