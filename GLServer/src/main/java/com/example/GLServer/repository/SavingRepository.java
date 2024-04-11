package com.example.GLServer.repository;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingRepository extends JpaRepository<SavingEntity, Integer> {
    Optional<SavingEntity> findByDateEntityAndUserEntity(DateEntity dateEntity, UserEntity userEntity);
}
