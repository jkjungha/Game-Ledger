package com.example.GLServer.service;

import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.ResponseData;
import com.example.GLServer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MainService {

    private final UserRepository userRepository;

    public MainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseData mainInfo(String username) {
        ResponseData responseData = new ResponseData();
        Map<String, Object> dic = new HashMap<>();

        UserEntity userEntity = userRepository.findByUsername(username);
        dic.put("username", userEntity.getUsername());
        dic.put("goalName", userEntity.getGoalName());
        dic.put("goalAchieved", userEntity.getGoalAchieved());
        dic.put("goalValue", userEntity.getGoalValue());

        responseData.setResult(dic);

        return responseData;
    }
}
