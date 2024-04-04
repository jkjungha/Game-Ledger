package com.example.GLServer.service;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserEntity userEntity;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, UserEntity userEntity){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userEntity = userEntity;
    }

    public void joinAuth(String emailPhone) {
        //이메일 or 핸드폰 or 둘 다 아닌 경우 구분
        if(true){
            Boolean isExist = userRepository.existsByEmail(emailPhone);
            if(isExist){
                return;
            }
            userEntity.setEmail(emailPhone);
        }else{
            Boolean isExist = userRepository.existsByPhone(emailPhone);
            if(isExist){
                return;
            }
            userEntity.setPhone(emailPhone);
        }
    }

    public void joinAuthCheck(String authCode) {
        //인증코드 발급

    }

    public void joinUser(UsernamePasswordDTO usernamePasswordDTO) {
        String username = usernamePasswordDTO.getUsername();
        String password = usernamePasswordDTO.getPassword();

        //아이디 중복 확인
        Boolean isExist = userRepository.existsByUsername(username);
        if(isExist){
            return;
        }

        userEntity.setUsername(username);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
    }

    public void joinInput(JoinInfoDTO joinInfoDto) {
        userEntity.setGoal_name(joinInfoDto.getGoalName());
        userEntity.setGoal_value(joinInfoDto.getGoalValue());
        userEntity.setFood_value(joinInfoDto.getFoodValue());
        userEntity.setTraffic_value(joinInfoDto.getTrafficValue());
        userEntity.setCulture_value(joinInfoDto.getCultureValue());
        userEntity.setLife_value(joinInfoDto.getLifeValue());

        userRepository.save(userEntity);
    }

}
