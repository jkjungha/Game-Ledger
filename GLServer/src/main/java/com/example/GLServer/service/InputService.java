package com.example.GLServer.service;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class InputService {
    private final UserRepository userRepository;
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final SavingRepository savingRepository;

    public InputService(UserRepository userRepository, DateRepository dateRepository, TransactionRepository transactionRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SavingRepository savingRepository) {
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.transactionRepository = transactionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.savingRepository = savingRepository;
    }

    public DateEntity searchDayDateEntity(int year, int month, int day) {
        DateEntity dateEntity = dateRepository.findByYearAndMonthAndDay(year, month, day);

        if (dateEntity == null) {
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

        if (!inputInfoDTO.isTransType()) {
            return inputExpendCalculate(userEntity, dateEntity, inputInfoDTO.getTransValue(), inputInfoDTO.getTransCategory());
        }else{
            return new ResponseData();
        }

    }

    public ResponseData inputExpendCalculate(UserEntity userEntity, DateEntity dateEntity, int transValue, String transCategory) {
        ResponseData responseData = new ResponseData();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);
        if (savingEntity.isPresent()) {
            SavingEntity SE = savingEntity.get();
            int value = transValue;
            if (Objects.equals(transCategory.trim(), "식비")) {
                int tmp = SE.getSavingFood();
                SE.setSavingFood(Math.max(tmp - value, 0));
            } else if (Objects.equals(transCategory.trim(), "교통")) {
                int tmp = SE.getSavingTraffic();
                SE.setSavingTraffic(Math.max(tmp - value, 0));
            } else if (Objects.equals(transCategory.trim(), "문화")) {
                int tmp = SE.getSavingCulture();
                SE.setSavingCulture(Math.max(tmp - value, 0));
            } else if (Objects.equals(transCategory.trim(), "생활")) {
                int tmp = SE.getSavingLife();
                SE.setSavingLife(Math.max(tmp - value, 0));
            } else if (Objects.equals(transCategory.trim(), "기타")) {
                int tmp = SE.getSavingEtc();
                SE.setSavingEtc(Math.max(tmp - value, 0));
            }
            savingRepository.save(SE);
        }
        return responseData;
    }

    public ResponseData inputIncomeCalculate(UserEntity userEntity, DateEntity dateEntity, int transValue, String transCategory) {
        ResponseData responseData = new ResponseData();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);
        if (savingEntity.isPresent()) {
            SavingEntity SE = savingEntity.get();
            int value = transValue;
            if (Objects.equals(transCategory.trim(), "식비")) {
                int tmp = SE.getSavingFood();
                SE.setSavingFood(Math.min(tmp + value, userEntity.getFoodValue()));
            } else if (Objects.equals(transCategory.trim(), "교통")) {
                int tmp = SE.getSavingTraffic();
                SE.setSavingTraffic(Math.min(tmp + value, userEntity.getTrafficValue()));
            } else if (Objects.equals(transCategory.trim(), "문화")) {
                int tmp = SE.getSavingCulture();
                SE.setSavingCulture(Math.min(tmp + value, userEntity.getCultureValue()));
            } else if (Objects.equals(transCategory.trim(), "생활")) {
                int tmp = SE.getSavingLife();
                SE.setSavingLife(Math.min(tmp + value, userEntity.getLifeValue()));
            } else if (Objects.equals(transCategory.trim(), "기타")) {
                int tmp = SE.getSavingEtc();
                SE.setSavingEtc(Math.min(tmp + value, userEntity.getEtcValue()));
            }
            savingRepository.save(SE);
        }
        return responseData;
    }

    public ResponseData settingsEdit(String username, String password, String newPassword) {
        UserEntity userEntity = userRepository.findByUsername(username);
        String oriPassword = userEntity.getPassword();
        ResponseData responseData = new ResponseData();
        if(!bCryptPasswordEncoder.matches(password, oriPassword)){
            responseData.setCode(400);
            responseData.setMessage("입력하신 비밀번호가 틀렸습니다.");
        }else{
            String inputPassword = bCryptPasswordEncoder.encode(newPassword);
            userEntity.setPassword(inputPassword);
            userRepository.save(userEntity);
            responseData.setMessage("비밀번호를 변경하였습니다.");
        }
        return responseData;
    }

    public ResponseData settingsLogout(String username) {
        System.out.println("logging out : "+ username);
        ResponseData responseData = new ResponseData();
        SecurityContextHolder.clearContext();
        responseData.setMessage("로그아웃");
        return responseData;
    }


    public ResponseData settingsSignout(String username) {
        System.out.println("signing out : "+ username);
        ResponseData responseData = new ResponseData();
        UserEntity userEntity = userRepository.findByUsername(username);
        userRepository.delete(userEntity);
        SecurityContextHolder.clearContext();
        responseData.setMessage("탈퇴");
        return responseData;
    }
}
