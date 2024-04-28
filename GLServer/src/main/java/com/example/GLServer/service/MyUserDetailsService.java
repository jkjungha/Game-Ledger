package com.example.GLServer.service;

import com.example.GLServer.dto.MyUserDetails;
import com.example.GLServer.entity.UserEntity;
import com.example.GLServer.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity data = userRepository.findByUsername(username);
        System.out.println("data"+ data);

        if(data != null){
            return new MyUserDetails(data);
        }
        return null;
    }
}
