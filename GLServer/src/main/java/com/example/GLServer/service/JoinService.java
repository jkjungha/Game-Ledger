package com.example.GLServer.service;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JoinService {

    private final MailService mailService;
    private final PhoneService phoneService;
    private final UserRepository userRepository;
    private final DateRepository dateRepository;
    private final SavingRepository savingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{4}-\\d{4}";

    private String email;
    private String phone;
    private String username;
    private String password;

    public JoinService(MailService mailService, PhoneService phoneService, UserRepository userRepository, DateRepository dateRepository, SavingRepository savingRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.mailService = mailService;
        this.phoneService = phoneService;
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.savingRepository = savingRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public DateEntity getDayDateEntity() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
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

    //등록된 회원인지 확인하는 함수
    public ResponseData joinAuth(String emailPhone, Boolean type) throws UnsupportedEncodingException {
        ResponseData responseData = new ResponseData();
        //이메일 or 핸드폰 or 둘 다 아닌 경우 구분
        if (type) {//이메일인 경우
            if (isValidEmail(emailPhone)) {
                Boolean isExist = userRepository.existsByEmail(emailPhone);
                if (isExist) {
                    responseData.setCode(400);
                    responseData.setMessage("이미 해당 이메일로 등록된 회원이 있습니다.");
                } else {
                    return mailService.sendEmail(emailPhone);
                }
            } else {
                responseData.setCode(400);
                responseData.setMessage("이메일 형식에 맞지 않습니다.");
            }
        } else {//핸드폰인 경우
            if (isValidPhone(emailPhone)) {
                Boolean isExist = userRepository.existsByPhone(emailPhone);
                if (isExist) {
                    responseData.setCode(400);
                    responseData.setMessage("이미 해당 전화번호로 등록된 회원이 있습니다.");
                } else {
                    return phoneService.sendPhone(emailPhone);
                }
            } else {
                responseData.setCode(400);
                responseData.setMessage("전화번호 형식에 맞지 않습니다.");
            }
        }
        return responseData;
    }

    public ResponseData joinAuthCheck(String emailPhone, String authCode, Boolean type) {
        if (type) {
            ResponseData responseData = mailService.authCheckEmail(emailPhone, authCode);
            if (responseData.getCode() == 200) {
                this.email = emailPhone;
            }
            return responseData;
        } else {
            ResponseData responseData = phoneService.authCheckPhone(emailPhone, authCode);
            if (responseData.getCode() == 200) {
                this.phone = emailPhone;
            }
            return responseData;
        }
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhone(String phone) {
        return phone.length() == 11;

//        Pattern pattern = Pattern.compile(PHONE_REGEX);
//        Matcher matcher = pattern.matcher(phone);
//        return matcher.matches();
    }

    public ResponseData joinUser(UsernamePasswordDTO usernamePasswordDTO) {
        ResponseData responseData = new ResponseData();
        String username = usernamePasswordDTO.getUsername();
        String password = usernamePasswordDTO.getPassword();
        String againPassword = usernamePasswordDTO.getAgainPassword();

        if (!Objects.equals(password, againPassword)) {
            responseData.setCode(400);
            responseData.setMessage("비밀번호가 일치하지 않습니다.");
            return responseData;
        }

        //아이디 중복 확인
        Boolean isExist = userRepository.existsByUsername(username);
        if (isExist) {
            responseData.setCode(400);
            responseData.setMessage("이미 존재하는 아이디입니다.");
        } else {

            this.username = username;
            this.password = bCryptPasswordEncoder.encode(password);
            responseData.setMessage("요청 완료");
        }
        return responseData;
    }


    public ResponseData joinInput(JoinInfoDTO joinInfoDto) {
        ResponseData responseData = new ResponseData();
        UserEntity userEntity = getUserEntity(joinInfoDto);
        userRepository.save(userEntity);

        initUserEntity();

//        execute5amTask(userEntity);
        responseData.setMessage("요청 완료");

        return responseData;

    }

    private void initUserEntity() {
        this.email = "";
        this.phone = "";
        this.username = "";
        this.password = "";
    }

    private UserEntity getUserEntity(JoinInfoDTO joinInfoDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(this.email);
        userEntity.setPhone(this.phone);
        userEntity.setUsername(this.username);
        userEntity.setPassword(this.password);
        userEntity.setGoalName(joinInfoDto.getGoalName());
        userEntity.setGoalValue(joinInfoDto.getGoalValue());
        userEntity.setFoodValue(joinInfoDto.getFoodValue());
        userEntity.setTrafficValue(joinInfoDto.getTrafficValue());
        userEntity.setCultureValue(joinInfoDto.getCultureValue());
        userEntity.setLifeValue(joinInfoDto.getLifeValue());
        userEntity.setEtcValue(0);
        userEntity.setRole("USER");
        return userEntity;
    }

    // 매일 오전 5시에 실행
//    @Scheduled(cron = "0 0 5 * * *")
//    public void execute5amTask(UserEntity userEntity) {
//        LocalDateTime localDateTime = LocalDateTime.now();
//        int year = localDateTime.getYear();
//        int month = localDateTime.getMonthValue();
//        int day = localDateTime.getDayOfMonth();
//        DateEntity dateEntity = dateRepository.findByYearAndMonthAndDay(year, month, day);
//        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);
//        if (dateEntity != null && savingEntity.isPresent()) {
//            SavingEntity SE = savingEntity.get();
//            int total = SE.getSavingFood();
//            total += SE.getSavingTraffic();
//            total += SE.getSavingCulture();
//            total += SE.getSavingLife();
//
//            int achieved = userEntity.getGoalAchieved();
//            userEntity.setGoalAchieved(achieved + total);
//        }
//    }
}
