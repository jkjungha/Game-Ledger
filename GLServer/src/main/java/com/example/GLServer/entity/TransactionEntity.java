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
    private DateEntity dateEntity;

    private boolean tranType;
    private String tranCategory;
    private String tranName;
    private int tranValue;

}
