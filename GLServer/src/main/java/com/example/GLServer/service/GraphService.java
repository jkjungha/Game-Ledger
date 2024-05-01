package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GraphService {
    private final UserRepository userRepository;
    private final DateRepository dateRepository;

    private final SavingRepository savingRepository;
    private final TransactionRepository transactionRepository;


    public GraphService(UserRepository userRepository, DateRepository dateRepository, SavingRepository savingRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.savingRepository = savingRepository;
        this.transactionRepository = transactionRepository;
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

    public ResponseData graphInfo(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        ResponseData responseData = new ResponseData();
        DateEntity dateEntity = getDayDateEntity();
        int year = dateEntity.getYear();
        int month = dateEntity.getMonth();
        int day = dateEntity.getDay();

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> expendCategoryGraph = new HashMap<>();
        expendCategoryGraph.put("food", transactionRepository.sumTransValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "food"));
        expendCategoryGraph.put("traffic",transactionRepository.sumTransValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "traffic"));
        expendCategoryGraph.put("culture", transactionRepository.sumTransValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "culture"));
        expendCategoryGraph.put("life",transactionRepository.sumTransValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "life"));
        expendCategoryGraph.put("etc",transactionRepository.sumTransValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "etc"));

        List<Object> savedGraph = new ArrayList<>();
        List<DateEntity> dateEntities = dateRepository.findAllByYearAndMonth(year, month);
        for(DateEntity DE : dateEntities) {
            Map<String, Object> saved = new HashMap<>();
            Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(DE, userEntity);
            System.out.println(savingEntity);
            if(DE.getDay() == day){
                continue;
            }
            if(savingEntity.isPresent()){
                SavingEntity SE = savingEntity.get();
                saved.put("year", DE.getYear());
                saved.put("month", DE.getMonth());
                saved.put("day", DE.getDay());
                int total = SE.getSavingFood();
                total += SE.getSavingTraffic();
                total += SE.getSavingCulture();
                total += SE.getSavingLife();
                saved.put("savedValue", total);
                savedGraph.add(saved);
            }
        }

        result.put("expendCategoryGraph", expendCategoryGraph);
        result.put("savingGraph", savedGraph);

        responseData.setResult(result);

        return responseData;

    }
}
