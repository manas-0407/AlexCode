package com.example.CodeOnCloud.controller;

import com.example.CodeOnCloud.models.AuthResponseDTO;
import com.example.CodeOnCloud.models.LoginDTO;
import com.example.CodeOnCloud.models.RegisterDTO;
import com.example.CodeOnCloud.models.UserEntity;
import com.example.CodeOnCloud.repository.UserRepository;
import com.example.CodeOnCloud.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTGenerator jwtGenerator;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){

        if(userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("User Present");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userEntity.setQuestions_prepared(new ArrayList<>());
        userEntity.setContest_prepared(new ArrayList<>());

        userRepository.save(userEntity);
        return ResponseEntity.ok().body("User saved");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername()
                            , loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return ResponseEntity.ok().body(new AuthResponseDTO(token));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new AuthResponseDTO("BAD CREDENTIALS"));
        }

    }

}
