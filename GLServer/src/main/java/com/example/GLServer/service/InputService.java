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
        transactionEntity.setTranType(inputInfoDTO.isTransType());
        transactionEntity.setTranCategory(inputInfoDTO.getTransCategory());
        transactionEntity.setTranName(inputInfoDTO.getTransName());
        transactionEntity.setTranValue(inputInfoDTO.getTransValue());
        transactionRepository.save(transactionEntity);

        if(!inputInfoDTO.isTransType()){
            Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);
            if(savingEntity.isPresent()){
                SavingEntity SE = savingEntity.get();
                int value = transactionEntity.getTranValue();
                if(Objects.equals(transactionEntity.getTranCategory(), "food")){
                    int tmp = SE.getSavingFood();
                    SE.setSavingFood(tmp - value);
                }else if(Objects.equals(transactionEntity.getTranCategory(), "traffic")){
                    int tmp = SE.getSavingTraffic();
                    SE.setSavingTraffic(tmp - value);
                }else if(Objects.equals(transactionEntity.getTranCategory(), "culture")){
                    int tmp = SE.getSavingCulture();
                    SE.setSavingCulture(tmp - value);
                }else if(Objects.equals(transactionEntity.getTranCategory(), "life")){
                    int tmp = SE.getSavingLife();
                    SE.setSavingLife(tmp - value);
                }else if(Objects.equals(transactionEntity.getTranCategory(), "etc")){
                    int tmp = SE.getSavingEtc();
                    SE.setSavingEtc(tmp - value);
                }
            }
        }

        //성공했을 시
        ResponseData responseData = new ResponseData();

        return responseData;

    }
}
