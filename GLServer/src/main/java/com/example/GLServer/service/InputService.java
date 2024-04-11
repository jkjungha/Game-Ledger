package com.example.GLServer.service;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InputService {
    private final UserRepository userRepository;
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;

    public InputService(UserRepository userRepository, DateRepository dateRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.transactionRepository = transactionRepository;
    }

    public DateEntity getDayDateEntity(){
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        DateEntity dateEntity = dateRepository.findAllByYearAndMonthAndDay(year, month, day);

        if(dateEntity == null){
            DateEntity DE = new DateEntity();
            DE.setYear(year);
            DE.setMonth(month);
            DE.setDay(day);
            dateRepository.save(DE);
            return DE;
        }

        return dateEntity;
    }

    public ResponseData inputInfo(String username, InputInfoDTO inputInfoDTO) {
        UserEntity userEntity = userRepository.findByUsername(username);

        DateEntity dateEntity = getDayDateEntity();

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setUserEntity(userEntity);
        transactionEntity.setDateEntity(dateEntity);
        transactionEntity.setTranType(inputInfoDTO.isTransType());
        transactionEntity.setTranCategory(inputInfoDTO.getTransCategory());
        transactionEntity.setTranName(inputInfoDTO.getTransName());
        transactionEntity.setTranValue(inputInfoDTO.getTransValue());
        transactionRepository.save(transactionEntity);

        ResponseData responseData = new ResponseData();

        return responseData;

    }
}
