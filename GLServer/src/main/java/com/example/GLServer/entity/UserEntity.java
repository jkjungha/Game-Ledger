package com.example.GLServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<SavingEntity> savingEntities;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<TransactionEntity> transactionEntities;

    private String username;
    private String password;
    private String email;
    private String phone;
    private String goalName;
    private int goalValue;
    private int goalAchieved;
    private int foodValue;
    private int trafficValue;
    private int cultureValue;
    private int lifeValue;
    private int etcValue;
    private String role;


}
