package com.example.GLServer.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class CertificationDao {

    private final int LIMIT_TIME = 3 * 60;  //인증코드 입력 제한시간

    private final StringRedisTemplate stringRedisTemplate;

    public CertificationDao(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void createCertification(String emailPhone, String certificationNumber) {
        stringRedisTemplate.opsForValue()
                .set(emailPhone, certificationNumber, Duration.ofSeconds(LIMIT_TIME));
    }

    public String getCertification(String emailPhone) {
        return stringRedisTemplate.opsForValue().get(emailPhone);
    }

    public void removeCertification(String emailPhone) {
        stringRedisTemplate.delete(emailPhone);
    }

    public boolean hasKey(String emailPhone) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(emailPhone));
    }
}