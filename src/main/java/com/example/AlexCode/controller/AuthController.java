package com.example.AlexCode.controller;

import com.example.AlexCode.models.AuthResponseDTO;
import com.example.AlexCode.models.LoginDTO;
import com.example.AlexCode.models.RegisterDTO;
import com.example.AlexCode.models.UserEntity;
import com.example.AlexCode.repository.UserRepository;
import com.example.AlexCode.security.JWTGenerator;
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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(UserRepository userRepository,PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,JWTGenerator jwtGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){

        if(userRepository.findByUsername(registerDTO.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("User Present");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerDTO.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

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
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().body(new AuthResponseDTO("BAD CREDENTIALS"));
        }

    }

}
