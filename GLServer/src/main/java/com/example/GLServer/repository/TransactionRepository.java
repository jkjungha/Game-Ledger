package com.example.GLServer.repository;

import com.example.GLServer.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {
    @Query("SELECT SUM(t.tranValue) FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3 AND t.tranType = ?4")
    Double sumTranValueByUserEntityAndDateEntityAndTranType(String username,int year, int month, Boolean tranType);

    @Query("SELECT t FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3")
    Optional<List<TransactionEntity>> findAllByUserEntityAndDateEntity(String username, int year, int month);

    @Query("SELECT SUM(t.tranValue) FROM TransactionEntity t WHERE t.userEntity.username = ?1 AND t.dateEntity.year = ?2 AND t.dateEntity.month = ?3 AND t.tranType = ?4 AND t.tranCategory = ?5")
    Double sumTranValueByUserEntityAndDateEntityAndCategory(String username, int year, int month, Boolean tranType, String tranCategory);
}
