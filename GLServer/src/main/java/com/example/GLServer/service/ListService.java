package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ListService {
    private final UserRepository userRepository;
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;

    public ListService(UserRepository userRepository, DateRepository dateRepository, TransactionRepository transactionRepository) {
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


    public ResponseData listInfo(String username) {
        DateEntity dateEntity = getDayDateEntity();
        int year = dateEntity.getYear();
        int month = dateEntity.getMonth();


        ResponseData responseData = new ResponseData();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> total = new HashMap<>();
        List<Object> list = new ArrayList<>();
        total.put("expendTotal", transactionRepository.sumTranValueByUserEntityAndDateEntityAndTranType(username, year, month, false));
        total.put("incomeTotal", transactionRepository.sumTranValueByUserEntityAndDateEntityAndTranType(username, year, month, true));
        result.put("total", total);


        Optional<List<TransactionEntity>> tranList = transactionRepository.findAllByUserEntityAndDateEntity(username, year, month);

        if(tranList.isPresent()) {
            List<TransactionEntity> TL = tranList.get();
            for (TransactionEntity tran : TL) {
                DateEntity DE = tran.getDateEntity();
                Map<String, Object> dic = new HashMap<>();
                dic.put("tranType", tran.isTranType());
                dic.put("tranYear", DE.getYear());
                dic.put("tranMonth", DE.getMonth());
                dic.put("tranDay", DE.getDay());
                dic.put("tranCategory", tran.getTranCategory());
                dic.put("tranName", tran.getTranName());
                dic.put("tranValue", tran.getTranValue());
                list.add(dic);
            }
        }
        result.put("list", list);
        responseData.setResult(result);
        System.out.println(responseData);

        return responseData;
    }
}
