package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public ResponseData graphInfo(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        ResponseData responseData = new ResponseData();
        DateEntity dateEntity = getDayDateEntity();
        int year = dateEntity.getYear();
        int month = dateEntity.getMonth();

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> expendCategoryGraph = new HashMap<>();
        expendCategoryGraph.put("food", transactionRepository.sumTranValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "food"));
        expendCategoryGraph.put("traffic",transactionRepository.sumTranValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "traffic"));
        expendCategoryGraph.put("culture", transactionRepository.sumTranValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "culture"));
        expendCategoryGraph.put("life",transactionRepository.sumTranValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "life"));
        expendCategoryGraph.put("etc",transactionRepository.sumTranValueByUserEntityAndDateEntityAndCategory(username, year, month,false, "etc"));

        List<Object> savingGraph = new ArrayList<>();
        List<DateEntity> dateEntities = dateRepository.findAllByYearAndMonth(year, month);
        for(DateEntity DE : dateEntities) {
            Map<String, Object> saving = new HashMap<>();
            Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(DE, userEntity);
            if(savingEntity.isPresent()){
                SavingEntity SE = savingEntity.get();
                saving.put("year", DE.getYear());
                saving.put("month", DE.getMonth());
                saving.put("day", DE.getDay());
                int total = userEntity.getFoodValue() - SE.getSavingFood();
                total += userEntity.getTrafficValue() - SE.getSavingTraffic();
                total += userEntity.getCultureValue() - SE.getSavingCulture();
                total+= userEntity.getLifeValue() - SE.getSavingLife();
                saving.put("savedValue", total);
                savingGraph.add(saving);
            }
        }

        result.put("expendCategoryGraph", expendCategoryGraph);
        result.put("savingGraph", savingGraph);

        responseData.setResult(result);

        return responseData;

    }
}
