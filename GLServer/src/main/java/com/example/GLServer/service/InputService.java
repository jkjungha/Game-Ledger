package com.example.GLServer.service;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.*;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class InputService {
    private final UserRepository userRepository;
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;

    private final SavingRepository savingRepository;

    public InputService(UserRepository userRepository, DateRepository dateRepository, TransactionRepository transactionRepository, SavingRepository savingRepository) {
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.transactionRepository = transactionRepository;
        this.savingRepository = savingRepository;
    }

    public DateEntity searchDayDateEntity(int year, int month, int day){
        DateEntity dateEntity = dateRepository.findByYearAndMonthAndDay(year, month, day);

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

        DateEntity dateEntity = searchDayDateEntity(inputInfoDTO.getTransYear(), inputInfoDTO.getTransMonth(), inputInfoDTO.getTransDay());

        TransactionEntity transactionEntity = new TransactionEntity();

        transactionEntity.setUserEntity(userEntity);
        transactionEntity.setDateEntity(dateEntity);
        transactionEntity.setTransType(inputInfoDTO.isTransType());
        transactionEntity.setTransCategory(inputInfoDTO.getTransCategory());
        transactionEntity.setTransName(inputInfoDTO.getTransName());
        transactionEntity.setTransValue(inputInfoDTO.getTransValue());
        transactionRepository.save(transactionEntity);

        if(!inputInfoDTO.isTransType()){
            Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);
            if(savingEntity.isPresent()){
                SavingEntity SE = savingEntity.get();
                int value = transactionEntity.getTransValue();
                if(Objects.equals(transactionEntity.getTransCategory().trim(), "식비")){
                    int tmp = SE.getSavingFood();
                    SE.setSavingFood(Math.max(tmp - value, 0));
                }else if(Objects.equals(transactionEntity.getTransCategory().trim(), "교통")){
                    int tmp = SE.getSavingTraffic();
                    SE.setSavingTraffic(Math.max(tmp - value, 0));
                }else if(Objects.equals(transactionEntity.getTransCategory().trim(), "문화")){
                    int tmp = SE.getSavingCulture();
                    SE.setSavingCulture(Math.max(tmp - value, 0));
                }else if(Objects.equals(transactionEntity.getTransCategory().trim(), "생활")){
                    int tmp = SE.getSavingLife();
                    SE.setSavingLife(Math.max(tmp - value, 0));
                }else if(Objects.equals(transactionEntity.getTransCategory().trim(), "기타")){
                    int tmp = SE.getSavingEtc();
                    SE.setSavingEtc(Math.max(tmp - value, 0));
                }
                savingRepository.save(SE);
            }
        }

        //성공했을 시
        ResponseData responseData = new ResponseData();

        return responseData;

    }
}
