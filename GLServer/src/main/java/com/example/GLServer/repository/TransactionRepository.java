package com.example.GLServer.repository;

import com.example.GLServer.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query("SELECT SUM(t.tranValue) FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.tranType = ?2")
    Optional<Double> sumTranValueByUserEntityAndTranType(String username, Boolean tranType);

}
