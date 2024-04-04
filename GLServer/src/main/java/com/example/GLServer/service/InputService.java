package com.example.GLServer.service;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class InputService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public InputService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public ResponseData inputInfo(String username, InputInfoDTO inputInfoDTO) {
        UserEntity userEntity = userRepository.findByUsername(username);

        int userId = userEntity.getId();
        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setUser_id(userId);
        transactionEntity.setTrans_year(inputInfoDTO.getTransYear());
        transactionEntity.setTrans_month(inputInfoDTO.getTransMonth());
        transactionEntity.setTrans_day(inputInfoDTO.getTransDay());
        transactionEntity.setTrans_type(inputInfoDTO.isTransType());
        transactionEntity.setTrans_category(inputInfoDTO.getTranCategory());
        transactionEntity.setTrans_name(inputInfoDTO.getTransName());
        transactionEntity.setTrans_value(inputInfoDTO.getTransValue());
        transactionRepository.save(transactionEntity);

        ResponseData responseData = new ResponseData();

        return responseData;

    }
}
