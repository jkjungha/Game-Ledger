package com.example.GLServer.service;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private String email;
    private String phone;
    private String username;
    private String password;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinAuth(String emailPhone) throws UnsupportedEncodingException {
        //이메일 or 핸드폰 or 둘 다 아닌 경우 구분
        if(true){
            Boolean isExist = userRepository.existsByEmail(emailPhone);
            if(isExist){
                return;
            }
            this.email = URLDecoder.decode(emailPhone, StandardCharsets.UTF_8);
        }else{
            Boolean isExist = userRepository.existsByPhone(emailPhone);
            if(isExist){
                return;
            }
            this.phone = emailPhone;
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

        this.username = username;
        this.password = bCryptPasswordEncoder.encode(password);
    }

    public void joinInput(JoinInfoDTO joinInfoDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(this.email);
        userEntity.setPhone(this.phone);
        userEntity.setUsername(this.username);
        userEntity.setPassword(this.password);
        userEntity.setGoal_name(joinInfoDto.getGoalName());
        userEntity.setGoal_value(joinInfoDto.getGoalValue());
        userEntity.setFood_value(joinInfoDto.getFoodValue());
        userEntity.setTraffic_value(joinInfoDto.getTrafficValue());
        userEntity.setCulture_value(joinInfoDto.getCultureValue());
        userEntity.setLife_value(joinInfoDto.getLifeValue());
        userEntity.setRole("ADMIN");

        userRepository.save(userEntity);
    }

}
