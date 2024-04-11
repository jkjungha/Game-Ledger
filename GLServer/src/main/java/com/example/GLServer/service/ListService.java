package com.example.GLServer.service;

import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public ListService(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }


    public ResponseData listInfo(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        ResponseData responseData = new ResponseData();
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> total = new HashMap<>();
        List<Object> list = new ArrayList<>();
        total.put("expendTotal", transactionRepository.sumTranValueByUserEntityAndTranType(username,false));
        total.put("incomeTotal", transactionRepository.sumTranValueByUserEntityAndTranType(username, true));
        result.put("total", total);


        List<TransactionEntity> tranList = userEntity.getTransactionEntities();


        for(TransactionEntity tran : tranList){
            DateEntity dateEntity = tran.getDateEntity();
            Map<String, Object> dic = new HashMap<>();
            dic.put("tranType", tran.isTranType());
            dic.put("tranYear", dateEntity.getYear());
            dic.put("tranMonth", dateEntity.getMonth());
            dic.put("tranDay", dateEntity.getDay());
            dic.put("tranCategory", tran.getTranCategory());
            dic.put("tranName", tran.getTranName());
            dic.put("tranValue", tran.getTranValue());
            list.add(dic);
        }
        result.put("list", list);
        responseData.setResult(result);
        System.out.println(responseData);

        return responseData;
    }
}
