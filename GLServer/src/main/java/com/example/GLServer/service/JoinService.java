package com.example.GLServer.service;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.CertificationDao;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.SavingRepository;
import com.example.GLServer.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final DateRepository dateRepository;
    private final SavingRepository savingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{4}-\\d{4}";

    private final CertificationDao certificationDao;

    private String email;
    private String phone;
    private String username;
    private String password;

    public JoinService(UserRepository userRepository, DateRepository dateRepository, SavingRepository savingRepository, BCryptPasswordEncoder bCryptPasswordEncoder, CertificationDao smsCertificationDao){
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.savingRepository = savingRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.certificationDao = smsCertificationDao;
    }

    //현재 시각 DataEntity를 리턴하는 함수
    public DateEntity getDayDateEntity(){
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
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

    @Scheduled(cron = "0 0 5 * * *") // 매일 오전 5시에 실행되는 함수
    public void execute5OclockTask() {
        UserEntity userEntity = userRepository.findByUsername(username);
        DateEntity dateEntity = getDayDateEntity();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);

        //퀘스트 갱신
        if(savingEntity.isEmpty()){
            SavingEntity SE = new SavingEntity();
            SE.setUserEntity(userEntity);
            SE.setDateEntity(dateEntity);
            SE.setSavingFood(0);
            SE.setFoodAchieved(false);
            SE.setSavingTraffic(0);
            SE.setTrafficAchieved(false);
            SE.setSavingCulture(0);
            SE.setCultureAchieved(false);
            SE.setSavingLife(0);
            SE.setLifeAchieved(false);
            SE.setSavingEtc(0);
            SE.setEtcAchieved(false);
            savingRepository.save(SE);
        }

    }

    //등록된 회원인지 확인하는 함수
    public String joinAuth(String emailPhone, Boolean type) throws UnsupportedEncodingException {
        //이메일 or 핸드폰 or 둘 다 아닌 경우 구분
        if(type){//이메일인 경우
            if(isValidEmail(emailPhone)){
                Boolean isExist = userRepository.existsByEmail(emailPhone);
                if(isExist){
                    return "이미 해당 이메일로 등록된 회원이 있습니다.";
                }else{
                    this.email = URLDecoder.decode(emailPhone, StandardCharsets.UTF_8);
//                    sendEmail(emailPhone);
                    return "ok";
                }
            }else{
                return "이메일 형식에 맞지 않습니다.";
            }
        }else{//핸드폰인 경우
            if(isValidPhone(emailPhone)){
                Boolean isExist = userRepository.existsByPhone(emailPhone);
                if(isExist){
                    return "이미 해당 전화번호로 등록된 회원이 있습니다.";
                }else{
                    this.phone = emailPhone;
//                    sendSms(emailPhone);
                    return "ok";
                }
            }else{
                return "전화번호 형식에 맞지 않습니다.";
            }
        }
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public void joinAuthCheck(String authCode) {

    }


    public String joinUser(UsernamePasswordDTO usernamePasswordDTO) {
        String username = usernamePasswordDTO.getUsername();
        String password = usernamePasswordDTO.getPassword();
        String againPassword = usernamePasswordDTO.getAgainPassword();

        if(!Objects.equals(password, againPassword)){
            return "비밀번호가 일치하지 않습니다.";
        }

        //아이디 중복 확인
        Boolean isExist = userRepository.existsByUsername(username);
        if(isExist){
            return "이미 존재하는 아이디입니다.";
        }else{
            this.username = username;
            this.password = bCryptPasswordEncoder.encode(password);
            return "ok";
        }
    }


    public void joinInput(JoinInfoDTO joinInfoDto) {
        UserEntity userEntity = getUserEntity(joinInfoDto);
        userRepository.save(userEntity);

//        SavingEntity savingEntity = getSavingEntity(userEntity);
//        savingRepository.save(savingEntity);

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

    private SavingEntity getSavingEntity(UserEntity userEntity) {
        SavingEntity savingEntity = new SavingEntity();
        savingEntity.setUserEntity(userEntity);
        savingEntity.setDateEntity(getDayDateEntity());
        savingEntity.setSavingFood(0);
        savingEntity.setFoodAchieved(false);
        savingEntity.setSavingTraffic(0);
        savingEntity.setTrafficAchieved(false);
        savingEntity.setSavingCulture(0);
        savingEntity.setCultureAchieved(false);
        savingEntity.setSavingLife(0);
        savingEntity.setLifeAchieved(false);
        savingEntity.setSavingEtc(0);
        savingEntity.setEtcAchieved(false);
        return savingEntity;
    }

}
