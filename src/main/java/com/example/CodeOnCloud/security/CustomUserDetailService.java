package com.example.CodeOnCloud.security;

import com.example.CodeOnCloud.models.UserEntity;
import com.example.CodeOnCloud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();
        return new User(userEntity.getUsername() , userEntity.getPassword() , new ArrayList<>());
    }

    // User class need username, password , granular authorities as params ,
    // Granular Authorities are a list of authorities a user own for this project assuming no special authorization is there
    // i.e., every user is equal on Authority basis;

}
