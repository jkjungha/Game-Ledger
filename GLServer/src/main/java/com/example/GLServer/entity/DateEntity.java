package com.example.GLServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @OneToOne
    @JoinColumn(name = "savingId")
    private SavingEntity savingEntity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dateEntity")
    private List<TransactionEntity> TransactionEntities;

    private int year;
    private int month;
    private int day;
}
