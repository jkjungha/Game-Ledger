package com.example.GLServer.repository;

import com.example.GLServer.entity.DateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<DateEntity, Integer> {
}
