package com.example.GLServer.jwt;

import com.example.GLServer.dto.MyUserDetails;
import com.example.GLServer.entity.DateEntity;
import com.example.GLServer.entity.SavingEntity;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.DateRepository;
import com.example.GLServer.repository.SavingRepository;
import com.example.GLServer.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

//    private static final int MIN_USERNAME_LENGTH = 5;
//    private static final int MAX_USERNAME_LENGTH = 15;
//    private static final int MIN_PASSWORD_LENGTH = 5;
//    private static final int MAX_PASSWORD_LENGTH = 45;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final SavingRepository savingRepository;
    private final DateRepository dateRepository;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, UserRepository userRepository, SavingRepository savingRepository, DateRepository dateRepository, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.savingRepository = savingRepository;
        this.dateRepository = dateRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
//        if (username == null || username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
//            throw new BadCredentialsException("Invalid username length");
//        }
//
//        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
//            throw new BadCredentialsException("Invalid password length");
//        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)  {
        MyUserDetails myUserDetails = (MyUserDetails) authResult.getPrincipal();

        String username = myUserDetails.getUsername();
        makeQuestTask(username);
        System.out.println("username : "+username);

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        System.out.println("role"+role);

        String token = jwtUtil.createJwt(username, role, 6L);

        response.addHeader("Authorization", "Bearer " + token);
    }

    //현재 시각 DataEntity를 리턴하는 함수
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

    //로그인한 시간에 따른 퀘스트 갱신 함수
    public void makeQuestTask(String username) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        UserEntity userEntity = userRepository.findByUsername(username);
        DateEntity dateEntity = getDayDateEntity();
        Optional<SavingEntity> savingEntity = savingRepository.findByDateEntityAndUserEntity(dateEntity, userEntity);

        //퀘스트 갱신
        if(savingEntity.isEmpty()){
            if(hour < 5){
                dateEntity.setDay(localDateTime.getDayOfMonth()-1);
            }
            SavingEntity SE = new SavingEntity();
            SE.setUserEntity(userEntity);
            SE.setDateEntity(dateEntity);
            SE.setSavingFood(userEntity.getFoodValue());
            SE.setFoodAchieved(false);
            SE.setSavingTraffic(userEntity.getTrafficValue());
            SE.setTrafficAchieved(false);
            SE.setSavingCulture(userEntity.getCultureValue());
            SE.setCultureAchieved(false);
            SE.setSavingLife(userEntity.getLifeValue());
            SE.setLifeAchieved(false);
            SE.setSavingEtc(userEntity.getEtcValue());
            SE.setEtcAchieved(false);
            savingRepository.save(SE);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
