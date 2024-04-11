package com.example.GLServer.service;

import com.example.GLServer.dto.JoinInfoDTO;
import com.example.GLServer.dto.UsernamePasswordDTO;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.TransactionEntity;
import com.example.GLServer.entity.UserEntity;
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
import java.util.Optional;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final DateRepository dateRepository;
    private final SavingRepository savingRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private String email;
    private String phone;
    private String username;
    private String password;

    public JoinService(UserRepository userRepository, DateRepository dateRepository, SavingRepository savingRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.dateRepository = dateRepository;
        this.savingRepository = savingRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

    @Scheduled(cron = "0 0 5 * * *") // 매일 오전 5시에 실행
    public void execute5OclockTask() {
        UserEntity userEntity = userRepository.findByUsername(username);
        DateEntity dateEntity = getDayDateEntity();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);

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
        userEntity.setGoalName(joinInfoDto.getGoalName());
        userEntity.setGoalValue(joinInfoDto.getGoalValue());
        userEntity.setFoodValue(joinInfoDto.getFoodValue());
        userEntity.setTrafficValue(joinInfoDto.getTrafficValue());
        userEntity.setCultureValue(joinInfoDto.getCultureValue());
        userEntity.setLifeValue(joinInfoDto.getLifeValue());
        userEntity.setEtcValue(0);
        userEntity.setRole("USER");
        userRepository.save(userEntity);

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
        savingRepository.save(savingEntity);

    }

}
