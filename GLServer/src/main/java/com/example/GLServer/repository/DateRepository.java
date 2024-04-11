package com.example.GLServer.repository;

import com.example.GLServer.entity.DateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DateRepository extends JpaRepository<DateEntity, Integer> {
    List<DateEntity> findAllByYearAndMonth(int year, int month);
    DateEntity findAllByYearAndMonthAndDay(int year, int month, int day);
}
