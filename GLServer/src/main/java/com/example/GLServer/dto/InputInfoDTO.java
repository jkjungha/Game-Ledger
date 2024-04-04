package com.example.GLServer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputInfoDTO {
    private boolean transType;
    private int transYear;
    private int transMonth;
    private int transDay;
    private String tranCategory;
    private String transName;
    private int transValue;
}
