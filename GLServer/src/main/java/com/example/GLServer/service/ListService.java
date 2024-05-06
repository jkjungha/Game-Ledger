package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ListService {
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;

    public ListService(DateRepository dateRepository, TransactionRepository transactionRepository) {
        this.dateRepository = dateRepository;
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


    public ResponseData listInfo(String username) {
        DateEntity dateEntity = getDayDateEntity();
        int year = dateEntity.getYear();
        int month = dateEntity.getMonth();

        ResponseData responseData = new ResponseData();

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> total = new HashMap<>();
        List<Object> list = new ArrayList<>();

        Optional<Double> expendTotal = transactionRepository.sumTransValueByUserEntityAndDateEntityAndTranType(username, year, month, false);
        if (expendTotal.isEmpty()){
            expendTotal = Optional.of(0.0);
        }
        Optional<Double> incomeTotal = transactionRepository.sumTransValueByUserEntityAndDateEntityAndTranType(username, year, month, true);
        if (incomeTotal.isEmpty()){
            incomeTotal = Optional.of(0.0);
        }

        total.put("expendTotal", expendTotal);
        total.put("incomeTotal", incomeTotal);
        result.put("total", total);

        Optional<List<TransactionEntity>> tranList = transactionRepository.findAllByUserEntityAndDateEntity(username, year, month);

        if(tranList.isPresent()) {
            List<TransactionEntity> TL = tranList.get();
            for (TransactionEntity tran : TL) {
                DateEntity DE = tran.getDateEntity();
                Map<String, Object> dic = new HashMap<>();
                dic.put("transType", tran.isTransType());
                dic.put("transYear", DE.getYear());
                dic.put("transMonth", DE.getMonth());
                dic.put("transDay", DE.getDay());
                dic.put("transCategory", tran.getTransCategory());
                dic.put("transName", tran.getTransName());
                dic.put("transValue", tran.getTransValue());
                list.add(dic);
            }
        }
        result.put("list", list);
        responseData.setResult(result);

        return responseData;
    }
}
