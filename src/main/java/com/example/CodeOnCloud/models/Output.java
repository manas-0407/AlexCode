package com.example.CodeOnCloud.models;

import lombok.Data;

@Data
public class Output {
    StringBuilder output;

    public Output(){
        output = new StringBuilder();
    }

    public void updateOutput(String s) {
        output.append(s).append("\n");
    }
}
