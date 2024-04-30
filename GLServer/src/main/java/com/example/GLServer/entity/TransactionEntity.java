package com.example.GLServer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "dateId")
    private DateEntity dateEntity;

    private boolean transType;
    private String transCategory;
    private String transName;
    private int transValue;

}
