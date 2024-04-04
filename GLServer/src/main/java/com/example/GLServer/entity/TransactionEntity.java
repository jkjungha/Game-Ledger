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

    private int user_id;
    private int trans_year;
    private int trans_month;
    private int trans_day;
    private boolean trans_type;
    private int trans_value;
    private String trans_name;
    private String trans_category;

}
