package com.example.GLServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinInfoDTO {
    private String goalName;
    private int goalValue;
    private int foodValue;
    private int trafficValue;
    private int cultureValue;
    private int lifeValue;
}
