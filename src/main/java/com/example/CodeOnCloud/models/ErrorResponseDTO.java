package com.example.CodeOnCloud.models;

public class ErrorResponseDTO {

    public String errorClass;
    public String error;

    public ErrorResponseDTO(String errorClass, String error){
        this.error = error;
        this.errorClass = errorClass;
    }
}
