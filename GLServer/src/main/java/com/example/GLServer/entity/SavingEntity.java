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

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @OneToOne
    @JoinColumn(name = "dateId")
    private DateEntity dateEntity;

    private int savingFood;
    private boolean foodAchieved;
    private int savingTraffic;
    private boolean trafficAchieved;
    private int savingCulture;
    private boolean cultureAchieved;
    private int savingLife;
    private boolean lifeAchieved;
    private int savingEtc;
    private boolean etcAchieved;

}
