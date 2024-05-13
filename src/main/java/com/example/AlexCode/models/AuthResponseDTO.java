package com.example.AlexCode.models;

import lombok.Data;

import java.util.*;
import java.io.*;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken){
        this.accessToken = accessToken;
    }
}
