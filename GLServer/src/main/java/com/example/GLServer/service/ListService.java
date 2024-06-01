package com.example.GLServer.service;

import com.example.GLServer.dto.InputInfoDTO;
import com.example.GLServer.dto.ListEditDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.TransactionRepository;
import com.example.GLServer.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ListService {
    private final DateRepository dateRepository;
    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    private final  InputService inputService;

    public ListService(DateRepository dateRepository, TransactionRepository transactionRepository, UserRepository userRepository, InputService inputService) {
        this.dateRepository = dateRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.inputService = inputService;
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
                dic.put("transId", tran.getId());
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

    public ResponseData listEdit(String username, ListEditDTO listEditDTO) {

        ResponseData responseData = new ResponseData();
        UserEntity userEntity = userRepository.findByUsername(username);

        int id = listEditDTO.getTransId();
        boolean transType = listEditDTO.isTransType();
        String transCategory = listEditDTO.getTransCategory();
        int transYear = listEditDTO.getTransYear();
        int transMonth = listEditDTO.getTransMonth();
        int transDay = listEditDTO.getTransDay();
        String transName = listEditDTO.getTransName();
        int transValue = listEditDTO.getTransValue();

        Optional<TransactionEntity> transactionEntity = transactionRepository.findById(id);

        if(transactionEntity.isPresent()) {
            TransactionEntity TE = transactionEntity.get();
            if(TE.isTransType()){
                transactionRepository.delete(TE);
            }else{
                inputService.inputIncomeCalculate(userEntity, TE.getDateEntity(), TE.getTransValue(), TE.getTransCategory());
            }

            DateEntity dateEntity = searchDayDateEntity(transYear,transMonth,transDay);
            TE.setTransType(transType);
            TE.setTransCategory(transCategory);
            TE.setDateEntity(dateEntity);
            TE.setTransName(transName);
            TE.setTransValue(transValue);
            transactionRepository.save(TE);

            if (!transType) {
                return inputService.inputExpendCalculate(userEntity, dateEntity, transValue, transCategory);
            }

            return responseData;
        }else{
            responseData.setCode(400);
            responseData.setMessage("해당하는 내역이 없습니다.");
            return responseData;
        }


    }

}
