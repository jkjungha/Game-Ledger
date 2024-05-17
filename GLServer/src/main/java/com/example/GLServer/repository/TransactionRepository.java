package com.example.GLServer.repository;

import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query("SELECT SUM(t.transValue) FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3 AND t.transType = ?4")
    Optional<Double> sumTransValueByUserEntityAndDateEntityAndTranType(String username,int year, int month, Boolean transType);

    @Query("SELECT t FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3")
    Optional<List<TransactionEntity>> findAllByUserEntityAndDateEntity(String username, int year, int month);

    @Query("SELECT SUM(t.transValue) FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3 AND t.transType = ?4 AND t.transCategory = ?5")
    Optional<Double> sumTransValueByUserEntityAndDateEntityAndCategory(String username, int year, int month, Boolean transType, String transCategory);

}
