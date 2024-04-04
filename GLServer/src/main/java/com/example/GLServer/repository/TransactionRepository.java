package com.example.GLServer.repository;

import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
}
