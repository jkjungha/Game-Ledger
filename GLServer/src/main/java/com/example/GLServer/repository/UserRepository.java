package com.example.GLServer.repository;

import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    UserEntity findByUsername(String username);

}


