package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.SavingRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestService {

    private final DateRepository dateRepository;
    private final UserRepository userRepository;
    private final SavingRepository savingRepository;

    public QuestService(DateRepository dateRepository, UserRepository userRepository, SavingRepository savingRepository) {
        this.dateRepository = dateRepository;
        this.userRepository = userRepository;
        this.savingRepository = savingRepository;
    }

    public DateEntity getDayDateEntity(){
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
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

    public Optional<List<DateEntity>> getMonthDateEntities(){
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        return Optional.ofNullable(dateRepository.findAllByYearAndMonth(year, month));
    }

    public ResponseData questInfo(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        ResponseData responseData = new ResponseData();
        Map<String, Object> result = new HashMap<>();

        DateEntity dateEntity = getDayDateEntity();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);

        if(savingEntity.isPresent()){
            SavingEntity SE = savingEntity.get();

            System.out.println(SE);
            Map<String, Object> food = new HashMap<>();
            food.put("goal", userEntity.getFoodValue());
            food.put("expend", userEntity.getFoodValue() - SE.getSavingFood());
            food.put("isAchieved", SE.isFoodAchieved());
            result.put("food", food);

            Map<String, Object> traffic = new HashMap<>();
            traffic.put("goal", userEntity.getTrafficValue());
            traffic.put("expend", userEntity.getTrafficValue() - SE.getSavingTraffic());
            traffic.put("isAchieved", SE.isTrafficAchieved());
            result.put("traffic", traffic);

            Map<String, Object> culture = new HashMap<>();
            culture.put("goal", userEntity.getCultureValue());
            culture.put("expend", userEntity.getCultureValue() - SE.getSavingCulture());
            culture.put("isAchieved", SE.isCultureAchieved());
            result.put("culture", culture);

            Map<String, Object> life = new HashMap<>();
            life.put("goal", userEntity.getLifeValue());
            life.put("expend", userEntity.getLifeValue() - SE.getSavingLife());
            life.put("isAchieved", SE.isLifeAchieved());
            result.put("life", life);
        }

        responseData.setResult(result);

        return responseData;
    }

}
